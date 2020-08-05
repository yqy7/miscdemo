package com.github.yqy7.miscdemo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author qiyun.yqy
 * @date 2020/8/4
 */
public class CheckExternalSort {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./temp/output.txt"));
        String pre = null, cur;
        long start = System.currentTimeMillis();
        while ((cur = bufferedReader.readLine()) != null) {
            if (pre == null) {
                pre = cur;
                continue;
            } else if (pre.compareTo(cur) > 0){
                System.out.println("发现有问题的行: ");
                System.out.println("pre = " + pre);
                System.out.println("cur = " + cur);
            } else {
                pre = cur;
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + "ms");
    }
}
