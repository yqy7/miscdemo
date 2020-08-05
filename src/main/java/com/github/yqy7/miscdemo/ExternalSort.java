package com.github.yqy7.miscdemo;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author qiyun.yqy
 * @date 2020/6/19
 *
 * 外部排序
 *
 * 用50M内存来实现: -Xmx50m
 * 40_0000行大概10m
 *
 * 80_0000 需要使用: -Xmx100m 否则会出现 GC overhead limit exceeded
 */
public class ExternalSort {
    public static final int CHUNK_ROWS = 80_0000;
    private static class Node implements Comparable<Node>{
        String line = null;
        BufferedReader reader;

        public Node(BufferedReader reader) throws IOException {
            this.reader = reader;
            readNewLine();
        }

        public String readNewLine() throws IOException {
            return line = reader.readLine();
        }

        @Override
        public int compareTo(Node o) {
            return this.line.compareTo(o.line);
        }
    }

    public static void sort(String inputFile, String outputFile) {
        List<Node> nodeList = new ArrayList<>();
        PrintWriter writer = null;
        List<String> tmpFileList = null;
        try {
            tmpFileList = splitAndSort(inputFile, outputFile);

            writer = new PrintWriter(new File(outputFile));

            for (int i = 0; i < tmpFileList.size(); i++) { // 创建node
                nodeList.add(new Node(new BufferedReader(new FileReader(tmpFileList.get(i)))));
            }

            PriorityQueue<Node> queue = new PriorityQueue<>();
            for (int i = 0; i < nodeList.size(); i++) { // 初始化queue
                queue.offer(nodeList.get(i));
            }

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                writer.println(node.line);
                if (node.readNewLine() != null) {
                    queue.offer(node);
                }
            }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            close(writer);
            nodeList.forEach(node -> close(node.reader));
        }

        // 清理临时文件
        if (tmpFileList != null) {
            tmpFileList.forEach(s -> deleteFile(s));
        }
    }

    public static List<String> splitAndSort(String inputFile, String outputFile) throws IOException {
        List<String> tmpFileList = new ArrayList<>();
        try(
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        ) {
            String[] rows = new String[CHUNK_ROWS];
            int i = 0;
            int count = 0;
            String line;

            while (true) {
                line = reader.readLine();
                if (line == null) {
                    if (i > 0) {
                        count++;
                        tmpFileList.add(writeToTmpFile(rows,i, outputFile + "-" + count + ".txt"));
                    }
                    break;
                }

                rows[i++] = line;

                if (i == CHUNK_ROWS) {
                    i = 0;
                    count++;
                    tmpFileList.add(writeToTmpFile(rows, CHUNK_ROWS, outputFile + "-" + count + ".txt"));
                }
            }
        }
        return tmpFileList;
    }

    public static String writeToTmpFile(String[] rows, int total, String outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))){ // 关闭autoflush前 36003ms，关闭之后 10604ms
            // 排序
            Arrays.sort(rows, 0, total); // 改成 parallelSort 也没什么变化

            // 写入
            for (int i = 0; i < total; i++) {
                writer.println(rows[i]);
            }
        }
        return outputFile;
    }

    public static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        sort("./temp/myInputFile.txt","./temp/output.txt");

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start)); // 459573 ms
    }
}
