package com.github.yqy7.miscdemo.uf;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
// weight-quick-union 的另外一种写法，按高度求并，单个数组保存结点
public class UnionFind5 {
    int[] ids;
    int count;

    public UnionFind5(int count) {
        this.count = count;
        ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = -1;
        }
    }

    public int find(int a) {
        //while (ids[a] >= 0) { // 如果需要路径压缩，可以在后面再加一次循环来实现。用循环可以防止爆栈
        //    a = ids[a];
        //}
        //return a;
        if (ids[a] < 0) {
            return a; // 返回的是索引，而不是高度
        } else {
            return ids[a] = find(ids[a]); // 路径压缩，用递归方便路径压缩
        }
    }

    public void union(int a, int b) {
        int p = find(a);
        int q = find(b);
        if (p == q) {
            return;
        }
        if (ids[p] == ids[q]) {
            ids[p] = q;
            ids[q]--; // 高度减1
        } else if (ids[p] < ids[q]) {
            ids[q] = p;
        } else {
            ids[p] = q;
        }
        count--;
    }

    public static void main(String[] args) {
        UnionFind5 unionFind5 = new UnionFind5(8);
        unionFind5.union(1,4);
        unionFind5.union(2,5);
        unionFind5.union(3,6);
        System.out.println(Arrays.toString(unionFind5.ids));
        unionFind5.union(2,3);
        System.out.println(unionFind5.find(5));
        System.out.println(unionFind5.find(2));
        System.out.println(Arrays.toString(unionFind5.ids));
    }
}
