package cn.syx.cache.command.string;

import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MSetCommand implements Command<String> {

    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<String> exec(SyxCacheHolder cache, String[] args) {
        List<Pair<String, String>> pairs = getPairs(args);
        String[] keys = pairs.stream().map(Pair::getLeft).toArray(String[]::new);
        String[] values = pairs.stream().map(Pair::getRight).toArray(String[]::new);
        cache.mset(keys, values);
        return Reply.simpleString(OK);
    }
}
