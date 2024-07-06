package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class GetsetCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "GETSET";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() > 2) {
            return "请求格式不符合set规范";
        }
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        return Reply.bulkString(StringCommandTool.getset(db, req.getKey(), req.getValue()));
    }
}
