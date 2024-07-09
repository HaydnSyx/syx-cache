package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class HashCommandTool {

    public static Integer hset(SyxCacheDb db, String key, String[] hkeys, String[] hvalues) {
        if (Objects.isNull(hkeys) || Objects.isNull(hvalues)
                || hkeys.length == 0
                || hvalues.length == 0
                || hkeys.length != hvalues.length) {
            return 0;
        }

        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashMap<String, String>());
            db.put(key, entity);
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        int count = 0;
        for (int i = 0; i < hkeys.length; i++) {
            if (map.put(hkeys[i], hvalues[i]) == null) {
                count++;
            }
        }
        return count;
    }

    public static String hget(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        return map.get(value);
    }

    public static String[] hgetall(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return new String[0];
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return new String[0];
        }

        return map.entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                .toArray(String[]::new);
    }

    public static Integer hdel(SyxCacheDb db, String key, String[] hkeys) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        long count = Arrays.stream(hkeys).filter(e-> Objects.nonNull(map.remove(e))).count();
        return (int) count;
    }

    public static Integer hexists(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.containsKey(value) ? 1 : 0;
    }

    public static String[] hmget(SyxCacheDb db, String key, String[] hkeys) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return new String[hkeys.length];
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return new String[hkeys.length];
        }

        return Arrays.stream(hkeys).map(map::get).toArray(String[]::new);
    }

    public static Integer hlen(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.size();
    }
}
