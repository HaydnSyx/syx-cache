package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class HMgetCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "HMGET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getValues(args);
        return Reply.array(cache.hmget(key, hkeys));
    }
}
