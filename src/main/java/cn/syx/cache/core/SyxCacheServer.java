package cn.syx.cache.core;

import cn.syx.cache.SyxCachePlugin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SyxCacheServer implements SyxCachePlugin {

    private int port = 6379;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel channel;

    @Override
    public void init() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("cache-boss"));
        workerGroup = new NioEventLoopGroup(16, new DefaultThreadFactory("cache-work"));
    }

    @Override
    public void startup() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SyxCacheDecoder());
//                            ch.pipeline().addLast(new RedisEncoder());
                            ch.pipeline().addLast(new SyxCacheHandler());
                        }
                    });

            channel = b.bind(port).sync().channel();
            System.out.println("开启netty cache服务器，监听端口为：" + port);
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
