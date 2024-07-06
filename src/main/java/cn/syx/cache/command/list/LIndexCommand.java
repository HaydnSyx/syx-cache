package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.ListCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

import java.util.Objects;

public class LIndexCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "LINDEX";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String value = req.getValue();
        if (Objects.isNull(value)) {
            return Reply.error("lindex 请求格式错误，请重新检查！");
        }
        return Reply.bulkString(ListCommandTool.lindex(db, key, Integer.parseInt(value)));
    }
}
