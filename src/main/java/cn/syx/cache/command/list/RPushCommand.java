package cn.syx.cache.command.list;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class RPushCommand implements Command<Integer> {

    @Override
    public String name() {
        return "RPUSH";
    }

    @Override
    public Reply<Integer> exec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] values = getValues(args);
        return Reply.integer(cache.rpush(key, values));
    }
}
