package cn.syx.cache.domain;

import cn.syx.cache.core.SyxCacheConstants;
import io.github.haydnsyx.toolbox.base.NumberTool;
import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static cn.syx.cache.core.SyxCacheConstants.DB_KEY;

@Getter
@Builder
public class CacheTask {

    private int dbNum;
    private ChannelHandlerContext ctx;
    private String cmd;

    private CacheCommandRequest req;

    public static CacheTask create(ChannelHandlerContext ctx, String[] request) {
        // 命令
        String cmd = request[0].toUpperCase(Locale.ROOT);
        if (Objects.equals(cmd, "CONFIG")) {
            cmd = cmd + " " + request[1].toUpperCase(Locale.ROOT);
            request = Arrays.copyOfRange(request, 1, request.length);
        }

        return CacheTask.builder()
                .ctx(ctx)
                .dbNum(Optional.ofNullable(ctx.channel().attr(DB_KEY).get()).orElse(NumberTool.INT_ZERO))
                .cmd(cmd)
                .req(parseRequest(request))
                .build();
    }

    public static CacheTask create(int dbNum, String[] request) {
        // 命令
        String cmd = request[0].toUpperCase(Locale.ROOT);

        return CacheTask.builder()
                .dbNum(dbNum)
                .cmd(cmd)
                .req(parseRequest(request))
                .build();
    }

    /**
     * 牵制拆解请求列表，使用工作线程处理，减少主线程工作量
     *
     * @param request String[]
     * @return CacheCommandRequest
     */
    private static CacheCommandRequest parseRequest(String[] request) {
        CacheCommandRequest req = new CacheCommandRequest();
        // 参数列表
        String[] params = request.length > 1
                ? Arrays.copyOfRange(request, 1, request.length)
                : SyxCacheConstants.EMPTY_STRING_ARRAY;
        req.setParams(params);

        if (params.length >= 1) {
            req.setKey(params[0]);
        }

        if (params.length >= 2) {
            req.setValue(params[1]);
            req.setValuesSkipKey(Arrays.copyOfRange(params, 1, params.length));
            req.setKvList(getPairs(params));
        }

        if (params.length >= 3) {
            req.setHashKvList(getHashPairs(params));
        }

        return req;
    }

    private static List<Pair<String, String>> getPairs(String[] params) {
        int len = params.length / 2;
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            pairs.add(Pair.of(params[i * 2], params[1 + i * 2]));
        }
        return pairs;
    }

    private static List<Pair<String, String>> getHashPairs(String[] params) {
        int len = (params.length - 1) / 2;
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            pairs.add(Pair.of(params[1 + i * 2], params[2 + i * 2]));
        }
        return pairs;
    }
}
