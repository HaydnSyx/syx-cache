package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import io.netty.channel.ChannelHandlerContext;

import static cn.syx.cache.core.SyxCacheConstants.DB_KEY;

public class SelectCommand extends AbstractGenericCommand<String> {

    @Override
    public String name() {
        return "SELECT";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() != 1) {
            return "ERR wrong number of arguments for 'select' command";
        }
        try {
            int number = Integer.parseInt(req.getKey());
            if (number < 0 || number > 15) {
                return "ERR DB index is out of range";
            }
        } catch (Exception e) {
            return "ERR value is not an integer or out of range";
        }
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        return Reply.simpleString("OK");
    }

    @Override
    protected void afterExec(ChannelHandlerContext ctx, SyxCacheDb db, CacheCommandRequest req, Reply<String> reply) {
        // 重新设置客户端访问的数据库
        if (SyxCacheConstants.ReplyType.ERROR != reply.getType()) {
            ctx.channel().attr(DB_KEY).set(Integer.parseInt(req.getKey()));
        }
    }
}
