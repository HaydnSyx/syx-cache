package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class RPopCommand extends AbstractCommand {

    @Override
    public String name() {
        return "RPOP";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String val = getValue(args);
            count = Integer.parseInt(val);
            return Reply.array(cache.rpop(key, count));
        }

        String[] lpop = cache.rpop(key, count);
        return Reply.complexString(lpop == null ? null : lpop[0]);
    }
}
