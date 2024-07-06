package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class PingCommand extends AbstractGenericCommand<String> {

    @Override
    public String name() {
        return "PING";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 1) {
            return "ERR wrong number of arguments for 'ping' command";
        }
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        if (req.getParamNum() > 0) {
            return Reply.simpleString(req.getKey());
        }
        return Reply.simpleString("PONG");
    }
}
