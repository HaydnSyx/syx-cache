package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class ListCommandTool {

    public static int lpush(SyxCacheDb db, String key, String... values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedList<String>());
            db.put(key, entity);
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        Arrays.stream(values).forEach(link::addFirst);
        return values.length;
    }

    public static String[] lpop(SyxCacheDb db, String key, int count) {
        CacheEntity<?> entity = db.get(key);
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

    public static int rpush(SyxCacheDb db, String key, String... values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedList<String>());
            db.put(key, entity);
        }

        LinkedList<String> link = (LinkedList<String>) entity.getData();
        Arrays.stream(values).forEach(link::addLast);
        return values.length;
    }

    public static String[] rpop(SyxCacheDb db, String key, int count) {
        CacheEntity<?> entity = db.get(key);
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

    public static int llen(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedList<String> link = (LinkedList<String>) entity.getData();
        if (Objects.isNull(link)) {
            return 0;
        }
        return link.size();
    }

    public static String lindex(SyxCacheDb db, String key, int index) {
        CacheEntity<?> entity = db.get(key);
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

    public static String[] lrange(SyxCacheDb db, String key, int start, int end) {
        CacheEntity<?> entity = db.get(key);
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
        return link.stream().skip(start).limit(len).toArray(String []::new);
    }
}
