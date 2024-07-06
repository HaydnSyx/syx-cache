package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;
import io.github.haydnsyx.toolbox.base.NumberTool;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class StringCommandTool {

    public static int append(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            db.getMap().put(key, CacheEntity.create(value));
            return value.length();
        } else {
            String data = entity.getData() + value;
            db.getMap().put(key, CacheEntity.create(data));
            return data.length();
        }
    }

    public static void set(SyxCacheDb db, String key, String value) {
        db.getMap().put(key, CacheEntity.create(value));
    }

    public static Integer setrange(SyxCacheDb db, String key, int start, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        String data = (String) db.getMap().get(key).getData();
        if (start < 0) {
            start = data.length() + start + 1;
        }
        String result = data.substring(0, start) + value;
        db.getMap().put(key, CacheEntity.create(result));
        return result.length();
    }

    public static int setnx(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            db.getMap().put(key, CacheEntity.create(value));
            return NumberTool.INT_ONE;
        }
        return NumberTool.INT_ZERO;
    }

    public static String get(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        return (String) db.getMap().get(key).getData();
    }

    public static String getdel(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        if (db.getMap().get(key).getData() instanceof String s) {
            return s;
        }
        return null;
    }

    public static String getset(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        String data = (String) db.getMap().get(key).getData();
        db.getMap().put(key, CacheEntity.create(value));
        return data;
    }

    public static String getrange(SyxCacheDb db, String key, int start, int end) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }
        String data = (String) db.getMap().get(key).getData();
        if (start < 0) {
            start = data.length() + start + 1;
        }
        if (end < 0) {
            end = data.length() + end + 1;
        }
        return data.substring(start, end);
    }

    public static int strlen(SyxCacheDb db, String key) {
        int len = 0;
        String value = get(db, key);
        if (Objects.isNull(value)) {
            return len;
        }
        return value.length();
    }

    public static String[] mget(SyxCacheDb db, String... keys) {
        if (Objects.isNull(keys)) {
            return new String[0];
        }

        return Arrays.stream(keys).map(e -> get(db, e)).toArray(String[]::new);
    }

    public static void mset(SyxCacheDb db, String[] keys, String[] values) {
        if (Objects.isNull(keys) || Objects.isNull(values) || keys.length != values.length) {
            return;
        }

        for (int i = 0; i < keys.length; i++) {
            db.getMap().put(keys[i], CacheEntity.create(values[i]));
        }
    }

    public static int msetnx(SyxCacheDb db, String[] keys, String[] values) {
        if (Objects.isNull(keys) || Objects.isNull(values) || keys.length != values.length) {
            return 0;
        }
        int result = 1;
        for (int i = 0; i < keys.length; i++) {
            result = result & setnx(db, keys[i], values[i]);
        }
        return result;
    }

    public static int incr(SyxCacheDb db, String key) {
        return incrBy(db, key, NumberTool.INT_ONE);
    }

    public static int incrBy(SyxCacheDb db, String key, int num) {
        String value = get(db, key);
        int data = Optional.ofNullable(value).map(Integer::parseInt).orElse(NumberTool.INT_ZERO);
        int result = data + num;
        db.getMap().put(key, CacheEntity.create(result));
        return result;
    }

    public static int decr(SyxCacheDb db, String key) {
        return decrBy(db, key, NumberTool.INT_ONE);
    }

    public static int decrBy(SyxCacheDb db, String key, int num) {
        String value = get(db, key);
        int data = Optional.ofNullable(value).map(Integer::parseInt).orElse(NumberTool.INT_ZERO);
        int result = data - num;
        db.getMap().put(key, CacheEntity.create(result));
        return result;
    }
}
