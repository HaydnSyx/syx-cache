package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class GetCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "GET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.complexString(cache.get(key));
    }
}
