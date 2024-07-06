package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.SetCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import io.github.haydnsyx.toolbox.base.StringTool;

import java.util.Objects;

public class SPopCommand extends AbstractCommand {

    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<?> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String value = req.getValue();
        boolean flag = false;
        int count = 1;
        if (StringTool.isNotBlank(value)) {
            count = Integer.parseInt(value);
            flag = true;
        }

        String[] spop = SetCommandTool.spop(db, key, count);
        if (flag) {
            return Reply.array(Objects.isNull(spop) ? new String[0] : spop);
        }
        return Reply.bulkString(Objects.isNull(spop) ? null : spop[0]);
    }
}
