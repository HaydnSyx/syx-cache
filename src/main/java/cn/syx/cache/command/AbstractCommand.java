package cn.syx.cache.command;

import cn.syx.cache.core.SyxCacheMonitor;
import cn.syx.cache.core.SyxCacheTimeWheel;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.ExpulsionResult;
import cn.syx.cache.domain.Reply;
import cn.syx.cache.utils.SingletonUtil;
import io.github.haydnsyx.toolbox.base.StringTool;
import io.netty.channel.ChannelHandlerContext;
import org.apache.lucene.util.RamUsageEstimator;

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

        // 判断该命令是否需要检查空间足够
        if (checkMemory()) {
            SyxCacheMonitor monitor = SingletonUtil.getInstance(SyxCacheMonitor.class);
            // 判断是否需要检查空间
            if (monitor.isCheckFlag()) {
                boolean overLimit = monitor.isOverLimit();
                // 判断是否超过空间大小
                if (overLimit) {
                    // 执行该db下的驱逐策略
                    ExpulsionResult result = db.expulsion(RamUsageEstimator.sizeOf(req.getParams()));
                    if (!result.isSuccess()) {
                        return Reply.error(result.getErrorMsg());
                    }
                }
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
