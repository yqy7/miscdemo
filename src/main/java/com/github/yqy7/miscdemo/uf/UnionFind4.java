package com.github.yqy7.miscdemo.uf;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
// weight-quick-union 的另外一种写法，按高度求并
public class UnionFind4 {
    int[] ids;
    int[] rank;
    int count;

    public UnionFind4(int count) {
        this.count = count;
        ids = new int[count];
        rank = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = i;
            rank[i] = 1;
        }
    }

    public int find(int a) {
        while (a != ids[a]) {
            ids[a] = ids[ids[a]]; // 路径压缩
            a = ids[a];
        }
        return a;
    }

    public void union(int a, int b) {
        int p = find(a);
        int q = find(b);
        if (p == q) {
            return;
        }
        if (rank[p] == rank[q]) {
            ids[p] = q;
            rank[q]++;
        } else if (rank[p] > rank[q]) {
            ids[q] = p;
        } else {
            ids[p] = q;
        }
        count--;
    }

    public static void main(String[] args) {
        UnionFind4 unionFind4 = new UnionFind4(8);
        unionFind4.union(1,4);
        unionFind4.union(2,5);
        unionFind4.union(3,6);
        System.out.println(Arrays.toString(unionFind4.ids));
        System.out.println(Arrays.toString(unionFind4.rank));
        unionFind4.union(2,3);
        System.out.println(unionFind4.find(5));
        System.out.println(unionFind4.find(2));
        System.out.println(Arrays.toString(unionFind4.ids));
        System.out.println(Arrays.toString(unionFind4.rank));
    }
}
