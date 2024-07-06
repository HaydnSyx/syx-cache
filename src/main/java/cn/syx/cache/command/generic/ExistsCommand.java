package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class ExistsCommand extends AbstractGenericCommand<Integer> {

    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() < 1) {
            return "ERR wrong number of arguments for 'exists' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String[] keys = req.getParams();
        return Reply.number(GenericCommandTool.exists(db, keys));
    }
}
