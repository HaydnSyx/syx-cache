package cn.syx.cache.core;

import cn.syx.cache.domain.Reply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static cn.syx.cache.core.SyxCacheConstants.*;

@Slf4j
public class SyxRespEncoder extends MessageToByteEncoder<Reply<?>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Reply<?> msg, ByteBuf out) throws Exception {
        // 生成字符串内容
        String content = convertToString(msg);
        log.info("===> 返回的信息: {}", content);
        
        // 转成字节流
        out.writeBytes(content.getBytes());
    }

    private String convertToString(Reply<?> reply) {
        return switch (reply.getType()) {
            case NUMBER -> numberEncoder((Number) reply.getValue());
            case SIMPLE_STRING -> simpleEncoder((String) reply.getValue());
            case BULK_STRING -> bulkEncoder((String) reply.getValue());
            case ARRAY -> arrayEncode((String[]) reply.getValue());
            case ERROR -> errEncoder(reply.getErrMsg());
        };
    }
    
    private String errEncoder(String errMsg) {
        return MINUS_STRING + errMsg + CRLF_STRING;
    }

    private String nilEncoder() {
        return DOLLAR_STRING + NEGATIVE_ONE_STRING + CRLF_STRING;
    }
    private String numberEncoder(Number value) {
        return COLON_STRING + value.toString() + CRLF_STRING;
    }

    private String simpleEncoder(String content) {
        if (Objects.isNull(content)) {
            return nilEncoder();
        }

        return PLUS_STRING + content + CRLF_STRING;
    }

    private String bulkEncoder(String content) {
        if (Objects.isNull(content)) {
            return nilEncoder();
        }

        return DOLLAR_STRING + 
                content.getBytes().length + CRLF_STRING +
                content + CRLF_STRING;
    }

    private String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append(ASTERISK_STRING).append(NEGATIVE_ONE_STRING).append(CRLF_STRING);
        } else if (array.length == 0) {
            sb.append(ASTERISK_STRING).append(ZERO_STRING).append(CRLF_STRING);
        } else {
            sb.append(ASTERISK_STRING).append(array.length).append(CRLF_STRING);
            for (Object obj : array) {
                if (obj == null) {
                    sb.append(DOLLAR_STRING).append(NEGATIVE_ONE_STRING).append(CRLF_STRING);
                } else {
                    if (obj instanceof Number it) {
                        sb.append(numberEncoder(it));
                    } else if (obj instanceof String str) {
                        sb.append(DOLLAR_STRING).append(str.getBytes().length).append(CRLF_STRING)
                                .append(str).append(CRLF_STRING);
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }
        return sb.toString();
    }
}
