package cn.syx.cache.command.string;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

import java.util.Objects;

public class StrlenCommand implements Command<Integer> {

    @Override
    public String name() {
        return "STRLEN";
    }

    @Override
    public Reply<Integer> exec(SyxCacheHolder cache, String[] args) {
        return Reply.integer(cache.strlen(getKey(args)));
    }
}
