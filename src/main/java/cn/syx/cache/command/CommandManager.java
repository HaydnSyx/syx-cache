package cn.syx.cache.command;

import cn.syx.cache.command.base.*;
import cn.syx.cache.command.list.*;
import cn.syx.cache.command.string.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private static final Map<String, Command<?>> COMMANDS = new LinkedHashMap<>();

    static {
        // ------------- Base -------------
        register(new InfoCommand());
        register(new CommandCommand());
        register(new PingCommand());
        register(new ExistsCommand());
        register(new DelCommand());
        register(new KeysCommand());

        // ------------- String -------------
        register(new SetCommand());
        register(new GetCommand());
        register(new MSetCommand());
        register(new MGetCommand());
        register(new IncrCommand());
        register(new DecrCommand());
        register(new StrlenCommand());

        // ------------- List -------------
        register(new LPushCommand());
        register(new RPushCommand());
        register(new LPopCommand());
        register(new RPopCommand());
        register(new LLenCommand());
        register(new LIndexCommand());
        register(new LRangeCommand());
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
