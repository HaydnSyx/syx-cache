package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

import java.util.Locale;
import java.util.Objects;

public class ConfigGetCommand extends AbstractGenericCommand<String[]> {

    @Override
    public String name() {
        return "CONFIG GET";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        /*if (req.getParamNum() >= 1) {
            return String.format("ERR Unknown subcommand or wrong number of arguments for '%s'. Try COMMAND HELP.", req.getKey());
        }*/
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        if (Objects.equals(key.toLowerCase(Locale.ROOT), "appendonly")) {
            return Reply.array(new String[]{key, "no"});
        }

        if (Objects.equals(key.toLowerCase(Locale.ROOT), "save")) {
            return Reply.array(new String[]{key, "3600 1 300 100 60 10000"});
        }

        return Reply.array(null);
    }
}
