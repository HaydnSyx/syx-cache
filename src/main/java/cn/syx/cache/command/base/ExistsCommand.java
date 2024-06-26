package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class ExistsCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    protected String checkArgs(String[] args) {
        if (args.length < 4) {
            return "ERR wrong number of arguments for 'exists' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        return Reply.integer(cache.exists(keys));
    }
}
