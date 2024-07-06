package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class TtlCommand extends AbstractGenericCommand<Integer> {

    @Override
    public String name() {
        return "TTL";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 1) {
            return "ERR wrong number of arguments for 'ttl' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        return Reply.number(GenericCommandTool.ttl(db, req.getKey()));
    }
}
