package cn.syx.cache.db;

public enum EvictionTypeEnums {

    NO_ENVICTION,
    VOLATILE_LRU,
    ALLKEYS_LRU,
    VOLATILE_RANDOM,
    ALLKEYS_RANDOM,
    VOLATILE_TTL,
    VOLATILE_LFU,
    ALLKEYS_LFU
}
