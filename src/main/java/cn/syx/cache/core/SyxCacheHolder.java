package cn.syx.cache.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SyxCacheHolder {

    private final Map<String, String> map = new HashMap<>();

    public void set(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public int exists(String... keys) {
        int count = 0;
        if (Objects.isNull(keys)) {
            return count;
        }

        for (String key : keys) {
            if (map.containsKey(key)) {
                count++;
            }
        }
        return count;
    }

    public int del(String... keys) {
        int count = 0;
        if (Objects.isNull(keys)) {
            return count;
        }

        for (String key : keys) {
            if (map.remove(key) != null) {
                count++;
            }
        }
        return count;
    }

    public String[] mget(String... keys) {
        if (Objects.isNull(keys)) {
            return new String[0];
        }

        return Arrays.stream(keys).map(map::get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if (Objects.isNull(keys) || Objects.isNull(values) || keys.length != values.length) {
            return;
        }

        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
    }

    public int incr(String key) {
        String value = map.get(key);
        if (Objects.isNull(value)) {
            map.put(key, "1");
            return 1;
        } else {
            int i = Integer.parseInt(value);
            map.put(key, String.valueOf(++i));
            return i;
        }
    }

    public int decr(String key) {
        String value = map.get(key);
        if (Objects.isNull(value)) {
            map.put(key, "-1");
            return -1;
        } else {
            int i = Integer.parseInt(value);
            map.put(key, String.valueOf(--i));
            return i;
        }
    }
}
