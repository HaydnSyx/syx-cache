package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.HashCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class HDelCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "HDEL";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] hkeys = req.getValuesSkipKey();
        return Reply.number(HashCommandTool.hdel(db, key, hkeys));
    }
}
