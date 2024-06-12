package cn.syx.cache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Locale;
import java.util.Objects;

public class SyxCacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String PRE_PLUS = "+";
    private static final String CRLF = "\r\n";
    private static final String OK = PRE_PLUS + "OK" + CRLF;

    private static final String INFO = "SyxCache 1.0.0, create by syx" + CRLF
            + "fsfsdfsdfdsfsdfsdfsdf" + CRLF;

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

    private void simpleWrite(ChannelHandlerContext chc, String content) {
        writeByteBuf(chc, PRE_PLUS + content + CRLF);
    }

    private void complexWrite(ChannelHandlerContext chc, String content) {
        writeByteBuf(chc, "$" + content.getBytes().length + CRLF + content + CRLF);
    }

    private void writeByteBuf(ChannelHandlerContext chc, String content) {
        ByteBuf byteBuf = Unpooled.buffer(128);
        byteBuf.writeBytes(content.getBytes());
        chc.writeAndFlush(byteBuf);
    }
}
