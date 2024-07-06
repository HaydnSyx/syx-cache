package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class MGetCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "MGET";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String[] keys = req.getParams();
        String[] values = StringCommandTool.mget(db, keys);
        return Reply.array(values);
    }
}
