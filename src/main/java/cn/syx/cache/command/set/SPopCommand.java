package cn.syx.cache.command.set;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;
import io.github.haydnsyx.toolbox.base.StringTool;

import java.util.Objects;

public class SPopCommand extends AbstractCommand {

    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<?> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        boolean flag = false;
        int count = 1;
        if (StringTool.isNotBlank(value)) {
            count = Integer.parseInt(value);
            flag = true;
        }

        String[] spop = cache.spop(key, count);
        if (flag) {
            return Reply.array(Objects.isNull(spop) ? new String[0] : spop);
        }
        return Reply.complexString(Objects.isNull(spop) ? null : spop[0]);
    }
}
