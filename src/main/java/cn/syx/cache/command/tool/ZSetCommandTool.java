package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;
import cn.syx.cache.domain.ZSetCacheEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZSetCommandTool {

    public static Integer zadd(SyxCacheDb db, String key, BigDecimal[] zscores, String[] zvalues) {
        if (Objects.isNull(zscores) || Objects.isNull(zvalues)
                || zscores.length == 0
                || zvalues.length == 0
                || zscores.length != zvalues.length) {
            return 0;
        }

        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashSet<ZSetCacheEntity<String>>());
            db.getMap().put(key, entity);
        }

        LinkedHashSet<ZSetCacheEntity<String>> zset = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        int count = 0;
        for (int i = 0; i < zscores.length; i++) {
            int finalI = i;
            AtomicBoolean add = new AtomicBoolean(false);
            ZSetCacheEntity<String> zsetEntity = zset.stream()
                    .filter(e -> Objects.equals(e.getData(), zvalues[finalI]))
                    .findFirst()
                    .orElseGet(() -> {
                        add.set(true);
                        return ZSetCacheEntity.create(zvalues[finalI], zscores[finalI]);
                    });
            zsetEntity.setScore(zscores[i]);
            if (add.get()) {
                zset.add(zsetEntity);
                count++;
            }
        }
        return count;
    }

    public static Integer zcard(SyxCacheDb db, String key) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return db.getMap().size();
    }

    public static BigDecimal zscore(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        ZSetCacheEntity<String> zsetEntity = map.stream()
                .filter(e -> Objects.equals(e.getData(), value))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(zsetEntity)) {
            return null;
        }
        return zsetEntity.getScore();
    }

    public static Integer zcount(SyxCacheDb db, String key, BigDecimal start, BigDecimal end) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return (int) map.stream()
                .filter(e -> e.getScore().compareTo(start) >= 0 && e.getScore().compareTo(end) <= 0)
                .count();
    }

    public static Integer zrank(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        BigDecimal exist = zscore(db, key, value);
        if (Objects.isNull(exist)) {
            return null;
        }

        return (int) map.stream()
                .filter(e -> e.getScore().compareTo(exist) < 0)
                .count();
    }

    public static Integer zrem(SyxCacheDb db, String key, String[] setKeys) {
        CacheEntity<?> entity = db.getMap().get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        long count = Arrays.stream(setKeys)
                .filter(e -> map.removeIf(z -> Objects.equals(z.getData(), e)))
                .count();
        return (int) count;
    }
}
