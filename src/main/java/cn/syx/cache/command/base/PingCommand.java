package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class PingCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "PING";
    }

    @Override
    protected String checkArgs(String[] args) {
        if (args.length < 3 || args.length > 5) {
            return "ERR wrong number of arguments for 'ping' command";
        }
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        if (args.length >= 5) {
            return Reply.simpleString(getKey(args));
        }
        return Reply.simpleString("PONG");
    }
}
