package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class CommandCommand extends AbstractGenericCommand<String[]> {

    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() >= 1) {
            return String.format("ERR Unknown subcommand or wrong number of arguments for '%s'. Try COMMAND HELP.", req.getKey());
        }
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        return Reply.array(CommandManager.names());
    }
}
