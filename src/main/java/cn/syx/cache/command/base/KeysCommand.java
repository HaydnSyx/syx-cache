package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class KeysCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "KEYS";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheHolder cache, String[] args) {
        String pattern = getKey(args);
        return Reply.array(cache.keys(pattern));
    }
}
