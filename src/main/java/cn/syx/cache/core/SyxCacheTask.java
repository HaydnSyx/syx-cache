package cn.syx.cache.core;

import cn.syx.cache.command.Command;
import cn.syx.cache.command.CommandManager;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.CacheTask;
import cn.syx.cache.domain.Reply;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SyxCacheTask implements Runnable {

    private final SyxCacheDb[] DBS;
    private final LinkedBlockingQueue<CacheTask> queue = new LinkedBlockingQueue<>();

    public SyxCacheTask(SyxCacheDb[] DBS) {
        this.DBS = DBS;
    }

    public void submitTask(CacheTask task) {
        queue.offer(task);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CacheTask task = queue.take();
                doTask(task.getCtx(), task.getDbNum(), task.getCmd(), task.getReq());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("任务执行异常", e);
            }
        }
    }

    private void doTask(ChannelHandlerContext ctx, int dbNum, String cmd, CacheCommandRequest req) {
        log.info("接收到消息: cmd={}, params={}", cmd, JSON.toJSONString(req.getParams()));
        Command<?> command = CommandManager.get(cmd);
        Reply<?> reply;
        if (Objects.isNull(command)) {
            reply = Reply.error(String.format("ERR unknown command `%s`", cmd.toLowerCase(Locale.ROOT)));
        } else {
            req.setCheckMemory(command.checkMemory());

            reply = command.exec(ctx, DBS[dbNum], req);
            log.info("[{}][{}] ===>: {}", dbNum, cmd, JSON.toJSONString(reply.getValue()));
        }

        if (Objects.nonNull(ctx)) {
            ctx.writeAndFlush(reply);
        }
    }
}
