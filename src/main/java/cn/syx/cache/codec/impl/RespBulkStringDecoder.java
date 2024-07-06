package cn.syx.cache.codec.impl;

import cn.syx.cache.utils.CodecUtil;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.codec.RespDecoder;
import cn.syx.cache.core.SyxRespDecoder;
import cn.syx.cache.domain.RedisMessage;
import io.netty.buffer.ByteBuf;

public class RespBulkStringDecoder implements RespDecoder<String> {

    @Override
    public RedisMessage<String> decode(ByteBuf buffer) {
        int lineEndIndex = CodecUtil.findLineEndIndex(buffer);
        if (-1 == lineEndIndex) {
            return RedisMessage.fail();
        }
        RedisMessage<Long> lengthResult = (RedisMessage<Long>) SyxRespDecoder.DECODERS.get(SyxCacheConstants.ReplyType.NUMBER).decode(buffer);
        if (null == lengthResult || !lengthResult.isSuccess()) {
            return RedisMessage.fail();
        }
        // 成功解析后，当前buffer的readerIndex位置已经指向数据位置
        long length = lengthResult.getData();

        // Bulk Null String
        if (SyxCacheConstants.NEGATIVE_ONE.equals(length)) {
            return RedisMessage.success(null);
        }

        // Bulk Empty String
        if (SyxCacheConstants.ZERO.equals(length)) {
            // 确认下后两个字节是不是\r\n结尾
            int currentIndex = buffer.readerIndex();
            if (buffer.getByte(currentIndex) != SyxCacheConstants.CR || buffer.getByte(currentIndex + 1) != SyxCacheConstants.LF) {
                return RedisMessage.fail();
            }

            // 重置读游标为\r\n之后的第一个字节
            buffer.readerIndex(currentIndex + 2);
            return RedisMessage.success(SyxCacheConstants.EMPTY_STRING);
        }

        // 真实字节内容的长度
        int readLength = (int) length;
        if (buffer.readableBytes() >= readLength + 2) {
            // 读取指定长度的内容
            byte[] bytes = new byte[readLength];
            buffer.readBytes(bytes);
            int currentIndex = buffer.readerIndex();

            // 确认下后两个字节是不是\r\n结尾（这种应该不会出现）
            if (buffer.getByte(currentIndex) != SyxCacheConstants.CR || buffer.getByte(currentIndex + 1) != SyxCacheConstants.LF) {
                return RedisMessage.fail();
            }

            // 重置读游标为\r\n之后的第一个字节
            buffer.readerIndex(currentIndex + 2);
            return RedisMessage.success(new String(bytes, SyxCacheConstants.UTF_8));
        }
        return RedisMessage.fail();
    }
}
