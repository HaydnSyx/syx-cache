package cn.syx.cache.command.generic;

import cn.syx.cache.command.AbstractGenericCommand;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;

public class InfoCommand extends AbstractGenericCommand<String> {

    private static final String INFO = "SyxCache 1.0.0, create by syx" + SyxCacheConstants.CRLF_STRING
            + "为了更好的了解redis" + SyxCacheConstants.CRLF_STRING;

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        return Reply.bulkString(INFO);
    }
}
