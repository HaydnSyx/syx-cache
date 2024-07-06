package cn.syx.cache.codec.impl;

import cn.syx.cache.utils.CodecUtil;
import cn.syx.cache.codec.RespDecoder;
import cn.syx.cache.domain.RedisMessage;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

public class RespSimpleStringDecoder implements RespDecoder<String> {

    @Override
    public RedisMessage<String> decode(ByteBuf buffer) {
        // 查找到第一个\r\n之间的内容
        String content = CodecUtil.readLine(buffer);
        return Objects.isNull(content) ? RedisMessage.fail() : RedisMessage.success(content);
    }
}
