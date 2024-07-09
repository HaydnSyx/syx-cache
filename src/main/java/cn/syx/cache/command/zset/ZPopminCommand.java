package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.ZSetCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

import java.util.Objects;

public class ZPopminCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "ZPOPMIN";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String value = req.getValue();
        return Reply.array(ZSetCommandTool.zpopmin(db, key, Objects.nonNull(value) ? Integer.parseInt(value) : 1));
    }
}
