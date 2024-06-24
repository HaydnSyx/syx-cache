package cn.syx.cache.command.zset;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

import java.math.BigDecimal;
import java.util.Objects;

public class ZScoreCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "ZSCORE";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        BigDecimal zscore = cache.zscore(key, value);
        return Reply.complexString(Objects.isNull(zscore) ? null : zscore.toString());
    }
}
