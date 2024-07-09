package cn.syx.cache.db;

import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.domain.CacheEntity;
import cn.syx.cache.domain.ExpulsionResult;
import io.github.haydnsyx.toolbox.base.CollectionTool;
import lombok.Getter;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//@Getter
public class SyxCacheDb {

    private final EvictionTypeEnums evictionType;
    @Getter
    private final int num;
    private final Map<String, CacheEntity<?>> map;
    private final Map<String, Long> expireMap;

//    private final ChronicleMap<String, CacheEntity> map;
//    private final ChronicleMap<String, Long> expireMap;

    public SyxCacheDb(int num) {
        this(num, EvictionTypeEnums.NO_ENVICTION);
    }

    public SyxCacheDb(int num, EvictionTypeEnums expulsionType) {
        this.num = num;
        this.evictionType = expulsionType;

        map = new HashMap<>();
        expireMap = new HashMap<>();

        /*map = ChronicleMapBuilder
                .of(String.class, CacheEntity.class)
                .name("redis-db-" + num)
                .entries(10_00_000)
                .create();
        expireMap = ChronicleMapBuilder
                .of(String.class, Long.class)
                .name("redis-expire-map-" + num)
                .entries(10_00_000)
                .create();*/
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public int size() {
        return map.size();
    }

    public CacheEntity get(String key) {
        return map.get(key);
    }

    public CacheEntity put(String key, CacheEntity entity) {
        return map.put(key, entity);
    }

    public CacheEntity putIfAbsent(String key, CacheEntity entity) {
        return map.putIfAbsent(key, entity);
    }

    public boolean containsKey(String key) {
        return Objects.nonNull(get(key));
    }

    public CacheEntity remove(String key) {
        return map.remove(key);
    }

    public Long getExpireTime(String key) {
        return expireMap.get(key);
    }

    public Long putExpireTime(String key, long time) {
        return expireMap.put(key, time);
    }

    public boolean existExpire(String key) {
        return Objects.nonNull(getExpireTime(key));
    }

    public Long removeExpire(String key) {
        return expireMap.remove(key);
    }


    public long dataMemorySize() {
        return RamUsageEstimator.sizeOfMap(map);
    }

    public long totalMemorySize() {
        return RamUsageEstimator.sizeOfMap(map) + RamUsageEstimator.sizeOfMap(expireMap);
    }

    public ExpulsionResult expulsion(long targetMemorySize) {
        return switch (evictionType) {
            case NO_ENVICTION -> ExpulsionResult.fail("over memory size");
            case VOLATILE_LRU -> volatileLru(targetMemorySize);
            case ALLKEYS_LRU -> allkeysLru(targetMemorySize);
            case VOLATILE_RANDOM -> volatileRandom(targetMemorySize);
            case ALLKEYS_RANDOM -> allkeysRandom(targetMemorySize);
            case VOLATILE_TTL -> volatileTtl(targetMemorySize);
            case VOLATILE_LFU -> volatileLfu(targetMemorySize);
            case ALLKEYS_LFU -> allkeysLfu(targetMemorySize);
        };
    }

    private ExpulsionResult allkeysLfu(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        return handleResult(keys);
    }

    private ExpulsionResult volatileLfu(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        return handleResult(keys);
    }

    private ExpulsionResult volatileTtl(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        return handleResult(keys);
    }

    private ExpulsionResult allkeysRandom(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        long size = 0;
        List<String> mapKeys = new ArrayList<>(map.keySet());
        while (!mapKeys.isEmpty()) {
            int index = ThreadLocalRandom.current().nextInt(mapKeys.size());
            String key = mapKeys.remove(index);

            keys.add(key);
            CacheEntity<?> entity = map.get(key);
            size = size + RamUsageEstimator.sizeOfObject(entity);

            if (size >= targetMemorySize) {
                break;
            }
        }

        if (size < targetMemorySize) {
            return handleResult(null);
        }

        return handleResult(keys);
    }

    private ExpulsionResult volatileRandom(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        long size = 0;
        List<String> mapKeys = new ArrayList<>(expireMap.keySet());
        while (!mapKeys.isEmpty()) {
            int index = ThreadLocalRandom.current().nextInt(mapKeys.size());
            String key = mapKeys.remove(index);

            keys.add(key);
            CacheEntity<?> entity = map.get(key);
            size = size + RamUsageEstimator.sizeOfObject(entity);

            if (size >= targetMemorySize) {
                break;
            }
        }

        if (size < targetMemorySize) {
            return handleResult(null);
        }

        return handleResult(keys);
    }

    private ExpulsionResult allkeysLru(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        return handleResult(keys);
    }

    private ExpulsionResult volatileLru(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        return handleResult(keys);
    }

    private ExpulsionResult handleResult(Set<String> keys) {
        if (CollectionTool.isEmpty(keys)) {
            return ExpulsionResult.fail("over memory size");
        }

        deleteKeys(keys);
        return ExpulsionResult.success();
    }

    private void deleteKeys(Set<String> keys) {
        GenericCommandTool.del(this, keys.toArray(String[]::new));
    }
}
