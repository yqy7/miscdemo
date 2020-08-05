package com.github.yqy7.miscdemo.uf;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
// weight-quick-union，按大小求并
public class UnionFind3 {
    int[] ids;
    int[] sz; // 记录结点数
    int count;

    public UnionFind3(int count) {
        this.count = count;
        ids = new int[count];
        sz = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = i;
            sz[i] = 1;
        }
    }

    public int find(int a) {
        while (ids[a] != a) {
            ids[a] = ids[ids[a]]; // 路径压缩，减半
            a = ids[a];
        }
        return ids[a];
    }

    public void union(int a, int b) {
        int p = find(a);
        int q = find(b);
        if (p == q) {
            return;
        }

        if (sz[p] > sz[q]) {
            ids[q] = p; // 将小树根变成大树的根
            sz[p] += sz[q];
        } else {
            ids[p] = q;
            sz[q] += sz[p];
        }
        count--;
    }

    public static void main(String[] args) {
        UnionFind3 unionFind3 = new UnionFind3(8);
        unionFind3.union(1,4);
        unionFind3.union(2,5);
        unionFind3.union(3,6);
        System.out.println(Arrays.toString(unionFind3.ids));
        System.out.println(Arrays.toString(unionFind3.sz));
        unionFind3.union(2,3);
        System.out.println(unionFind3.find(5));
        System.out.println(unionFind3.find(2));
        System.out.println(Arrays.toString(unionFind3.ids));
        System.out.println(Arrays.toString(unionFind3.sz));
    }
}
