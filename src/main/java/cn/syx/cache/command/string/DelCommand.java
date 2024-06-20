package cn.syx.cache.command.string;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class DelCommand implements Command<Integer> {

    @Override
    public String name() {
        return "DEL";
    }

    @Override
    public Reply<Integer> exec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        int count = cache.del(keys);
        return Reply.integer(count);
    }
}
