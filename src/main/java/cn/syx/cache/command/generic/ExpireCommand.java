package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.command.tool.GenericCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class ExpireCommand extends AbstractGenericCommand<Integer> {

    @Override
    public String name() {
        return "EXPIRE";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        if (req.getParamNum() != 2) {
            return "ERR wrong number of arguments for 'expire' command";
        }
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        String key = req.getKey();
        String[] values = req.getValuesSkipKey();
        long timeout = System.currentTimeMillis() + Integer.parseInt(values[0]) * 1000L;
        String type = values.length > 1 ? values[1] : null;
        int count = GenericCommandTool.expireat(db, key, timeout, type);
        Reply<Integer> reply = Reply.number(count);
        if (count == 1) {
            reply.setExpire(true);
            reply.setExpireTime(timeout);
        }
        return reply;
    }
}
