package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

import java.math.BigDecimal;

public class ZCountCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZCOUNT";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        BigDecimal start = new BigDecimal(getValue(args, 6));
        BigDecimal end = new BigDecimal(getValue(args, 8));
        return Reply.integer(cache.zcount(key, start, end));
    }
}
