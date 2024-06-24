package cn.syx.cache.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = {"data", "score"})
public class ZSetCacheEntity<T> {

    private T data;

    private BigDecimal score;

    public static <T> ZSetCacheEntity<T> create(T data, BigDecimal score) {
        ZSetCacheEntity<T> entity = new ZSetCacheEntity<>();
        entity.setData(data);
        entity.setScore(score);
        return entity;
    }
}
