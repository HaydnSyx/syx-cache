package cn.syx.cache.command;

import cn.syx.cache.core.SyxCacheMonitor;
import cn.syx.cache.core.SyxCacheTimeWheel;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import cn.syx.cache.utils.SingletonUtil;
import io.github.haydnsyx.toolbox.base.StringTool;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

public abstract class AbstractCommand<T> implements Command<T> {

    @Override
    public Reply<T> exec(ChannelHandlerContext ctx, SyxCacheDb db, CacheCommandRequest req) {
        String errMsg = checkArgs(req);
        if (StringTool.isNotBlank(errMsg)) {
            return Reply.error(errMsg);
        }

        // 检查是否过期
        Reply<T> reply = beforeExec(ctx, db, req);
        if (Objects.nonNull(reply)) {
            return reply;
        }

        // 执行命令
        reply = doExec(db, req);

        // 执行后置处理
        afterExec(ctx, db, req, reply);

        return reply;
    }

    protected abstract String checkArgs(CacheCommandRequest req);

    protected Reply<T> beforeExec(ChannelHandlerContext ctx, SyxCacheDb db, CacheCommandRequest req) {
        GenericCommandTool.isExpire(db, req.getKey());

        if (req.isCheckMemory()) {
            SyxCacheMonitor monitor = SingletonUtil.getInstance(SyxCacheMonitor.class);
            boolean overLimit = monitor.isOverLimit();
            if (overLimit) {
                // todo 判断淘汰机制
                return Reply.error("over memory size");
            }
        }

        return null;
    }

    protected abstract Reply<T> doExec(SyxCacheDb db, CacheCommandRequest req);

    protected void afterExec(ChannelHandlerContext ctx, SyxCacheDb db, CacheCommandRequest req, Reply<T> reply) {
        if (reply.isExpire()) {
            SingletonUtil.getInstance(SyxCacheTimeWheel.class).add(db.getNum(), req.getKey(), reply.getExpireTime());
        }
    }
}
