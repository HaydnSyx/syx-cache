package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class LPushCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "LPUSH";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] values = getValues(args);
        return Reply.integer(cache.lpush(key, values));
    }
}
