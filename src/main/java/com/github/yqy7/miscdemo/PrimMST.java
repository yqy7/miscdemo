package com.github.yqy7.miscdemo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qiyun.yqy
 * @date 2020/6/21
 */
public class PrimMST {

    public int[][] prim(int[][] graph) {
        int sz = graph.length;
        int[][] mst = new int[sz][sz];
        //for (int[] arr : mst) {
        //    Arrays.fill(arr,Integer.MAX_VALUE); // 为了测试显示好看，返回结果0代表边不通
        //}

        Set<Integer> visited = new HashSet<>(); // 保存访问过的结点
        visited.add(0);
        while (visited.size() < sz) {
            int minWeight = Integer.MAX_VALUE;
            int h1 = -1, h2 = -1;
            for (Integer n : visited) { // 算法：从访问过的结点中，找出不包含访问过的结点的，并且权值最小的边
                for (int i = 0; i < sz; i++) {
                    if (!visited.contains(i) && graph[n][i] < minWeight) {
                        minWeight = graph[n][i];
                        h1 = n;
                        h2 = i;
                    }
                }
            }
            visited.add(h2); // 加入已访问过的结点
            mst[h1][h2] = minWeight;
        }
        return mst;
    }

    private static int INF = Integer.MAX_VALUE;
    public static void main(String[] args) {
        PrimMST primMST = new PrimMST();
        int[][] mst = primMST.prim(new int[][] { {INF,7,4,INF,INF,INF},   //INF代表两点之间不可达
            {7,INF,6,2,INF,4},
            {4,6,INF,INF,9,8},
            {INF,2,INF,INF,INF,7},
            {INF,INF,9,INF,INF,1},
            {INF,4,8,7,1,INF}
        });
        for (int i = 0; i < mst.length; i++) {
            System.out.println(Arrays.toString(mst[i]));
        }
        /* 结果
        [0, 0, 4, 0, 0, 0]
        [0, 0, 0, 2, 0, 4]
        [0, 6, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 0, 0]
        [0, 0, 0, 0, 1, 0]
         */
    }
}
