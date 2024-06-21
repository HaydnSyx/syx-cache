package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class LLenCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "LLEN";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.llen(key));
    }
}
