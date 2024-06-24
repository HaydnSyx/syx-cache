package cn.syx.cache.command.hash;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class HGetCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "HGET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        return Reply.complexString(cache.hget(key, value));
    }
}
