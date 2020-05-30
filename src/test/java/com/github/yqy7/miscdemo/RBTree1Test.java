package com.github.yqy7.miscdemo;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

class RBTree1Test {

    @Test
    void test1() {
        RBTree1 rbTree = new RBTree1();
        rbTree.put("abc", 123);
        rbTree.put("aaa", 123);
        rbTree.put("bbb", 123);
        rbTree.put("acd", 123);

        rbTree.remove("bbb");
        rbTree.put("ccc", 222);

        System.out.println(rbTree);
    }

    @Test
    void test2() {
        RBTree1 rbTree = new RBTree1(Comparator.reverseOrder());
        rbTree.put("abc", 123);
        rbTree.put("aaa", 123);
        rbTree.put("bbb", 123);
        rbTree.put("acd", 123);

        rbTree.remove("bbb");
        rbTree.put("ccc", 222);

        System.out.println(rbTree);
    }
}