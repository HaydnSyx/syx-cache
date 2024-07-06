package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.SetCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class SRemCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "SREM";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] setKeys = req.getValuesSkipKey();
        return Reply.number(SetCommandTool.srem(db, key, setKeys));
    }
}
