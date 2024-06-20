package cn.syx.cache.command.string;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class GetCommand implements Command<String> {

    @Override
    public String name() {
        return "GET";
    }

    @Override
    public Reply<String> exec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        return Reply.complexString(cache.get(key));
    }
}
