package cn.syx.cache.domain;

import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CacheTask {

    private ChannelHandlerContext ctx;
    private String message;
}
