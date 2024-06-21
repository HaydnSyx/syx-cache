package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class IncrCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "INCR";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        try {
            return Reply.integer(cache.incr(key));
        } catch (Exception e) {
            return Reply.integer(null, "[" + key + "] get value is not an integer or out of range");
        }
    }
}
