package cn.syx.cache.command;

import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;
import io.github.haydnsyx.toolbox.base.StringTool;

public abstract class AbstractCommand<T> implements Command<T>{

    @Override
    public Reply<T> exec(SyxCacheHolder cache, String[] args) {
        String errMsg = checkArgs(args);
        if (StringTool.isNotBlank(errMsg)) {
            return Reply.error(errMsg);
        }

        return doExec(cache, args);
    }

    protected abstract String checkArgs(String[] args);

    protected abstract Reply<T> doExec(SyxCacheHolder cache, String[] args);
}
