package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class SAddCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "SADD";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] values = getValues(args);
        return Reply.integer(cache.sadd(key, values));
    }
}
