package cn.syx.cache.command.base;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class PingCommand implements Command<String> {

    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<String> exec(SyxCacheHolder cache, String[] args) {
        if (args.length >= 5) {
            return Reply.simpleString(args[4]);
        }
        return Reply.simpleString("PONG");
    }
}
