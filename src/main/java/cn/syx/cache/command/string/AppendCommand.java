package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class AppendCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "APPEND";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 2) {
            return "请求格式不符合set规范";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String value = req.getValue();
        return Reply.number(StringCommandTool.append(db, req.getKey(), value));
    }
}
