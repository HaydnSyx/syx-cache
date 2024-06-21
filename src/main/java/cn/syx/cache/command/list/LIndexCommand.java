package cn.syx.cache.command.list;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

import java.util.Objects;

public class LIndexCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "LINDEX";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        if (Objects.isNull(value)) {
            return Reply.error("lindex 请求格式错误，请重新检查！");
        }
        return Reply.complexString(cache.lindex(key, Integer.parseInt(value)));
    }
}
