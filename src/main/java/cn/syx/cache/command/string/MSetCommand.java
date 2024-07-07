package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.core.SyxCacheConstants;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MSetCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "MSET";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheDb db, CacheCommandRequest req) {
        List<Pair<String, String>> pairs = req.getKvList();
        String[] keys = pairs.stream().map(Pair::getLeft).toArray(String[]::new);
        String[] values = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        StringCommandTool.mset(db, keys, values);
        return Reply.simpleString(SyxCacheConstants.OK_STRING);
    }
}
