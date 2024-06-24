package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class HExistsCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "HEXISTS";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        return Reply.integer(cache.hexists(key, value));
    }
}
