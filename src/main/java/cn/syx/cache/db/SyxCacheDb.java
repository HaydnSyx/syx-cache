package cn.syx.cache.db;

import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.domain.CacheEntity;
import cn.syx.cache.domain.ExpulsionResult;
import io.github.haydnsyx.toolbox.base.CollectionTool;
import lombok.Getter;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class SyxCacheDb {

    private final EvictionTypeEnums evictionType;
    private final int num;
    private final Map<String, CacheEntity<?>> map = new HashMap<>();
    private final Map<String, Long> expireMap = new HashMap<>();

    public SyxCacheDb(int num) {
        this.num = num;
        this.evictionType = EvictionTypeEnums.NO_ENVICTION;
    }

    public SyxCacheDb(int num, EvictionTypeEnums expulsionType) {
        this.num = num;
        this.evictionType = expulsionType;
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
        while (true) {
            String key = mapKeys.get(ThreadLocalRandom.current().nextInt(mapKeys.size()));
            if (keys.contains(key)) {
                continue;
            }

            keys.add(key);
            CacheEntity<?> entity = map.get(key);
            size = size + RamUsageEstimator.sizeOfObject(entity);

            if (size >= targetMemorySize) {
                break;
            }
        }

        return handleResult(keys);
    }

    private ExpulsionResult volatileRandom(long targetMemorySize) {
        Set<String> keys = new HashSet<>();

        long size = 0;
        List<String> mapKeys = new ArrayList<>(expireMap.keySet());
        while (true) {
            String key = mapKeys.get(ThreadLocalRandom.current().nextInt(mapKeys.size()));
            if (keys.contains(key)) {
                continue;
            }

            keys.add(key);
            CacheEntity<?> entity = map.get(key);
            size = size + RamUsageEstimator.sizeOfObject(entity);

            if (size >= targetMemorySize) {
                break;
            }
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
