package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class DelCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "DEL";
    }

    @Override
    protected String checkArgs(String[] args) {
        if (args.length < 4) {
            return "ERR wrong number of arguments for 'del' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        int count = cache.del(keys);
        return Reply.integer(count);
    }
}
