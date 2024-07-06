package cn.syx.cache.codec;

import cn.syx.cache.domain.RedisMessage;
import io.netty.buffer.ByteBuf;

public interface RespDecoder<V> {

    RedisMessage<V> decode(ByteBuf buffer);
}
