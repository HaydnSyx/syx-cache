package cn.syx.cache.db;

import cn.syx.cache.domain.CacheEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.util.RamUsageEstimator;

import java.lang.instrument.Instrumentation;
import java.util.*;

@Getter
public class SyxCacheDb {

    private final int num;
    private final Map<String, CacheEntity<?>> map = new HashMap<>();
    private final Map<String, Long> expireMap = new HashMap<>();

    public SyxCacheDb(int num) {
        this.num = num;
    }

    public long dataMemorySize() {
        return RamUsageEstimator.sizeOfMap(map);
    }

    public long totalMemorySize() {
        return RamUsageEstimator.sizeOfMap(map);
    }
}
