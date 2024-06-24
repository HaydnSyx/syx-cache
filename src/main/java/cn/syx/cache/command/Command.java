package cn.syx.cache.command;

import cn.syx.cache.domain.Reply;
import cn.syx.cache.core.SyxCacheHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public interface Command<T> {

    String CRLF = "\r\n";

    String OK = "OK";

    default String getKey(String[] args) {
        return args.length < 5 ? null : args[4];
    }

    default String getValue(String[] args) {
        return args.length < 7 ? null : args[6];
    }

    default String getValue(String[] args, int index) {
        return args.length < index + 1 ? null : args[index];
    }

    default String[] getKeys(String[] args) {
        int len = (args.length - 3) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i * 2];
        }
        return keys;
    }

    default String[] getValues(String[] args) {
        int len = (args.length - 5) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 2];
        }
        return keys;
    }

    default List<Pair<String, String>> getPairs(String[] args) {
        int len = (args.length - 3) / 4;
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            pairs.add(Pair.of(args[4 + i * 4], args[6 + i * 4]));
        }
        return pairs;
    }

    default List<Pair<String, String>> getHashPairs(String[] args) {
        int len = (args.length - 5) / 4;
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            pairs.add(Pair.of(args[6 + i * 4], args[8 + i * 4]));
        }
        return pairs;
    }

    String name();

    Reply<T> exec(SyxCacheHolder cache, String[] args);
}
