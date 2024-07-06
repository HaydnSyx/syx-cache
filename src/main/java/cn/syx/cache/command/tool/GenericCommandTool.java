package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.utils.PatternUtil;
import io.github.haydnsyx.toolbox.base.StringTool;

import java.util.Objects;

public class GenericCommandTool {

    public static boolean isExpire(SyxCacheDb db, String key) {
        if (!db.getExpireMap().containsKey(key)) {
            return false;
        }
        long expire = db.getExpireMap().get(key);
        boolean re = System.currentTimeMillis() >= expire;
        if (re) {
            del(db, key);
        }
        return re;
    }

    public static long expiretime(SyxCacheDb db, String key) {
        if (!db.getMap().containsKey(key)) {
            return -2L;
        }

        if (!db.getExpireMap().containsKey(key)) {
            return -1L;
        }

        return db.getExpireMap().get(key) / 1000;
    }

    public static int expireat(SyxCacheDb db, String key, long time, String type) {
        if (!db.getMap().containsKey(key)) {
            return 0;
        }

        if (StringTool.isBlank(type) || Objects.equals("NX", type)
                || Objects.equals("XX", type)) {
            db.getExpireMap().put(key, time);
            return 1;
        }

        long expire = db.getExpireMap().get(key);
        long now = System.currentTimeMillis();
        if (Objects.equals("XX", type) && now >= expire) {
            db.getExpireMap().put(key, time);
            return 1;
        }

        if (Objects.equals("GT", type) && time > expire) {
            db.getExpireMap().put(key, time);
            return 1;
        }

        if (Objects.equals("LT", type) && time < expire) {
            db.getExpireMap().put(key, time);
            return 1;
        }

        return 0;
    }

    public static int ttl(SyxCacheDb db, String key) {
        if (!db.getMap().containsKey(key)) {
            return -2;
        }

        if (!db.getExpireMap().containsKey(key)) {
            return -1;
        }

        long expire = db.getExpireMap().get(key);
        long now = System.currentTimeMillis();
        if (now >= expire) {
            del(db, key);
            return -2;
        }

        return (int) ((expire - now) / 1000);
    }

    public static int exists(SyxCacheDb db, String... keys) {
        int count = 0;
        if (Objects.isNull(keys)) {
            return count;
        }

        for (String key : keys) {
            if (db.getMap().containsKey(key)) {
                count++;
            }
        }
        return count;
    }

    public static int del(SyxCacheDb db, String... keys) {
        int count = 0;
        if (Objects.isNull(keys)) {
            return count;
        }

        for (String key : keys) {
            if (db.getMap().remove(key) != null) {
                db.getExpireMap().remove(key);
                count++;
            }
        }
        return count;
    }

    public static String[] keys(SyxCacheDb db, String pattern) {
        String[] keys = db.getMap().keySet().toArray(String[]::new);
        if (keys.length == 0) {
            return null;
        }

        return PatternUtil.filterByPattern(keys, pattern);
    }
}
