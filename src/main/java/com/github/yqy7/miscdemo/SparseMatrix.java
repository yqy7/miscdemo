package com.github.yqy7.miscdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qiyun.yqy
 * @date 2020/6/18
 *
 * 稀疏矩阵
 * 行数-列数-值数
 * 行-列-值
 */
public class SparseMatrix {
    public static int[][] compress(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("matrix 格式不正确！");
        }

        List<int[]> list = new ArrayList<>();
        list.add(new int[] {matrix.length, matrix[0].length, 0});
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    list.add(new int[] {i, j, matrix[i][j]});
                    list.get(0)[2]++;
                }
            }
        }
        int[][] res = new int[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            int[] arr = list.get(i);
            res[i][0] = arr[0];
            res[i][1] = arr[1];
            res[i][2] = arr[2];
        }
        return res;
    }

    public static int[][] uncompress(int[][] matrix) {
        if (matrix.length < 1 || matrix[0].length < 3) {
            throw new IllegalArgumentException("matrix 格式不正确！");
        }

        int[] info = matrix[0];
        int[][] res = new int[info[0]][info[1]];
        for (int i = 0; i < info[2]; i++) {
            int[] t = matrix[i + 1];
            res[t[0]][t[1]] = t[2];
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] res = SparseMatrix.compress(new int[][] {
            {1, 2, 0,0},
            {0, 0, 3,5},
            {1, 0, 4,6}});
        for (int[] arr : res) {
            System.out.println(Arrays.toString(arr));
        }
        System.out.println();

        res = SparseMatrix.uncompress(res);
        for (int[] arr : res) {
            System.out.println(Arrays.toString(arr));
        }
        System.out.println();
    }
}
