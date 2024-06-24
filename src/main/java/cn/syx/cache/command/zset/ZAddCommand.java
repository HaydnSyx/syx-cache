package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
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
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        List<Pair<String, String>> pairs = getHashPairs(args);
        BigDecimal[] zscores = pairs.stream().map(Pair::getLeft).map(BigDecimal::new).toArray(BigDecimal[]::new);
        String[] zvalues = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        Integer count = cache.zadd(key, zscores, zvalues);
        return Reply.integer(count);
    }
}
