package cn.syx.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reply<T> {

    private T value;
    private ReplyType type;
    private String errMsg;

    public Reply(T value, ReplyType type) {
        this.value = value;
        this.type = type;
    }

    public Reply(T value, String errMsg) {
        this.value = value;
        this.errMsg = errMsg;
        this.type = ReplyType.ERROR;
    }

    public Reply(String errMsg) {
        this.errMsg = errMsg;
        this.type = ReplyType.ERROR;
    }

    public static Reply<Integer> integer(Integer value) {
        return new Reply<>(value, ReplyType.INT);
    }

    public static Reply<Integer> integer(Integer value, String errMsg) {
        return new Reply<>(value, errMsg);
    }

    public static Reply<String> simpleString(String value) {
        return new Reply<>(value, ReplyType.SIMPLE_STRING);
    }

    public static Reply<String> simpleString(String value, String errMsg) {
        return new Reply<>(value, errMsg);
    }

    public static Reply<String> complexString(String value) {
        return new Reply<>(value, ReplyType.COMPLEX_STRING);
    }

    public static Reply<String> complexString(String value, String errMsg) {
        return new Reply<>(value, errMsg);
    }

    public static Reply<String[]> array(String[] value) {
        return new Reply<>(value, ReplyType.ARRAY);
    }

    public static Reply<String[]> array(String[] value, String errMsg) {
        return new Reply<>(value, errMsg);
    }

    public static Reply<String> error(String errMsg) {
        return new Reply<>(errMsg);
    }
}
