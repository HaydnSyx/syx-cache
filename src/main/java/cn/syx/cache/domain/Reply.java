package cn.syx.cache.domain;

import cn.syx.cache.core.SyxCacheConstants.ReplyType;
import lombok.Data;

@Data
public class Reply<T> {

    private boolean expire;
    private long expireTime;

    private T value;
    private ReplyType type;
    private String errMsg;

    public static <E> Reply<E> error(String errMsg) {
        Reply<E> reply = new Reply<>();
        reply.setType(ReplyType.ERROR);
        reply.setErrMsg(errMsg);
        return reply;
    }

    public static Reply<Integer> number(Integer value) {
        Reply<Integer> reply = new Reply<>();
        reply.setType(ReplyType.NUMBER);
        reply.setValue(value);
        return reply;
    }

    public static Reply<Long> number(Long value) {
        Reply<Long> reply = new Reply<>();
        reply.setType(ReplyType.NUMBER);
        reply.setValue(value);
        return reply;
    }

    public static Reply<String> simpleString(String value) {
        Reply<String> reply = new Reply<>();
        reply.setType(ReplyType.SIMPLE_STRING);
        reply.setValue(value);
        return reply;
    }

    public static Reply<String> bulkString(String value) {
        Reply<String> reply = new Reply<>();
        reply.setType(ReplyType.BULK_STRING);
        reply.setValue(value);
        return reply;
    }

    public static Reply<String[]> array(String[] value) {
        Reply<String[]> reply = new Reply<>();
        reply.setType(ReplyType.ARRAY);
        reply.setValue(value);
        return reply;
    }
}
