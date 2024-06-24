package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class ZRemCommand extends AbstractCommand<Integer> {

    @Override
    public String name() {
        return "ZREM";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<Integer> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String[] setKeys = getValues(args);
        return Reply.integer(cache.zrem(key, setKeys));
    }
}
