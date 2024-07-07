package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;

public class ZAddCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZADD";
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
        List<Pair<String, String>> pairs = req.getHashKvList();
        BigDecimal[] zscores = pairs.stream().map(Pair::getLeft).map(BigDecimal::new).toArray(BigDecimal[]::new);
        String[] zvalues = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        Integer count = ZSetCommandTool.zadd(db, key, zscores, zvalues);
        return Reply.number(count);
    }
}
