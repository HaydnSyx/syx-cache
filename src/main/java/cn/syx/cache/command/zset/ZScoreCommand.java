package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

import java.math.BigDecimal;
import java.util.Objects;

public class ZScoreCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "ZSCORE";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String value = req.getValue();
        BigDecimal zscore = ZSetCommandTool.zscore(db, key, value);
        return Reply.bulkString(Objects.isNull(zscore) ? null : zscore.toString());
    }
}
