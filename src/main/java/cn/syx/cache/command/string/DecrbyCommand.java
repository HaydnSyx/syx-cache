package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class DecrbyCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "DECRBY";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String value = req.getValue();
        try {
            return Reply.number(StringCommandTool.decrBy(db, key, Integer.parseInt(value)));
        } catch (Exception e) {
            return Reply.error("[" + key + "] get value is not an integer or out of range");
        }
    }
}
