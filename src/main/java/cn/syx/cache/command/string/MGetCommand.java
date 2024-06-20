package cn.syx.cache.command.string;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class MGetCommand implements Command<String[]> {

    @Override
    public String name() {
        return "MGET";
    }

    @Override
    public Reply<String[]> exec(SyxCacheHolder cache, String[] args) {
        String[] keys = getKeys(args);
        String[] values = cache.mget(keys);
        return Reply.array(values);
    }
}
