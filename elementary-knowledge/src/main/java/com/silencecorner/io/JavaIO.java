package com.silencecorner.io;

import com.silencecorner.io.streams.DataStreams;
import com.silencecorner.io.streams.ObjectStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaIO {
    /**
     * @Description 随机访问
     * 随机流指向文件不刷新文件
     * @date 2016年10月16日 下午4:45:03
     */
    public void randomAccessFileTest() {
        File file = CreateFile.dataFile();
        //参数mode的有r读、rw读写,至于rws和rwd模式还不知怎么去用
        try(RandomAccessFile inAndOut = new RandomAccessFile(file, "rw");) {

            String str = "这是一个随机流"; //7x2 = 14 byte
            //也可以使用str.getBytes()来获取str的byte数组表示，这里实验时粘贴过来的
            byte[] byteArr = new byte[]{-113, -39, 102, 47, 78, 0, 78, 42, -106, -113, 103, 58, 109, 65};
            //写500个字符串进去
            for (int i = 0; i < 500; i++) {
                inAndOut.write(byteArr);
            }
            StringBuilder buf = new StringBuilder();
            //读取循环次数500 * 7，一个字符串是7个char字符串组成
            for (int i = 0; i < 500 * 7; i++) {
                //读取位置
                inAndOut.seek(i * 2);
                //每个字符串开始插入一个提示
                if (i % 7 == 0) {
                    buf.append("\n第" + i + "个字符串：");
                }
                buf.append(inAndOut.readChar());
            }
            System.out.println(buf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ObjectStreams objectStreams = new ObjectStreams();
        System.out.println("----- 对象流测试开始 ------");
        objectStreams.objectOutputStreamTest();
        objectStreams.objectInputStreamTest();
        objectStreams.objectClone();
        System.out.println("----- Externalizable测试 ------");
        objectStreams.objectOutputStreamExternalizableTest();
        objectStreams.objectInputStreamExternalizableTest();
        System.out.println("----- 对象流测试结束 ------");

        DataStreams dataStreams = new DataStreams();
        System.out.println("-----数据流测试开始 ------");
        dataStreams.dataOutputStreamTest();
        dataStreams.dataInputStreamTest();
        System.out.println("-----数据流测试结束 ------");
        File file = new File("fewf");
        file.deleteOnExit();
        Path path = Files.createTempFile(null,"x.txt");
        Files.delete(path);
    }
}
