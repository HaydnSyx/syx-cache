package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

import java.util.Objects;

public class StrlenCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "STRLEN";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        return Reply.integer(cache.strlen(getKey(args)));
    }
}
