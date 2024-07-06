package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class ZCardCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZCARD";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        return Reply.number(ZSetCommandTool.zcard(db, key));
    }
}
