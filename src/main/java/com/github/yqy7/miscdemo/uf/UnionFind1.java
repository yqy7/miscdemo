package com.github.yqy7.miscdemo.uf;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
// quick-find
public class UnionFind1 {
    int[] ids;
    int count;

    public UnionFind1(int count) {
        this.count = count;
        ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = i; // 指向自己
        }
    }

    public int find(int a) {
        return ids[a];
    }

    public void union(int a, int b) { // a合并到b
        int p = find(a);
        int q = find(b);
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == p) {
                ids[i] = q;
            }
        }
        count--;
    }

    public static void main(String[] args) {
        UnionFind1 unionFind1 = new UnionFind1(8);
        unionFind1.union(1,4);
        unionFind1.union(2,5);
        unionFind1.union(3,6);
        System.out.println(Arrays.toString(unionFind1.ids));
        unionFind1.union(2,3);
        System.out.println(unionFind1.find(5));
        System.out.println(unionFind1.find(2));
        System.out.println(Arrays.toString(unionFind1.ids));
    }
}
