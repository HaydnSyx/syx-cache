package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class MGetCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "MGET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        String[] values = cache.mget(keys);
        return Reply.array(values);
    }
}
