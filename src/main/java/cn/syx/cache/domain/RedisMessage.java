package cn.syx.cache.domain;

import lombok.Data;

@Data
public class RedisMessage<T> {

    private boolean success;
    private T data;

    public static <OUT> RedisMessage<OUT> success(OUT data) {
        RedisMessage<OUT> redisMessage = new RedisMessage<>();
        redisMessage.setSuccess(true);
        redisMessage.setData(data);
        return redisMessage;
    }

    public static <OUT> RedisMessage<OUT> fail() {
        RedisMessage<OUT> redisMessage = new RedisMessage<>();
        redisMessage.setSuccess(false);
        return redisMessage;
    }
}
