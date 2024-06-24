package cn.syx.cache.core;

import cn.syx.cache.command.utils.PatternUtil;
import cn.syx.cache.domain.CacheEntity;
import cn.syx.cache.domain.ZSetCacheEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

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

    // ======================= set ============================
    public Integer sadd(String key, String[] values) {
        if (Objects.isNull(values)) {
            return 0;
        }

        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashSet<>());
            map.put(key, entity);
        }

        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        long count = Arrays.stream(values).filter(e -> !set.contains(e)).peek(set::add).count();
        return (int) count;
    }

    public String[] smembers(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return new String[0];
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return new String[0];
        }
        return set.toArray(String[]::new);
    }

    public Integer scard(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return 0;
        }
        return set.size();
    }

    public Integer sismember(String key, String value) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }
        LinkedHashSet<String> set = (LinkedHashSet<String>) entity.getData();
        if (Objects.isNull(set)) {
            return 0;
        }
        return set.contains(value) ? 1 : 0;
    }

    public Integer srem(String keys, String[] setKeys) {
        CacheEntity<?> entity = map.get(keys);
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

    public String[] spop(String key, int count) {
        CacheEntity<?> entity = map.get(key);
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

    // ======================= hash ============================

    public Integer hset(String key, String[] hkeys, String[] hvalues) {
        if (Objects.isNull(hkeys) || Objects.isNull(hvalues)
                || hkeys.length == 0
                || hvalues.length == 0
                || hkeys.length != hvalues.length) {
            return 0;
        }

        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashMap<String, String>());
            map.put(key, entity);
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

    public String hget(String key, String value) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        return map.get(value);
    }

    public String[] hgetall(String key) {
        CacheEntity<?> entity = map.get(key);
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

    public Integer hdel(String key, String[] hkeys) {
        CacheEntity<?> entity = map.get(key);
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

    public Integer hexists(String key, String value) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.containsKey(value) ? 1 : 0;
    }

    public String[] hmget(String key, String[] hkeys) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return new String[hkeys.length];
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return new String[hkeys.length];
        }

        return Arrays.stream(hkeys).map(map::get).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.size();
    }

    // ======================= zadd ============================
    public Integer zadd(String key, BigDecimal[] zscores, String[] zvalues) {
        if (Objects.isNull(zscores) || Objects.isNull(zvalues)
                || zscores.length == 0
                || zvalues.length == 0
                || zscores.length != zvalues.length) {
            return 0;
        }

        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            entity = CacheEntity.create(new LinkedHashSet<ZSetCacheEntity<String>>());
            map.put(key, entity);
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

    public Integer zcard(String key) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return 0;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return 0;
        }

        return map.size();
    }

    public BigDecimal zscore(String key, String value) {
        CacheEntity<?> entity = map.get(key);
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

    public Integer zcount(String key, BigDecimal start, BigDecimal end) {
        CacheEntity<?> entity = map.get(key);
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

    public Integer zrank(String key, String value) {
        CacheEntity<?> entity = map.get(key);
        if (Objects.isNull(entity)) {
            return null;
        }

        LinkedHashSet<ZSetCacheEntity<String>> map = (LinkedHashSet<ZSetCacheEntity<String>>) entity.getData();
        if (Objects.isNull(map)) {
            return null;
        }

        BigDecimal exist = zscore(key, value);
        if (Objects.isNull(exist)) {
            return null;
        }

        return (int) map.stream()
                .filter(e -> e.getScore().compareTo(exist) < 0)
                .count();
    }

    public Integer zrem(String key, String[] setKeys) {
        CacheEntity<?> entity = map.get(key);
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
