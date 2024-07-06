package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.SetCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class SMembersCommand extends AbstractCommand<String[]> {

    @Override
    public String name() {
        return "SMEMBERS";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String[]> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        return Reply.array(SetCommandTool.smembers(db, key));
    }
}
