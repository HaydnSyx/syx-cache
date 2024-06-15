package cn.syx.cache.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Locale;
import java.util.Objects;

public class SyxCacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String PRE_PLUS = "+";
    private static final String PRE_MINUS = "-";
    private static final String CRLF = "\r\n";
    private static final String OK = "OK";

    private static final String INFO = "SyxCache 1.0.0, create by syx" + CRLF
            + "fsfsdfsdfdsfsdfsdfsdf" + CRLF;

    public static final SyxCacheHolder cache = new SyxCacheHolder();

    @Override
    protected void channelRead0(ChannelHandlerContext chc, String message) throws Exception {
        String[] args = message.split(CRLF);
        System.out.println("接收到消息: " + String.join(",", args));

        String content = null;

        String cmd = args[2].toUpperCase(Locale.ROOT);
        if (Objects.equals("COMMAND", cmd)) {
            content = "*3"
                    + CRLF + "$7"
                    + CRLF + "COMMAND"
                    + CRLF + "$4"
                    + CRLF + "ping"
                    + CRLF + "$4"
                    + CRLF + "info"
                    + CRLF;
            writeByteBuf(chc, content);
            return;
        }

        if (Objects.equals("SET", cmd)) {
            String valueFlag = args[5];
            String value = null;
            if (valueFlag.startsWith("$") && valueFlag.length() >= 2) {
                int valueLength = Integer.parseInt(valueFlag.substring(1));
                if (valueLength == 0) {
                    value = "";
                } else {
                    value = args[6];
                    if (value.length() != valueLength) {
                        errWrite(chc,  "value长度与请求参数中的长度不符");
                        return;
                    }
                }
            } else {
                errWrite(chc, "请求格式不符合set规范");
            }
            cache.set(args[4], value);
            simpleWrite(chc, OK);
            return;
        }

        if (Objects.equals("GET", cmd)) {
            complexWrite(chc, cache.get(args[4]));
            return;
        }

        if (Objects.equals("DEL", cmd)) {
            int length = (args.length - 3) / 2;
            String[] keys = new String[length];
            for (int i = 0; i < length; i++) {
                keys[i] = args[4 + i * 2];
            }
            int count = cache.del(keys);
            intWrite(chc, count);
            return;
        }

        if (Objects.equals("MGET", cmd)) {
            int length = (args.length - 3) / 2;
            String[] keys = new String[length];
            for (int i = 0; i < length; i++) {
                keys[i] = args[4 + i * 2];
            }
            String[] values = cache.mget(keys);
            arrayWrite(chc, values);
            return;
        }

        if (Objects.equals("MSET", cmd)) {
            int length = (args.length - 3) / 4;
            String[] keys = new String[length];
            String[] vals = new String[length];
            for (int i = 0; i < length; i++) {
                keys[i] = args[4 + i * 4];
                vals[i] = args[6 + i * 4];
            }
            cache.mset(keys, vals);
            simpleWrite(chc, OK);
            return;
        }

        if (Objects.equals("EXISTS", cmd)) {
            int length = (args.length - 3) / 2;
            String[] keys = new String[length];
            for (int i = 0; i < length; i++) {
                keys[i] = args[4 + i * 2];
            }
            intWrite(chc, cache.exists(keys));
            return;
        }

        if (Objects.equals("STRLEN", cmd)) {
            String value = cache.get(args[4]);
            intWrite(chc, Objects.isNull(value) ? 0 : value.length());
            return;
        }

        if (Objects.equals("INCR", cmd)) {
            try {
                intWrite(chc, cache.incr(args[4]));
            } catch (Exception e) {
                errWrite(chc, "value is not an integer or out of range");
            }
            return;
        }

        if (Objects.equals("DECR", cmd)) {
            try {
                intWrite(chc, cache.decr(args[4]));
            } catch (Exception e) {
                errWrite(chc, "value is not an integer or out of range");
            }
            return;
        }


        if (Objects.equals("PING", cmd)) {
            if (args.length >= 5) {
                simpleWrite(chc, args[4]);
            } else {
                simpleWrite(chc, "PONG");
            }
            return;
        }

        if (Objects.equals("INFO", cmd)) {
            complexWrite(chc, INFO);
            return;
        }

        simpleWrite(chc, OK);
    }

    private void errWrite(ChannelHandlerContext chc, String errMsg) {
        writeByteBuf(chc, PRE_MINUS + errMsg + CRLF);
    }

    private void nilWrite(ChannelHandlerContext chc) {
        writeByteBuf(chc, "$-1" + CRLF);
    }

    private void emptyWrite(ChannelHandlerContext chc) {
        writeByteBuf(chc, "$0" + CRLF + CRLF);
    }

    private void intWrite(ChannelHandlerContext chc, int value) {
        writeByteBuf(chc, intEncoder(value));
    }

    private String intEncoder(Integer value) {
        return ":" + value + CRLF;
    }

    private void simpleWrite(ChannelHandlerContext chc, String content) {
        if (Objects.isNull(content)) {
            nilWrite(chc);
            return;
        }

        if (content.isEmpty()) {
            emptyWrite(chc);
            return;
        }

        writeByteBuf(chc, PRE_PLUS + content + CRLF);
    }

    private void complexWrite(ChannelHandlerContext chc, String content) {
        if (Objects.isNull(content)) {
            nilWrite(chc);
            return;
        }

        if (content.isEmpty()) {
            emptyWrite(chc);
            return;
        }

        writeByteBuf(chc, "$" + content.getBytes().length + CRLF + content + CRLF);
    }

    private void arrayWrite(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

    private String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if(array == null) {
            sb.append("*-1" + CRLF);
        } else if(array.length == 0) {
            sb.append("*0" + CRLF);
        } else {
            sb.append("*").append(array.length).append(CRLF);
            for (Object obj : array) {
                if (obj == null) {
                    sb.append("$-1" + CRLF);
                } else {
                    if (obj instanceof Integer it) {
                        sb.append(intEncoder(it));
                    } else if (obj instanceof String str) {
                        sb.append("$").append(str.getBytes().length).append(CRLF).append(str).append(CRLF);
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }
        return sb.toString();
    }

    private void writeByteBuf(ChannelHandlerContext chc, String content) {
        ByteBuf byteBuf = Unpooled.buffer(128);
        byteBuf.writeBytes(content.getBytes());
        chc.writeAndFlush(byteBuf);
    }
}
