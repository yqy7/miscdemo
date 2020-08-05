package com.github.yqy7.miscdemo;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author qiyun.yqy
 * @date 2020/8/5
 */
public class WinnerTree<T> {
    int[] nodes;
    T[] leaves;
    Comparator<T> comparator;

    public WinnerTree(T[] leaves, Comparator<T> comparator) {
        this.leaves = leaves;
        this.comparator = comparator;
        this.nodes = new int[leaves.length - 1]; // 胜者树总是有兄弟结点

        // init
        Arrays.fill(nodes, -1);
        for (int i = 2 * nodes.length; i >= 2 ; i -= 2) {
            int right = i;
            int left = i - 1;
            int parent = left / 2;
            nodes[parent] = comparator.compare(leaves[leafIndex(left)], leaves[leafIndex(right)]) < 0
                ? leafIndex(left) : leafIndex(right);
        }
    }

    private int leafIndex(int i) {
        if (i >= nodes.length) {
            return i - nodes.length;
        } else {
            return nodes[i];
        }
    }

    public void adjust(int i) { // 赢者树，每次需要比较兄弟结点
        int s = i + nodes.length;
        while (true) {
            int right = (s & 1) == 0 ? s : s + 1;
            int left = (s & 1) == 0 ? s - 1 : s;
            int parent = left / 2;
            nodes[parent] = comparator.compare(leaves[leafIndex(left)], leaves[leafIndex(right)]) < 0
                ? leafIndex(left) : leafIndex(right);
            s = parent;
            if (s == 0) {
                break;
            }
        }
    }

    public int getWinnerIndex() {
        return nodes[0];
    }

    public static void main(String[] args) {
        String[] strings = new String[] {"bbb", "aaa", "kkk", "mmm", "ccc", "ddd", "fff", "eee", "ggg", "hhh"};
        WinnerTree<String> winnerTree = new WinnerTree<>(strings, Comparator.nullsLast(Comparator.reverseOrder())); // 反序排列

        for (int i = 0; i < strings.length; i++) {
            int winnerIndex = winnerTree.getWinnerIndex();
            System.out.println(strings[winnerIndex]);
            winnerTree.leaves[winnerIndex] = null;
            winnerTree.adjust(winnerIndex);
        }
    }
}
