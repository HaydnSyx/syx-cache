package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class LRangeCommand extends AbstractCommand {

    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] values = getValues(args);
        int start = Integer.parseInt(values[0]);
        int end = Integer.parseInt(values[1]);
        return Reply.array(cache.lrange(key, start, end));
    }
}
