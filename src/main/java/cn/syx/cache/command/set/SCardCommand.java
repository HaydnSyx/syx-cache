package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class SCardCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "SCARD";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.scard(key));
    }
}
