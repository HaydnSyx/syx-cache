package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class SetrangeCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "SETRANGE";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 3) {
            return "请求格式不符合set规范";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] values = req.getValuesSkipKey();
        int start = Integer.parseInt(values[0]);
        String value = values[1];
        return Reply.number(StringCommandTool.setrange(db, key, start, value));
    }
}
