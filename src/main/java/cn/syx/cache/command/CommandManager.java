package cn.syx.cache.command;

import cn.syx.cache.command.base.*;
import cn.syx.cache.command.hash.*;
import cn.syx.cache.command.list.*;
import cn.syx.cache.command.set.*;
import cn.syx.cache.command.string.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private static final Map<String, Command<?>> COMMANDS = new LinkedHashMap<>();

    static {
        // ------------- base -------------
        register(new InfoCommand());
        register(new CommandCommand());
        register(new PingCommand());
        register(new ExistsCommand());
        register(new DelCommand());
        register(new KeysCommand());

        // ------------- string -------------
        register(new SetCommand());
        register(new GetCommand());
        register(new MSetCommand());
        register(new MGetCommand());
        register(new IncrCommand());
        register(new DecrCommand());
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
