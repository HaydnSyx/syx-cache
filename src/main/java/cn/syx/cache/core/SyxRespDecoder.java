package cn.syx.cache.core;

import cn.syx.cache.codec.*;
import cn.syx.cache.codec.impl.*;
import cn.syx.cache.domain.RedisMessage;
import cn.syx.cache.utils.CodecUtil;
import com.alibaba.fastjson2.JSON;
import io.github.haydnsyx.toolbox.base.StringTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SyxRespDecoder extends ByteToMessageDecoder {

    public static final Map<SyxCacheConstants.ReplyType, RespDecoder<?>> DECODERS;
    static {
        DECODERS = new HashMap<>();
        DECODERS.put(SyxCacheConstants.ReplyType.SIMPLE_STRING, new RespSimpleStringDecoder());
        DECODERS.put(SyxCacheConstants.ReplyType.ERROR, new RespErrDecoder());
        DECODERS.put(SyxCacheConstants.ReplyType.NUMBER, new RespNumberDecoder());
        DECODERS.put(SyxCacheConstants.ReplyType.BULK_STRING, new RespBulkStringDecoder());
        DECODERS.put(SyxCacheConstants.ReplyType.ARRAY, new RespArrayDecoder());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 客户端访问服务端只有数组类型
        List<Object> data = decode(in);
        if (Objects.isNull(data)) {
            return;
        }

        log.info("decode buffer success, content: {}", JSON.toJSONString(data));
        String[] result = data.stream().map(e -> {
            if (Objects.isNull(e)) {
                return null;
            }

            if (e instanceof String v) {
                return v;
            }

            if (e instanceof Number v) {
                return v.toString();
            }
            return e.toString();
        }).toArray(String[]::new);

        out.add(result);
    }

    public static <OUT> OUT decode(ByteBuf buffer) {
        // 没有可用字节则忽略
        if (!buffer.isReadable(1)) {
            log.info("decode buffer is empty");
            return null;
        }

        // 标记解析点
        int mark = buffer.markReaderIndex().readerIndex();

        // 通过第一个字节解析协议
        byte b = buffer.readByte();
        RespDecoder<?> decoder = DECODERS.get(determineReplyType(b));
        // 可能是内联标记
        if (Objects.isNull(decoder)) {
            log.info("decode buffer error, skip this because of unknown reply type, byte: {}", b);
            // 重置到标记点
            buffer.readerIndex(mark);
            String content = CodecUtil.readLine(buffer);
            log.info("decode buffer inline content: {}", content);
            if (StringTool.isBlank(content)) {
                // 重置到标记点
                buffer.readerIndex(mark);
                return null;
            }

            return (OUT) Arrays.stream(content.split(" ")).toList();
        }

        RedisMessage<?> content = decoder.decode(buffer);
        if (Objects.isNull(content) || !content.isSuccess()) {
            // 没有解析成功则回到标记点
            buffer.readerIndex(mark);
            log.info("decode buffer fail, content is not complete");
            return null;
        }

        return (OUT) content.getData();
    }

    private static SyxCacheConstants.ReplyType determineReplyType(Byte b) {
        // 读取第一个字节
        return switch (b) {
            case SyxCacheConstants.PLUS_BYTE -> SyxCacheConstants.ReplyType.SIMPLE_STRING;
            case SyxCacheConstants.MINUS_BYTE -> SyxCacheConstants.ReplyType.ERROR;
            case SyxCacheConstants.COLON_BYTE -> SyxCacheConstants.ReplyType.NUMBER;
            case SyxCacheConstants.DOLLAR_BYTE -> SyxCacheConstants.ReplyType.BULK_STRING;
            case SyxCacheConstants.ASTERISK_BYTE -> SyxCacheConstants.ReplyType.ARRAY;
            default -> null;
        };
    }
}
