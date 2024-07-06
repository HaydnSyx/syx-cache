package cn.syx.cache.utils;

import cn.syx.cache.core.SyxCacheConstants;
import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

public class CodecUtil {

    public static int findLineEndIndex(ByteBuf buffer) {
        int index = buffer.forEachByte(ByteProcessor.FIND_LF);
        return (index > 0 && buffer.getByte(index - 1) == SyxCacheConstants.CR) ? index : -1;
    }

    public static String readLine(ByteBuf buffer) {
        int lineEndIndex = findLineEndIndex(buffer);
        if (lineEndIndex > -1) {
            int lineStartIndex = buffer.readerIndex();
            // 计算字节长度
            int size = lineEndIndex - lineStartIndex - 1;
            byte[] bytes = new byte[size];
            buffer.readBytes(bytes);
            // 重置读游标为\r\n之后的第一个字节
            buffer.readerIndex(lineEndIndex + 1);
            buffer.markReaderIndex();
            return new String(bytes, SyxCacheConstants.UTF_8);
        }
        return null;
    }
}
