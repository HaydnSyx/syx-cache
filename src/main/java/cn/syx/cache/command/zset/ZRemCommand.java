package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class ZRemCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZREM";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] setKeys = req.getValuesSkipKey();
        return Reply.number(ZSetCommandTool.zrem(db, key, setKeys));
    }
}
