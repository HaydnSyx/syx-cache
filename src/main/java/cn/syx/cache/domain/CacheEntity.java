package cn.syx.cache.domain;

import lombok.Data;

@Data
public class CacheEntity<T> {

    private T data;

    public static <T> CacheEntity<T> create(T data) {
        CacheEntity<T> entity = new CacheEntity<>();
        entity.setData(data);
        return entity;
    }
}
