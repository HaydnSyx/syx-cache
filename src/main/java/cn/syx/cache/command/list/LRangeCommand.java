package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.ListCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class LRangeCommand extends AbstractCommand {

    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<?> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] values = req.getValuesSkipKey();
        int start = Integer.parseInt(values[0]);
        int end = Integer.parseInt(values[1]);
        return Reply.array(ListCommandTool.lrange(db, key, start, end));
    }
}
