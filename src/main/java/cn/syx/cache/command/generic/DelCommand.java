package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class DelCommand extends AbstractGenericCommand<Integer> {

    @Override
    public String name() {
        return "DEL";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() < 1) {
            return "ERR wrong number of arguments for 'del' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String[] keys = req.getParams();
        int count = GenericCommandTool.del(db, keys);
        return Reply.number(count);
    }
}
