package cn.syx.cache.codec;

import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.core.SyxRespDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class DefaultRespCodecTest {

    ByteBuf buffer;
    String value;

    @BeforeEach
    public void before() {
        buffer = ByteBufAllocator.DEFAULT.buffer();
    }

    @Test
    public void testSimpleStr() {
        // +OK\r\n
        buffer.writeBytes("+OK".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("OK", value);
    }

    @Test
    public void testErrStr() {
        // -ERR unknown command 'foobar'\r\n
        buffer.writeBytes("-ERR unknown command 'foobar'".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("ERR unknown command 'foobar'", value);
    }

    @Test
    public void testNumber() {
        // :-1000\r\n
        buffer.writeBytes(":-1000".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        Long number = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals(number, Long.valueOf(-1000));

        // :555\r\n
        buffer.writeBytes(":555".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        number = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals(number, Long.valueOf(555));

        // :555\r\n
        buffer.writeBytes(":0".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        number = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals(number, Long.valueOf(0));
    }

    @Test
    public void testBulkStr() {
        // $9\r\nthrowable\r\n
        buffer.writeBytes("$9".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("throwable".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("throwable", value);

        // $0\r\n\r\n
        buffer.writeBytes("$0".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("", value);

        // $-1\r\n
        buffer.writeBytes("$-1".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertNull(value);
    }

    @Test
    public void testBulkStrPart() {
        // 半包场景
        buffer.writeBytes("$9".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("throw".getBytes(SyxCacheConstants.UTF_8));
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertNull(value);

        buffer.writeBytes("able".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("throwable", value);
    }

    @Test
    public void testBulkStrComplex() {
        // 半包场景
        buffer.writeBytes("$9".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("throwable".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$3".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("abc".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$5".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("123".getBytes(SyxCacheConstants.UTF_8));
        // 验证
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("throwable", value);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("abc", value);
        value = SyxRespDecoder.decode(buffer);
        Assertions.assertNull(value);

        buffer.writeBytes("45".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);

        value = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals("12345", value);
    }


    @Test
    public void testArr() {
        //*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n
        buffer.writeBytes("*5".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$3".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("foo".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$-1".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$0".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$3".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("bar".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        buffer.writeBytes("$-1".getBytes(SyxCacheConstants.UTF_8));
        buffer.writeBytes(SyxCacheConstants.CRLF);
        List<Object> list = SyxRespDecoder.decode(buffer);
        Assertions.assertEquals(5, list.size());
        Assertions.assertEquals("foo", list.get(0));
        Assertions.assertNull(list.get(1));
        Assertions.assertEquals("", list.get(2));
        Assertions.assertEquals("bar", list.get(3));
        Assertions.assertNull(list.get(4));
    }
}
