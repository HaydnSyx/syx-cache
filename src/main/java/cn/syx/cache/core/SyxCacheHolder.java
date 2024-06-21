package cn.syx.cache.core;

import cn.syx.cache.command.utils.PatternUtil;
import cn.syx.cache.domain.CacheEntity;

import java.util.*;

public class SyxCacheHolder {

    private final Map<String, CacheEntity<?>> map = new HashMap<>();

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

    public String[] keys(String pattern) {
        String[] keys = map.keySet().toArray(String[]::new);
        if (keys.length == 0) {
            return null;
        }

        return PatternUtil.filterByPattern(keys, pattern);
    }

    // ======================= String ============================

    public void set(String key, String value) {
        map.put(key, CacheEntity.create(value));
    }

    public String get(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        return (String) map.get(key).getData();
    }

    public int strlen(String key) {
        int len = 0;
        String value = get(key);
        if (Objects.isNull(value)) {
            return len;
        }
        return value.length();
    }

    public String[] mget(String... keys) {
        if (Objects.isNull(keys)) {
            return new String[0];
        }

        return Arrays.stream(keys).map(this::get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if (Objects.isNull(keys) || Objects.isNull(values) || keys.length != values.length) {
            return;
        }

        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], CacheEntity.create(values[i]));
        }
    }

    public int incr(String key) {
        String value = get(key);
        if (Objects.isNull(value)) {
            map.put(key, CacheEntity.create("1"));
            return 1;
        } else {
            int i = Integer.parseInt(value);
            map.put(key, CacheEntity.create(String.valueOf(++i)));
            return i;
        }
    }

    public int decr(String key) {
        String value = get(key);
        if (Objects.isNull(value)) {
            map.put(key, CacheEntity.create("-1"));
            return -1;
        } else {
            int i = Integer.parseInt(value);
            map.put(key, CacheEntity.create(String.valueOf(--i)));
            return i;
        }
    }

    // ======================= List ============================

    public int lpush(String key, String... values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedList<String>());
            map.put(key, entity);
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        Arrays.stream(values).forEach(link::addFirst);
        return values.length;
    }

    public String[] lpop(String key, int count) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return null;
        }

        int len = Math.min(count, link.size());
        String[] res = new String[len];
        for (int i = 0; i < len; i++) {
            res[i] = link.removeFirst();
        }
        return res;
    }

    public int rpush(String key, String... values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedList<String>());
            map.put(key, entity);
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        Arrays.stream(values).forEach(link::addLast);
        return values.length;
    }

    public String[] rpop(String key, int count) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return null;
        }

        int len = Math.min(count, link.size());
        String[] res = new String[len];
        for (int i = 0; i < len; i++) {
            res[i] = link.removeLast();
        }
        return res;
    }

    public int llen(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return 0;
        }
        return link.size();
    }

    public String lindex(String key, int index) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return null;
        }
        if (index < 0 || index >= link.size()) {
            return null;
        }
        return link.get(index);

    }

    public String[] lrange(String key, int start, int end) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return null;
        }
        int size = link.size();
        if (start < 0 || end < 0 || start >= size || start > end) {
            return null;
        }

        if (end >= size) {
            end = size - 1;
        }
        int len = Math.min(end - start + 1, size);
        String[] res = new String[len];
        for (int i = 0; i < len; i++) {
            res[i] = link.get(start + i);
        }
        return res;
    }
}
