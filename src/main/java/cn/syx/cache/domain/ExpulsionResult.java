package cn.syx.cache.domain;

import lombok.Data;

@Data
public class ExpulsionResult {
    private boolean success;
    private String errorMsg;

    public static ExpulsionResult success() {
        ExpulsionResult result = new ExpulsionResult();
        result.setSuccess(true);
        return result;
    }

    public static ExpulsionResult fail(String msg) {
        ExpulsionResult result = new ExpulsionResult();
        result.setSuccess(false);
        result.setErrorMsg(msg);
        return result;
    }
}
