package cn.syx.cache.command;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractGenericCommand<T> extends AbstractCommand<T> {

    @Override
    protected Reply<T> beforeExec(ChannelHandlerContext ctx, SyxCacheDb cache, CacheCommandRequest req) {
        return null;
    }
}
