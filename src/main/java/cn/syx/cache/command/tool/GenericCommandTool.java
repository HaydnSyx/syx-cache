package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.utils.PatternUtil;
import io.github.haydnsyx.toolbox.base.StringTool;

import java.util.Objects;

public class GenericCommandTool {

    public static boolean isExpire(SyxCacheDb db, String key) {
        Long expire = db.getExpireTime(key);
        if (Objects.isNull(expire)) {
            return false;
        }
        
        boolean re = System.currentTimeMillis() >= expire;
        if (re) {
            del(db, key);
        }
        return re;
    }

    public static long expiretime(SyxCacheDb db, String key) {
        if (!db.containsKey(key)) {
            return -2L;
        }

        Long expire = db.getExpireTime(key);
        if (Objects.isNull(expire)) {
            return -1L;
        }

        return expire / 1000;
    }

    public static int expireat(SyxCacheDb db, String key, long time, String type) {
        if (!db.containsKey(key)) {
            return 0;
        }

        if (StringTool.isBlank(type) || Objects.equals("NX", type)
                || Objects.equals("XX", type)) {
            db.putExpireTime(key, time);
            return 1;
        }

        long expire = db.getExpireTime(key);
        long now = System.currentTimeMillis();
        if (Objects.equals("XX", type) && now >= expire) {
            db.putExpireTime(key, time);
            return 1;
        }

        if (Objects.equals("GT", type) && time > expire) {
            db.putExpireTime(key, time);
            return 1;
        }

        if (Objects.equals("LT", type) && time < expire) {
            db.putExpireTime(key, time);
            return 1;
        }

        return 0;
    }

    public static int ttl(SyxCacheDb db, String key) {
        if (!db.containsKey(key)) {
            return -2;
        }

        Long expire = db.getExpireTime(key);
        if (Objects.isNull(expire)) {
            return -1;
        }

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
            if (db.containsKey(key)) {
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
            if (db.remove(key) != null) {
                db.removeExpire(key);
                count++;
            }
        }
        return count;
    }

    public static String[] keys(SyxCacheDb db, String pattern) {
        String[] keys = db.keySet().toArray(String[]::new);
        if (keys.length == 0) {
            return null;
        }

        return PatternUtil.filterByPattern(keys, pattern);
    }
}
