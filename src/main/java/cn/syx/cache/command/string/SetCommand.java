package cn.syx.cache.command.string;

import cn.syx.cache.command.AbstractCommand;
import cn.syx.cache.command.Command;
import cn.syx.cache.core.SyxCacheHolder;
import cn.syx.cache.domain.Reply;

public class SetCommand extends AbstractCommand<String> {

    @Override
    public String name() {
        return "SET";
    }

    @Override
    protected String checkArgs(String[] args) {
        return null;
    }

    @Override
    public Reply<String> doExec(SyxCacheHolder cache, String[] args) {
        if (args.length < 6) {
            return Reply.error("请求参数不足");
        }

        String valueFlag = args[5];
        String value;
        if (valueFlag.startsWith("$") && valueFlag.length() >= 2) {
            int valueLength = Integer.parseInt(valueFlag.substring(1));
            if (valueLength == 0) {
                value = "";
            } else {
                value = args[6];
                if (value.length() != valueLength) {
                    return Reply.error("value长度与请求参数中的长度不符");
                }
            }
        } else {
            return Reply.error("请求格式不符合set规范");
        }
        cache.set(getKey(args), value);
        return Reply.simpleString(OK);
    }
}
