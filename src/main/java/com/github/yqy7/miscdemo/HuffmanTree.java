package com.github.yqy7.miscdemo;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author qiyun.yqy
 * @date 2020/6/18
 * 哈夫曼树：最优二叉树
 * 带权路径长度(WPL)
 * 结点的带权路径长度：从根结点到该结点的路径长度 * 结点的权
 * 树的带权路径长度：树的所有叶子结点的带权路径长度和
 */
public class HuffmanTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "TreeNode{" +
                "val=" + val +
                ", left=" + left +
                ", right=" + right +
                '}';
        }
    }

    /**
     * 从数组构建哈夫曼树
     * @param arr
     * @return
     */
    public static TreeNode build(int[] arr) {
        // 用优先队列替代每次的排序
        Queue<TreeNode> nodes = new PriorityQueue<>(Comparator.comparingInt(o -> o.val));
        for (int i = 0; i < arr.length; i++) {
            nodes.offer(new TreeNode(arr[i]));
        }

        while (nodes.size() > 1) {
            TreeNode node1 = nodes.poll();
            TreeNode node2 = nodes.poll();
            TreeNode root = new TreeNode(node1.val + node2.val);
            root.left = node1;
            root.right = node2;
            nodes.offer(root);
        }

        return nodes.poll();
    }

    public static void main(String[] args) {
        TreeNode treeNode = build(new int[] {13, 7, 8, 3, 29, 6, 1});
        System.out.println(treeNode);
    }
}
