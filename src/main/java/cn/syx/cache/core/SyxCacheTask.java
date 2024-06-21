package cn.syx.cache.core;

import cn.syx.cache.command.Command;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.domain.CacheTask;
import cn.syx.cache.domain.Reply;
import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SyxCacheTask implements Runnable {

    private static final String PRE_PLUS = "+";
    private static final String PRE_MINUS = "-";
    private static final String CRLF = "\r\n";

    public static final SyxCacheHolder cache = new SyxCacheHolder();

    private final LinkedBlockingQueue<CacheTask> queue = new LinkedBlockingQueue<>();

    public void submitTask(CacheTask task) {
        queue.offer(task);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CacheTask task = queue.take();
                doTask(task.getCtx(), task.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("任务执行异常", e);
            }
        }
    }

    private void doTask(ChannelHandlerContext chc, String message) {
        String[] args = message.split(CRLF);
        log.info("接收到消息: {}", JSON.toJSONString(args));
        String cmd = args[2].toUpperCase(Locale.ROOT);

        Command<?> command = CommandManager.get(cmd);
        if (Objects.nonNull(command)) {
            Reply<?> reply = command.exec(cache, args);
            log.info("[{}] ===>: {}", cmd, JSON.toJSONString(reply.getValue()));
            replyContext(chc, reply);
            return;
        }

        replyContext(chc, Reply.error(String.format("ERR unknown command `%s`", cmd.toLowerCase(Locale.ROOT))));
    }

    private void errWrite(ChannelHandlerContext chc, String errMsg) {
        writeByteBuf(chc, PRE_MINUS + errMsg + CRLF);
    }

    private void nilWrite(ChannelHandlerContext chc) {
        writeByteBuf(chc, "$-1" + CRLF);
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

        writeByteBuf(chc, PRE_PLUS + content + CRLF);
    }

    private void complexWrite(ChannelHandlerContext chc, String content) {
        if (Objects.isNull(content)) {
            nilWrite(chc);
            return;
        }

        writeByteBuf(chc, "$" + content.getBytes().length + CRLF + content + CRLF);
    }

    private void arrayWrite(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

    private String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append("*-1" + CRLF);
        } else if (array.length == 0) {
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

    private void replyContext(ChannelHandlerContext chc, Reply<?> reply) {
        switch (reply.getType()) {
            case INT:
                intWrite(chc, (int) reply.getValue());
                break;
            case SIMPLE_STRING:
                simpleWrite(chc, (String) reply.getValue());
                break;
            case COMPLEX_STRING:
                complexWrite(chc, (String) reply.getValue());
                break;
            case ARRAY:
                arrayWrite(chc, (String[]) reply.getValue());
                break;
            case ERROR:
                errWrite(chc, reply.getErrMsg());
                break;
        }
    }
}
