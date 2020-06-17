package com.github.yqy7.miscdemo;

import java.util.Arrays;

public class QSort {
    public void sort(int[] arr, int start, int end) {
        if (start < end) {
            int p = partition(arr, start, end);
            sort(arr, start, p); // hoare分区，p并不是基准元素在新数组中的位置
            //sort(arr, start, p - 1) ; // lomuto分区，p是基准元素在新数组中的位置，因此下一次可以不包括它
            sort(arr, p + 1, end);
        }
    }

    private int partition(int[] arr, int start, int end) {
        int pivot = arr[start]; // hoare分区，总是以第一个为基准
        int i = start - 1;
        int j = end + 1;
        while (true) {
            do { j--; } while (arr[j] > pivot);
            do { i++; } while (arr[i] < pivot);

            if (i < j) {
                swap(arr, i, j);
            } else {
                return j;
            }
        }

        //int pivot = arr[end]; // lomuto分区，总是以最后一个为基准
        //int i = start;
        //for (int j = start; j < end ; j++) {
        //    if (arr[j] <= pivot) {
        //        swap(arr, i, j);
        //        i++;
        //    }
        //}
        //swap(arr, i, end);
        //return i;
    }

    private void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {
        QSort qSort = new QSort();
        int[] arr = new int[] {3, 4, 5, 1, 2, 7, 8, 4};
        qSort.sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
