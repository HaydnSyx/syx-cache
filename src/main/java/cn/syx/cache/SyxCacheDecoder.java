package cn.syx.cache;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SyxCacheDecoder extends ByteToMessageDecoder {

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解码次数: " + counter.incrementAndGet());

        int count = in.readableBytes();
        if (count <= 0) {
            return;
        }

        int index = in.readerIndex();
        System.out.println("count" + count + ", index: " + index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);
        String ret = new String(bytes);
        System.out.println("解码内容: " + ret);

        out.add(ret);
    }
}
