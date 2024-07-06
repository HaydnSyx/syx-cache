package cn.syx.cache.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
public class SingletonUtil {

    private static final Map<Class<?>, Object> SINGLETON_MAP = new ConcurrentHashMap<>();

    private SingletonUtil() {
    }

    public static <T> T getInstance(Class<T> cls) {
        Object object = SINGLETON_MAP.get(cls);
        if (Objects.isNull(object)) {
            try {
                Constructor<?> constructor = cls.getConstructor();
                constructor.setAccessible(true);
                object = constructor.newInstance();
                SINGLETON_MAP.put(cls, object);
            } catch (Exception e) {
                log.error("get instance error, cls:{}", cls.getName(), e);
            }
        }
        return (T) object;
    }

    public static <T> T getInstance(Class<T> cls, Supplier<T> supplier) {
        Object object = SINGLETON_MAP.get(cls);
        if (Objects.isNull(object)) {
            object = supplier.get();
            SINGLETON_MAP.put(cls, object);
        }
        return (T) object;
    }
}
