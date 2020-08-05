package com.github.yqy7.miscdemo.uf;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
// quick-union
public class UnionFind2 {
    int[] ids;
    int count;

    public UnionFind2(int count) {
        this.count = count;
        ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = i;
        }
    }

    public int find(int a) {
        while (ids[a] != a) {
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
        ids[p] = q;
        count--;
    }

    public static void main(String[] args) {
        UnionFind2 unionFind2 = new UnionFind2(8);
        unionFind2.union(1,4);
        unionFind2.union(2,5);
        unionFind2.union(3,6);
        System.out.println(Arrays.toString(unionFind2.ids));
        unionFind2.union(2,3);
        System.out.println(unionFind2.find(5));
        System.out.println(unionFind2.find(2));
        System.out.println(Arrays.toString(unionFind2.ids));
    }
}
