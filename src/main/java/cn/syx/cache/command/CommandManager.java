package cn.syx.cache.command;

import cn.syx.cache.command.generic.*;
import cn.syx.cache.command.hash.*;
import cn.syx.cache.command.list.*;
import cn.syx.cache.command.set.*;
import cn.syx.cache.command.string.*;
import cn.syx.cache.command.zset.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private static final Map<String, Command<?>> COMMANDS = new LinkedHashMap<>();

    static {
        // ------------- base -------------
        register(new InfoCommand());
        register(new ExpireCommand());
        register(new ExpireatCommand());
        register(new ExpiretimeCommand());
        register(new SelectCommand());
        register(new CommandCommand());
        register(new PingCommand());
        register(new ExistsCommand());
        register(new DelCommand());
        register(new KeysCommand());
        register(new TtlCommand());

        // ------------- string -------------
        register(new AppendCommand());
        register(new DecrCommand());
        register(new DecrbyCommand());
        register(new GetCommand());
        register(new GetdelCommand());
        register(new GetsetCommand());
        register(new GetrangeCommand());
        register(new IncrCommand());
        register(new IncrbyCommand());
        register(new MGetCommand());
        register(new MSetCommand());
        register(new MSetnxCommand());
        register(new SetCommand());
        register(new SetnxCommand());
        register(new SetrangeCommand());
        register(new StrlenCommand());

        // ------------- list -------------
        register(new LPushCommand());
        register(new RPushCommand());
        register(new LPopCommand());
        register(new RPopCommand());
        register(new LLenCommand());
        register(new LIndexCommand());
        register(new LRangeCommand());

        // ------------- set -------------
        register(new SAddCommand());
        register(new SCardCommand());
        register(new SMembersCommand());
        register(new SIsmemberCommand());
        register(new SRemCommand());
        register(new SPopCommand());
        // ------------- hash -------------
        register(new HSetCommand());
        register(new HGetCommand());
        register(new HGetallCommand());
        register(new HDelCommand());
        register(new HLenCommand());
        register(new HExistsCommand());
        register(new HMgetCommand());
        // ------------- zset -------------
        register(new ZAddCommand());
        register(new ZCardCommand());
        register(new ZScoreCommand());
        register(new ZCountCommand());
        register(new ZRankCommand());
        register(new ZRemCommand());
    }

    public static void register(Command<?> command) {
        COMMANDS.put(command.name(), command);
    }

    public static Command<?> get(String name) {
        return COMMANDS.get(name);
    }

    public static String[] names() {
        return COMMANDS.keySet().toArray(new String[0]);
    }
}
