package cn.syx.cache.codec.impl;

import cn.syx.cache.utils.CodecUtil;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.codec.RespDecoder;
import cn.syx.cache.core.SyxRespDecoder;
import cn.syx.cache.domain.RedisMessage;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RespArrayDecoder implements RespDecoder<List<Object>> {

    @Override
    public RedisMessage<List<Object>> decode(ByteBuf buffer) {
        int lineEndIndex = CodecUtil.findLineEndIndex(buffer);
        if (-1 == lineEndIndex) {
            return RedisMessage.fail();
        }
        // 解析元素个数
        RedisMessage<Long> lengthResult = (RedisMessage<Long>) SyxRespDecoder.DECODERS.get(SyxCacheConstants.ReplyType.NUMBER).decode(buffer);
        if (null == lengthResult || !lengthResult.isSuccess()) {
            return RedisMessage.fail();
        }

        long length = lengthResult.getData();
        // 空数据
        if (SyxCacheConstants.NEGATIVE_ONE.equals(length)) {
            return RedisMessage.success(null);
        }
        // 空数组
        if (SyxCacheConstants.ZERO.equals(length)) {
            return RedisMessage.success(Collections.emptyList());
        }
        List<Object> result = new ArrayList<>((int) length);
        // 递归
        for (int i = 0; i < length; i++) {
            result.add(SyxRespDecoder.decode(buffer));
        }
        return RedisMessage.success(result);
    }
}
