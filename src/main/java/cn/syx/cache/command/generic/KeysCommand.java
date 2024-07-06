package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class KeysCommand extends AbstractGenericCommand<String[]> {

    @Override
    public String name() {
        return "KEYS";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() != 1) {
            return "ERR wrong number of arguments for 'keys' command";
        }
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String pattern = req.getKey();
        return Reply.array(GenericCommandTool.keys(db, pattern));
    }
}
