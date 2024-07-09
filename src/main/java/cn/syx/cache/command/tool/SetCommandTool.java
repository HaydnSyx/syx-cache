package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SetCommandTool {

    public static Integer sadd(SyxCacheDb db, String key, String[] values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashSet<>());
            db.put(key, entity);
        }

        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        long count = Arrays.stream(values).filter(e -> !set.contains(e)).peek(set::add).count();
        return (int) count;
    }

    public static String[] smembers(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return new String[0];
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return new String[0];
        }
        return set.toArray(String[]::new);
    }

    public static Integer scard(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return 0;
        }
        return set.size();
    }

    public static Integer sismember(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return 0;
        }
        return set.contains(value) ? 1 : 0;
    }

    public static Integer srem(SyxCacheDb db, String keys, String[] setKeys) {
        CacheEntity<?> entity = db.get(keys);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return 0;
        }

        long count = Arrays.stream(setKeys).filter(set::remove).count();
        return (int) count;
    }

    public static String[] spop(SyxCacheDb db, String key, int count) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return null;
        }

        int len = Math.min(count, set.size());
        String[] res = new String[len];
        for (int i = 0; i < len; i++) {
            int index = ThreadLocalRandom.current().nextInt(set.size());
            res[i] = set.toArray(String[]::new)[index];
            set.remove(res[i]);
        }
        return res;
    }
}
