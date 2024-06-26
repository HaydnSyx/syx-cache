package cn.syx.cache.command.base;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class CommandCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    protected String checkArgs(String[] args) {
        if (args.length < 3 || args.length >= 5) {
            return String.format("ERR Unknown subcommand or wrong number of arguments for '%s'. Try COMMAND HELP.", args[4]);
        }
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheHolder cache, String[] args) {
        return Reply.array(CommandManager.names());
    }
}
