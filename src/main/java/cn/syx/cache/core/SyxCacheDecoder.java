package cn.syx.cache.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class SyxCacheDecoder extends ByteToMessageDecoder {

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> out) throws Exception {
        int count = in.readableBytes();
        if (count <= 0) {
            return;
        }

        int index = in.readerIndex();
        log.info("count: {}, index: {}", count, index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);
        String ret = new String(bytes);
        log.info("解码内容: {}", ret);

        out.add(ret);
    }
}
