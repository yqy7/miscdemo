package com.github.yqy7.miscdemo;

import java.util.Arrays;

/**
 * @author qiyun.yqy
 * @date 2020/6/18
 */
public class KMP {
    public static int kmp(String text, String dest) {
        int[] next = kmpNext(dest);
        for (int i = 0, j = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != dest.charAt(j)) {
                j = next[j - 1];
            }

            if (text.charAt(i) == dest.charAt(j)) {
                j++;
            }

            if (j == dest.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }

    private static int[] kmpNext(String dest) {
        int[] next = new int[dest.length()];
        for (int j = 0, i = 1; i < dest.length(); i++) {
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j - 1]; // 回退
            }

            if (dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(kmpNext("abcbabcd")));
        System.out.println(kmp("abcbabcd", "cba"));
        System.out.println(kmp("abcbabcd", "cbc"));
    }
}
