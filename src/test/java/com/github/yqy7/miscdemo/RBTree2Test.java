package com.github.yqy7.miscdemo;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RBTree2Test {

    @Test
    void test1() {
        RBTree2 rbTree = new RBTree2();
        rbTree.put("abc", 123);
        rbTree.put("aaa", 123);
        rbTree.put("bbb", 123);
        rbTree.put("acd", 123);

        rbTree.remove("bbb");
        rbTree.put("ccc", 222);

        System.out.println(rbTree);
    }

}