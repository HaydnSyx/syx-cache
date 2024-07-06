package cn.syx.cache.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Data
public class CacheCommandRequest {

    private boolean checkMemory;
    private int paramNum;
    
    private String[] params;
    private String key;
    private String value;
    /**
     * 跳过第一个数据的后续所有数据
     */
    private String[] valuesSkipKey;
    private List<Pair<String, String>> kvList;
    private List<Pair<String, String>> hashKvList;

    public String getTarget(int index) {
        return getParamNum() <= index ? null : params[index];
    }

    public int getParamNum() {
        return params.length;
    }
}
