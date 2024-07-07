package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.ListCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class RPushCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "RPUSH";
    }

    @Override
    public boolean checkMemory() {
        return true;
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] values = req.getValuesSkipKey();
        return Reply.number(ListCommandTool.rpush(db, key, values));
    }
}
