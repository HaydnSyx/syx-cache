package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.tool.StringCommandTool;
import cn.syx.cache.db.SyxCacheDb;
import cn.syx.cache.domain.CacheCommandRequest;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MSetnxCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "MSETNX";
    }

    @Override
    protected String checkArgs(CacheCommandRequest req) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheDb db, CacheCommandRequest req) {
        List<Pair<String, String>> pairs = req.getKvList();
        String[] keys = pairs.stream().map(Pair::getLeft).toArray(String[]::new);
        String[] values = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        return Reply.number(StringCommandTool.msetnx(db, keys, values));
    }
}
