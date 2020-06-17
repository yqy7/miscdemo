package com.github.yqy7.miscdemo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author qiyun.yqy
 * @date 2020/6/13
 */
public class GenerateData {
    public static void main(String[] args) throws IOException {
        final int MAX=80;
        File f=new File("./myInputFile.txt");
        if (f.exists())
            f.delete();
        BufferedWriter bufw=new BufferedWriter(new FileWriter(f));
        for (int i=0;i<MAX;++i){
            bufw.write(getRandomString());
            bufw.newLine();
        }
        bufw.flush();
        bufw.close();
    }
    public static String getRandomString(){
        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        for (int i = 0; i < 8; i++) {
            sb.append((char)(random.nextInt(26)+97));

        }
        sb.append(',');
        for (int i = 0; i <16 ; i++) {
            sb.append((char)(random.nextInt(26)+97));
        }

        return sb.toString();
    }
}
