package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class ExpiretimeCommand extends AbstractGenericCommand<Long> {

    @Override
    public String name() {
        return "EXPIRETIME";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() != 1) {
            return "ERR wrong number of arguments for 'expiretime' command";
        }
        return null;
    }

    @Override
    public Reply<Long> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        long time = GenericCommandTool.expiretime(db, key);
        return Reply.number(time);
    }
}
