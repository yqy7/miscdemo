package com.github.yqy7.miscdemo;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author qiyun.yqy
 * @date 2020/8/5
 */
public class LoserTree<T> {
    int[] nodes;
    T[] leaves;
    Comparator<T> comparator;

    public LoserTree(T[] leaves, Comparator<T> comparator) {
        this.leaves = leaves;
        this.comparator = comparator;
        this.nodes = new int[leaves.length];
        Arrays.fill(this.nodes, -1);

        for (int i = leaves.length - 1; i >= 0; i--) {
            adjust(i);
        }
    }

    public void adjust(int s) {
        int t = (s + nodes.length) / 2; // 父结点
        while (t > 0) {
            if (nodes[t] < 0) { // 初始的时候，遇到-1就停下来，-1索引代表一个不存在但是最小的值
                nodes[t] = s;
                return;
            } else if (comparator.compare(leaves[nodes[t]], leaves[s]) < 0) { // 每次只需要比较父结点
                int temp = nodes[t];
                nodes[t] = s;
                s = temp;
            }
            t = t / 2;
        }

        nodes[0] = s;
    }

    public int getLoserIndex() {
        return nodes[0];
    }

    public static void main(String[] args) {
        String[] strings = new String[] {"bbb", "aaa", "kkk", "mmm", "ccc", "ddd", "fff", "eee", "ggg", "hhh"};
        LoserTree<String> loserTree = new LoserTree<>(strings, Comparator.nullsLast(Comparator.naturalOrder()));

        for (int i = 0; i < strings.length; i++) {
            int loserIndex = loserTree.getLoserIndex();
            System.out.println(strings[loserIndex]);
            loserTree.leaves[loserIndex] = null; // 删除
            loserTree.adjust(loserIndex);
        }
    }
}
