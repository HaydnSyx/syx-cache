package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.HashCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class HGetallCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "HGETALL";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        return Reply.array(HashCommandTool.hgetall(db, key));
    }
}
