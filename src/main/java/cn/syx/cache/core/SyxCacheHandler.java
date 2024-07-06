package cn.syx.cache.core;

import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.CacheTask;
import io.github.haydnsyx.toolbox.base.NumberTool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static cn.syx.cache.core.SyxCacheConstants.DB_KEY;

@Slf4j
public class SyxCacheHandler extends SimpleChannelInboundHandler<String[]> {


    private final SyxCacheTask task;

    public SyxCacheHandler(SyxCacheTask task) {
        this.task = task;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String[] request) throws Exception {
        task.submitTask(CacheTask.create(ctx, request));
    }
}
