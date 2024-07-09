package cn.syx.cache.core;

import cn.syx.cache.SyxCachePlugin;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.utils.SingletonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Order(1)
@Component
public class SyxCacheServer implements SyxCachePlugin {

    private SyxCacheTask task;
    private Thread taskThread;
    private SyxCacheTimeWheel timeWheel;
    private SyxCacheMonitor monitor;

    private SyxCacheDb[] DBS;

    private int port = 6379;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel channel;

    @Override
    public void init() {
        // 创建DB
        DBS = new SyxCacheDb[16];
        for (int i = 0; i < DBS.length; i++) {
            DBS[i] = new SyxCacheDb(i);
        }

        // 创建真正的工作线程
        task = SingletonUtil.getInstance(SyxCacheTask.class, () -> new SyxCacheTask(DBS));
        taskThread = new Thread(task, "task-processor");

        timeWheel = SingletonUtil.getInstance(SyxCacheTimeWheel.class);
        timeWheel.init();

        // 创建监控者
        monitor = SingletonUtil.getInstance(SyxCacheMonitor.class, () -> new SyxCacheMonitor(DBS));
        monitor.init();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("cache-boss"));
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("cache-work"));
    }

    @Override
    public void startup() {
        try {
            // 开启任务
            taskThread.start();
            log.info("cache task start up ...");

            timeWheel.start();
            log.info("cache wheel task start up ...");

            monitor.start();
            log.info("monitor task start up ...");

            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SyxRespDecoder());
                            ch.pipeline().addLast(new SyxRespEncoder());
                            ch.pipeline().addLast(new SyxCacheHandler(task));
                        }
                    });

            channel = b.bind(port).sync().channel();
            log.info("开启netty cache服务器，监听端口为：{}", port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
        if (Objects.nonNull(channel)) {
            channel.close();
            channel = null;
        }
        if (Objects.nonNull(bossGroup)) {
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }
        if (Objects.nonNull(workerGroup)) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
    }
}
