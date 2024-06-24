package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class SMembersCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "SMEMBERS";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.array(cache.smembers(key));
    }
}
