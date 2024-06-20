package cn.syx.cache.command.base;

import cn.syx.cache.command.Command;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class CommandCommand implements Command<String[]> {

    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    public Reply<String[]> exec(SyxCacheHolder cache, String[] args) {
        return Reply.array(CommandManager.names());
    }
}
