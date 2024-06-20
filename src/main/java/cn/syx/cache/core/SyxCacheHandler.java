package cn.syx.cache.core;

import cn.syx.cache.domain.CacheTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyxCacheHandler extends SimpleChannelInboundHandler<String> {

    private final SyxCacheTask task;

    public SyxCacheHandler(SyxCacheTask runnable) {
        this.task = runnable;
        new Thread(task, "cache-task").start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        CacheTask ct = CacheTask.builder()
                .ctx(ctx)
                .message(message)
                .build();
        task.submitTask(ct);
    }
}
