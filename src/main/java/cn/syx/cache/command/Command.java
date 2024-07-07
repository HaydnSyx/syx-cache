package cn.syx.cache.command;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import io.netty.channel.ChannelHandlerContext;

public interface Command<T> {

    String name();

    default boolean checkMemory() {
        return false;
    }

    Reply<T> exec(ChannelHandlerContext ctx, SyxCacheDb cache, CacheCommandRequest req);
}
