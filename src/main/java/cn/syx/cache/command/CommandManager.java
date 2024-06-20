package cn.syx.cache.command;

import cn.syx.cache.command.base.CommandCommand;
import cn.syx.cache.command.base.ExistsCommand;
import cn.syx.cache.command.base.InfoCommand;
import cn.syx.cache.command.base.PingCommand;
import cn.syx.cache.command.list.*;
import cn.syx.cache.command.string.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {

    private static Map<String, Command<?>> COMMANDS = new LinkedHashMap<>();

    static {
        // ------------- Base -------------
        register(new InfoCommand());
        register(new CommandCommand());
        register(new PingCommand());
        register(new ExistsCommand());

        // ------------- String -------------
        register(new SetCommand());
        register(new GetCommand());
        register(new DelCommand());
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
