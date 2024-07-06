package cn.syx.cache.codec.impl;

import cn.syx.cache.utils.CodecUtil;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.codec.RespDecoder;
import cn.syx.cache.domain.RedisMessage;
import io.netty.buffer.ByteBuf;

public class RespNumberDecoder implements RespDecoder<Long> {

    @Override
    public RedisMessage<Long> decode(ByteBuf buffer) {
        int lineEndIndex = CodecUtil.findLineEndIndex(buffer);
        // 没有行尾，异常
        if (-1 == lineEndIndex) {
            return RedisMessage.fail();
        }
        long result = 0L;
        int lineStartIndex = buffer.readerIndex();
        boolean negative = false;
        byte firstByte = buffer.getByte(lineStartIndex);
        // 负数
        if (SyxCacheConstants.MINUS_BYTE == firstByte) {
            negative = true;
        } else {
            result = firstByte - '0';
        }

        // 剩余数字
        for (int i = lineStartIndex + 1; i < (lineEndIndex - 1); i++) {
            byte value = buffer.getByte(i);
            int digit = value - '0';
            result = result * 10 + digit;
        }

        if (negative) {
            result = -result;
        }

        // 重置读游标为\r\n之后的第一个字节
        buffer.readerIndex(lineEndIndex + 1);
        return RedisMessage.success(result);
    }
}
