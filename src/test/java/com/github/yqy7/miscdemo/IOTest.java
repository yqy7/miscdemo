package com.github.yqy7.miscdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

/**
 * @author qiyun.yqy
 * @date 2020/8/13
 */
public class IOTest {
    @Test
    void test1() throws Exception { // 8s 165ms
        long start = System.currentTimeMillis();

        Path path = Paths.get("temp/myInputFile.txt");

        File outputFile = new File("temp/m_test1.txt");
        outputFile.deleteOnExit();

        Files.copy(path, outputFile.toPath());

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
    }

    @Test
    void test2() throws Exception {
        long start = System.currentTimeMillis();
        File file = new File("temp/myInputFile.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        File outputFile = new File("temp/m_test2.txt");
        outputFile.deleteOnExit();

        Files.copy(fileInputStream, outputFile.toPath());
        fileInputStream.close();

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
    }

    @Test
    void test3() throws Exception {
        long start = System.currentTimeMillis();
        int writeCount = 0;

        File file = new File("temp/myInputFile.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        File outputFile = new File("temp/m_test3.txt");
        outputFile.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        byte[] bytes = new byte[8 * 1024 * 1024];
        int num = 0;
        while ((num = fileInputStream.read(bytes)) > 0) {
            writeCount++;
            fileOutputStream.write(bytes, 0 , num);
        }

        fileInputStream.close();
        fileOutputStream.close();

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
        System.out.println("writeCount = " + writeCount);
    }

    @Test
    void test4() throws Exception { // 这个可能最快
        long start = System.currentTimeMillis();
        File file = new File("temp/myInputFile.txt");
        File outputFile = new File("temp/m_test4.txt");
        outputFile.deleteOnExit();

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        FileChannel outputFileChannel = FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE,
            StandardOpenOption.CREATE);
        fileChannel.transferTo(0, file.length(), outputFileChannel);

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
    }

    @Test
    void test5() throws Exception {
        long start = System.currentTimeMillis();
        int writeCount = 0;

        File file = new File("temp/myInputFile.txt");
        File outputFile = new File("temp/m_test5.txt");
        outputFile.deleteOnExit();

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        FileChannel outputFileChannel = FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE,
            StandardOpenOption.CREATE);

        ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024 * 1024);
        while (fileChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                writeCount++;
                outputFileChannel.write(byteBuffer);
            }
            byteBuffer.clear();
        }

        fileChannel.close();
        outputFileChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
        System.out.println("writeCount = " + writeCount);
    }

    @Test
    void test6() throws Exception {
        long start = System.currentTimeMillis();
        int writeCount = 0;

        File file = new File("temp/myInputFile.txt");
        File outputFile = new File("temp/m_test6.txt");
        outputFile.deleteOnExit();

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        FileChannel outputFileChannel = FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE,
            StandardOpenOption.CREATE);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8 * 1024 * 1024);
        while (fileChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                writeCount++;
                outputFileChannel.write(byteBuffer);
            }
            byteBuffer.clear();
        }

        fileChannel.close();
        outputFileChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
        System.out.println("writeCount = " + writeCount);
    }

    @Test
    void test7() throws Exception { // 10456ms，比较慢
        long start = System.currentTimeMillis();
        int writeCount = 0;

        File file = new File("temp/myInputFile.txt");
        File outputFile = new File("temp/m_test7.txt");
        outputFile.deleteOnExit();

        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        FileChannel outputFileChannel = FileChannel.open(outputFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ,
            StandardOpenOption.CREATE);

        int pos = 0;
        int len = 8 * 1024 * 1024;
        MappedByteBuffer byteBuffer = outputFileChannel.map(MapMode.READ_WRITE, pos, len);

        while (fileChannel.read(byteBuffer) > 0) {
            writeCount++;
            if (byteBuffer.position() == byteBuffer.capacity()) {
                pos += len;
                byteBuffer = outputFileChannel.map(MapMode.READ_WRITE, pos, len);
            }
        }

        fileChannel.close();
        outputFileChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end - start) + "ms");
        System.out.println("writeCount = " + writeCount);
        System.out.println("File size: " + outputFile.length());
    }

/*
cost: 11706ms


cost: 10804ms


cost: 6252ms
writeCount = 248


cost: 4477ms


cost: 5024ms
writeCount = 248


cost: 4465ms
writeCount = 248


cost: 9208ms
writeCount = 248
File size: 2080374784
 */
}
