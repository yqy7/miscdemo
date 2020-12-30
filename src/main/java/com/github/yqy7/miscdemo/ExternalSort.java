package com.github.yqy7.miscdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
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

    // TODO 优化性能，比较 FileChannel 和 FileWriter，AsynchronousFileChannel

    public static void sort(String inputFile, String outputFile) {
        List<Node> nodeList = new ArrayList<>();
        //PrintWriter writer = null;
        FileChannel fileChannel = null;
        List<String> tmpFileList = null;
        try {
            // 拆分
            long start = System.currentTimeMillis();
            tmpFileList = splitAndSort(inputFile, outputFile);
            long end = System.currentTimeMillis();
            System.out.println("splitAndSort cost: " + (end - start) + "ms");

            // 归并

            //writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile), 8192 * 1024));
            fileChannel = FileChannel.open(Paths.get(outputFile ), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

            for (int i = 0; i < tmpFileList.size(); i++) { // 创建node
                nodeList.add(new Node(new BufferedReader(new FileReader(tmpFileList.get(i)), 8192 * 100)));
            }

            PriorityQueue<Node> queue = new PriorityQueue<>();
            for (int i = 0; i < nodeList.size(); i++) { // 初始化queue
                queue.offer(nodeList.get(i));
            }

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                //writer.println(node.line);
                ByteBuffer buffer = Charset.defaultCharset().encode(node.line + "\n");
                if (byteBuffer.capacity() - byteBuffer.position() < buffer.limit()) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) { // 必须用这种方式，write不一定一次写完
                        fileChannel.write(byteBuffer);
                    }
                    byteBuffer.clear();
                }
                byteBuffer.put(buffer);

                if (node.readNewLine() != null) {
                    queue.offer(node);
                }
            }

            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) { // 必须用这种方式，write不一定一次写完
                fileChannel.write(byteBuffer);
            }
            byteBuffer.clear();

        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            //close(writer);
            close(fileChannel);
            nodeList.forEach(node -> close(node.reader));
        }
    }

    public static List<String> splitAndSort(String inputFile, String outputFile) throws IOException {
        List<String> tmpFileList = new ArrayList<>();
        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile), 8192 * 1024)
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
                        tmpFileList.add(writeToTmpFile(rows, i, outputFile + "-" + count + ".txt"));
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

    static ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 8 * 1024);
    public static String writeToTmpFile(String[] rows, int total, String outputFileName) throws IOException {
        File outputFile = new File(outputFileName);
        outputFile.deleteOnExit(); // 退出时才删除，
        try (
            //PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile), 8192 * 1024))
            FileChannel fileChannel = FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        ){ // 关闭autoflush前 36003ms，关闭之后 10604ms
            // 排序
            long start = System.currentTimeMillis();
            Arrays.parallelSort(rows, 0, total); // 改成 parallelSort 稍快
            long end = System.currentTimeMillis();
            System.out.println(outputFileName + " sort cost: " + (end - start) + "ms");
            start = end;
            // 写入
            //ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 8 * 1024);
            for (int i = 0; i < total; i++) {
                ByteBuffer buffer = Charset.defaultCharset().encode(rows[i] + "\n");
                if (byteBuffer.capacity() - byteBuffer.position() < buffer.limit()) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) { // 必须用这种方式，write不一定一次写完
                        fileChannel.write(byteBuffer);
                    }
                    byteBuffer.clear();
                }
                //
                //byteBuffer.put(buffer);
                //writer.println(rows[i]);
            }

            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                fileChannel.write(byteBuffer);
            }
            byteBuffer.clear();

            end = System.currentTimeMillis();
            System.out.println(outputFileName + " write cost: " + (end - start) + "ms");
        }
        return outputFileName;
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

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        sort("./temp/myInputFile.txt","./temp/output.txt");
        long end = System.currentTimeMillis();
        System.out.println("total cost: " + (end - start) + "ms");

        // 8k内存：459.573s
        // 80k内存：292.114s
        // 800k内存：308.606s
    }
}
