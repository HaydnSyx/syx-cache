package cn.syx.cache.command.list;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class LLenCommand implements Command<Integer> {

    @Override
    public String name() {
        return "LLEN";
    }

    @Override
    public Reply<Integer> exec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.llen(key));
    }
}
