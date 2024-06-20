package cn.syx.cache.command.base;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class ExistsCommand implements Command<Integer> {

    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    public Reply<Integer> exec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        return Reply.integer(cache.exists(keys));
    }
}
