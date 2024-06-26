package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class LPopCommand extends AbstractCommand {

    @Override
    public String name() {
        return "LPOP";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<?> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String val = getValue(args);
            count = Integer.parseInt(val);
            return Reply.array(cache.lpop(key, count));
        }

        String[] lpop = cache.lpop(key, count);
        return Reply.complexString(lpop == null ? null : lpop[0]);
    }
}
