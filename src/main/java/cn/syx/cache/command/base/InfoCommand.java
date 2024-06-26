package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.domain.Reply;
import cn.syx.cache.core.SyxCacheHolder;

public class InfoCommand extends AbstractCommand<String> {

    private static final String INFO = "SyxCache 1.0.0, create by syx" + CRLF
            + "fsdfsdfsd" + CRLF;

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        return Reply.complexString(INFO);
    }
}
