package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class GetrangeCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "GETRANGE";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 3) {
            return "请求格式不符合set规范";
        }
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] values = req.getValuesSkipKey();
        int start = Integer.parseInt(values[0]);
        int end = Integer.parseInt(values[1]);
        return Reply.bulkString(StringCommandTool.getrange(db, key, start, end));
    }
}
