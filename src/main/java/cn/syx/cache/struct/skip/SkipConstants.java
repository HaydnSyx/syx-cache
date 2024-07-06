package cn.syx.cache.struct.skip;

import java.util.concurrent.ThreadLocalRandom;

public class SkipConstants {

    // 让每个节点的索引最多16个
    public static final int MAX_LEVEL  = 16;

    // 在当前跳表中，用到了多少层索引
    private int curMaxLevel = 1;

    // 跳表中节点数
    private int size = 0;

    // 头结点
    private SkipNode<?, ?> head = new SkipNode<>(MAX_LEVEL);

    // 每个节点默认有一层索引，每增加一层索引的概率是1/2
    private int randomLevel(){
        int level = 1;
        while ((ThreadLocalRandom.current().nextInt() & 1) == 1 && level < MAX_LEVEL){
            level ++;
        }
        return level;
    }
}
