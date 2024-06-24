package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class HDelCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "HDEL";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getValues(args);
        return Reply.integer(cache.hdel(key, hkeys));
    }
}
