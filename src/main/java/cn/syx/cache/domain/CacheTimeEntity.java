package cn.syx.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheTimeEntity {

    private int dbNum;
    private String key;
    private long turnNum;
}
