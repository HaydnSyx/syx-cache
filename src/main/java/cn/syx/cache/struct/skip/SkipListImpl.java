package cn.syx.cache.struct.skip;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class SkipListImpl<K extends Comparable<K>, T> implements SkipList<K, T> {

    // 让每个节点的索引最多16个
    public static final int MAX_LEVEL = 16;

    // 在当前跳表中，用到了多少层索引
    private int curMaxLevel = 1;

    // 跳表中节点数
    private int size = 0;

    // 头结点
    private SkipNode<K, T> head = new SkipNode<>(MAX_LEVEL);

    // 随机函数，给每个节点生成层数做随机处理
    @Override
    public void add(K key, T value) {

        // 为每一个节点生成一个随机的层数
        int level = randomLevel();

        // 防止极端情况：第一节点的层数是1，第二个节点层数直接来个16，跨度太大
        // 所以当新生成的节点层数大于当前skip list中最大的层数时，让当前层数 = skip list中最大的层数 + 1
        if (level > curMaxLevel) level = ++curMaxLevel;

        // 生成新节点
        SkipNode<K, T> newNode = new SkipNode<>(key, value, level);

        // 从头结点开始
        SkipNode<K, T> p = head;

        // 新插入的节点只会影响当前节点level个“链表”
        // todo 拆分出来讲这个循环
        // i -- 控制层数下移
        for (int i = level - 1; i >= 0; i--) {
            // 目的是找到每层比目标值 略小 的那个节点
            while (p.next[i] != null && p.next[i].key.compareTo(key) < 0) {
                p = p.next[i];
            }
            // 往每层链表插入节点的过程
            newNode.next[i] = p.next[i];
            p.next[i] = newNode;
        }
        // 维护一下插入的节点数
        size++;
    }

    @Override
    public T remove(K key) {
        SkipNode<K, T> p = head;
        boolean flag = false;
        // 从当前skip list的最高层开始往下遍历
        for (int i = curMaxLevel - 1; i >= 0; i--) {
            // 这个循环和前面插入的是一样的
            while (p.next[i] != null && p.next[i].key.compareTo(key) < 0) {
                p = p.next[i];
            }
            // 1、存在第一个不比目标值小的节点
            // 2、判断这个节点的值是否等于目标值
            if (p.next[i] != null && p.next[i].key.compareTo(key) == 0) {
                // “链表” 断链操作
                p.next[i] = p.next[i].next[i];
                // 只有执行了断链操作，维护的size才能 --
                flag = true;
            }
        }
        // 维护节点数
        if (flag) size--;

        return null;
    }

    @Override
    public void set(K key, T value) {
        SkipNode<K, T> node;
        if ((node = getNode(key)) != null) {
            node.data = value;
        } else {
            // 否则新建节点
            add(key, value);
        }
    }

    @Override
    public T get(K key) {
        SkipNode<K, T> node = getNode(key);
        return node == null ? null : (T) node.data;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void clear() {
        head = new SkipNode<>(MAX_LEVEL);
        size = 0;
        curMaxLevel = 1;
    }

    private SkipNode<K, T> getNode(K key) {
        SkipNode<K, T> p = head;
        for (int i = curMaxLevel - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].key.compareTo(key) < 0) {
                p = p.next[i];
            }
            // 不为null，并且相等，立马返回
            if (p.next[i] != null && p.next[i].key.compareTo(key) == 0) {
                return p.next[i];
            }
        }
        return null;
    }

    // 每个节点默认有一层索引，每增加一层索引的概率是1/2
    private int randomLevel() {
        int level = 1;
        while ((ThreadLocalRandom.current().nextInt() & 1) == 1 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    public Iterator iterator() {
        return new Iterator() {

            SkipNode<K, T> p = head.next[0];

            int s = getSize();

            @Override
            public boolean hasNext() {
                return s != 0;
            }

            @Override
            public Object next() {
                s--;
                SkipNode<K, T> tmp = p;
                p = p.next[0];
                return tmp;
            }
        };
    }

    public Iterator iteratorForSet() {
        return new Iterator() {

            SkipNode<K, T> p = head.next[0];

            int s = getSize();

            @Override
            public boolean hasNext() {
                return s != 0;
            }

            @Override
            public Object next() {
                s--;
                SkipNode<K, T> tmp = p;
                p = p.next[0];
                return tmp.key;
            }
        };
    }
}