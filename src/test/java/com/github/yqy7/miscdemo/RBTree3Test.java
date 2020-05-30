package com.github.yqy7.miscdemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RBTree3Test {
    @Test
    void test1() {
        RBTree3 rbTree = new RBTree3();
        rbTree.put("abc");
        rbTree.put("aaa");
        rbTree.put("bbb");
        rbTree.put("acd");

        rbTree.remove("bbb");
        rbTree.put("ccc");

        System.out.println(rbTree);
    }

}