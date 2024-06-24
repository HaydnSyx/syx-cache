package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class HSetCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "HSET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        List<Pair<String, String>> pairs = getHashPairs(args);
        String[] hkeys = pairs.stream().map(Pair::getLeft).toArray(String[]::new);
        String[] hvalues = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        Integer count = cache.hset(key, hkeys, hvalues);
        return Reply.integer(count);
    }
}
