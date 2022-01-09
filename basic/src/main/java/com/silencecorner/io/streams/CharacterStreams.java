package com.silencecorner.io.streams;

import com.silencecorner.io.CreateFile;

import java.io.*;
import java.util.Arrays;

public class CharacterStreams {
    /**
     * @Description 文件字符输入流
     * @date 2016年10月16日 下午2:34:12
     */
    public void fileReaderTest() {
        File file = CreateFile.charSourceFile();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            //设置一个可存储1M大小的char数组
            char[] accept = new char[512];
            int n = 0;
            while ((n = reader.read(accept, 0, accept.length)) != -1) {
                System.out.println(new String(accept, 0, n));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description 文件字符输出流
     * @date 2016年10月16日 下午2:34:12
     */
    public void fileWriterTest() {
        File file = CreateFile.charSourceFile();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write("这是一个文件字符输出流");
            //对于writer流，write方法会首先将数据写入缓冲区，每当缓冲区溢出是，缓冲区的内容才会被写入到目的地
            //调用flush方法或者关闭当前流都会立刻将数据写入目的地
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * @Description 字符数组输入流测试
     * @date 2016年10月16日 上午11:14:52
     */
    public void charArrayReaderTest() {
        //定义一个字符数组
        char[] charArr = charArrayWriterTest();
        //指定字符数组输入流的源（已经在内存分配具体空间）
        CharArrayReader reader = new CharArrayReader(charArr);
        //定义一个接收的字符数组
        char[] acceptArr = new char[charArr.length];
        try {
            //读取内容
            while (reader.read(acceptArr, 0, charArr.length) != -1) {
                //打印目的数组
                System.out.println("打印目的数组" + Arrays.toString(acceptArr));

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            reader.close();
        }


    }

    /**
     * @return
     * @Description 字符数组输出流
     * @date 2016年10月16日 下午2:35:33
     */
    public char[] charArrayWriterTest() {
        String str = "这是一个字符数组流";
        //定义一个源字符数组
        char[] charArr = str.toCharArray();
        //初始化一个5个字符大小的缓冲区
        CharArrayWriter writer = new CharArrayWriter(5);
        //用来复制缓冲区的数据
        char[] inputData = null;
        try {
            //写入缓冲区
            writer.write(charArr);
            //获得缓冲区中的数据
            inputData = writer.toCharArray();
            System.out.println("缓冲区中的数组：" + Arrays.toString(inputData));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        return inputData;

    }

    /**
     * @Description inputStreamReader是字节流到字符流桥梁，将字节流按照参数字符集解码读取出来
     * 较为常见的是与BufferReader一起使用
     * @date 2016年10月17日 下午7:43:06
     */
    public void inputStreamReaderTest() {
        File file = CreateFile.charSourceFile();
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            while (reader.readLine() != null) {
                System.out.println(reader.readLine());
            }
            //关闭流
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description OutputStreamWriter是字符流到字节流的桥梁，
     * 将字符流按照参数的字符编码写入文件中
     * @date 2016年10月17日 下午7:46:42
     */
    public void outputStreamWriterTest() {
        File file = CreateFile.charSourceFile();
        BufferedWriter writer = null;
        try {
            //编码和解码的字符集不一样会乱码，gb2312与GBK是兼容的
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "gb2312");
            writer = new BufferedWriter(osw);
            for (int i = 0; i < 50; i++) {
                writer.write("这是一个OutputStreamWriter");
                //插入换行符
                writer.newLine();
            }
            //刷新文件
            writer.flush();
            //关闭流
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
