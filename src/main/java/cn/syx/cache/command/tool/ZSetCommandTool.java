package cn.syx.cache.command.tool;

import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheEntity;
import cn.syx.cache.domain.ZSetCacheEntity;
import io.github.haydnsyx.toolbox.base.CollectionTool;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ZSetCommandTool {

    public static Integer zadd(SyxCacheDb db, String key, BigDecimal[] zscores, String[] zvalues) {
        if (Objects.isNull(zscores) || Objects.isNull(zvalues)
                || zscores.length == 0
                || zvalues.length == 0
                || zscores.length != zvalues.length) {
            return 0;
        }

        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashSet<ZSetCacheEntity<String>>());
            db.put(key, entity);
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
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.size();
    }

    public static BigDecimal zscore(SyxCacheDb db, String key, String value) {
        CacheEntity<?> entity = db.get(key);
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
        CacheEntity<?> entity = db.get(key);
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
        CacheEntity<?> entity = db.get(key);
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
        CacheEntity<?> entity = db.get(key);
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

    public static String[] zpopmin(SyxCacheDb db, String key, int num) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        List<ZSetCacheEntity<String>> elements = map.stream()
                .sorted(Comparator.comparing(ZSetCacheEntity::getScore))
                .limit(num).toList();

        if (CollectionTool.isEmpty(elements)) {
            return null;
        }

        String[] keys = new String[elements.size()];
        String[] results = new String[elements.size() * 2];

        for (int i = 0; i < elements.size(); i++) {
            ZSetCacheEntity<String> zsetEntity = elements.get(i);
            keys[i] = zsetEntity.getData();
            results[i * 2] = zsetEntity.getData();
            results[i * 2 + 1] = zsetEntity.getScore().toString();
        }

        zrem(db, key, keys);

        return results;
    }

    public static String[] zpopmax(SyxCacheDb db, String key, int num) {
        CacheEntity<?> entity = db.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        List<ZSetCacheEntity<String>> elements = map.stream()
                .sorted((a, b) -> b.getScore().compareTo(a.getScore()))
                .limit(num).toList();

        if (CollectionTool.isEmpty(elements)) {
            return null;
        }

        String[] keys = new String[elements.size()];
        String[] results = new String[elements.size() * 2];

        for (int i = 0; i < elements.size(); i++) {
            ZSetCacheEntity<String> zsetEntity = elements.get(i);
            keys[i] = zsetEntity.getData();
            results[i * 2] = zsetEntity.getData();
            results[i * 2 + 1] = zsetEntity.getScore().toString();
        }

        zrem(db, key, keys);

        return results;
    }
}
