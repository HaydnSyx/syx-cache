package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

import java.math.BigDecimal;

public class ZCountCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZCOUNT";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        BigDecimal start = new BigDecimal(req.getTarget(1));
        BigDecimal end = new BigDecimal(req.getTarget(2));
        return Reply.number(ZSetCommandTool.zcount(db, key, start, end));
    }
}
