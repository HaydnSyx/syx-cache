package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.ListCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class LPopCommand extends AbstractCommand {

    @Override
    public String name() {
        return "LPOP";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<?> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        int count = 1;
        if (req.getParamNum() > 1) {
            String val = req.getValue();
            count = Integer.parseInt(val);
            return Reply.array(ListCommandTool.lpop(db, key, count));
        }

        String[] lpop = ListCommandTool.lpop(db, key, count);
        return Reply.bulkString(lpop == null ? null : lpop[0]);
    }
}
