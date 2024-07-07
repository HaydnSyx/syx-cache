package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.HashCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class HSetCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "HSET";
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
        String[] hkeys = pairs.stream().map(Pair::getLeft).toArray(String[]::new);
        String[] hvalues = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        Integer count = HashCommandTool.hset(db, key, hkeys, hvalues);
        return Reply.number(count);
    }
}
