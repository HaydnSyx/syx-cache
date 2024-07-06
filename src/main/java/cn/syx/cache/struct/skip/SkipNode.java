package cn.syx.cache.struct.skip;

import lombok.Data;

public class SkipNode<K extends Comparable<K>, T> {

    public K key;
    public T data;

    // p.next[i] 表示 p 节点的第 i 层 的下一个指向
    // 可以理解成指向N个孩子节点的引用
    public SkipNode[] next;

    public SkipNode(K key, T data,int level){
        this.key = key;
        this.data = data;
        this.next = new SkipNode[level];
    }

    public SkipNode(int level){
        this.next = new SkipNode[level];
    }
}
