package com.silencecorner.io.streams;

import com.silencecorner.io.CreateFile;

import java.io.*;

public class ByteStreams {
    /**
     * @Decription 文件字节输入流
     * @date 2016年10月14日 下午8:06:17
     */
    public int fileOutputStreamTest() {
        int len = 0;
        try {
            //创建文件对象
            File file = CreateFile.byteSourceFile();
            //创建输出流对象
            FileOutputStream out = new FileOutputStream(file);
            //输出内容
            String str = "this is a file output stream";
            //写入文件--byte[]数组
            out.write(str.getBytes());
            len = str.getBytes().length;
            //关闭输出流
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }

    /**
     * @Decription 文件字节输出流
     * @date 2016年10月14日 下午8:06:27
     */
    public void fileInputStreamTest() {
        //先写入文件
        int len = fileOutputStreamTest();
        //创建文件对象
        File file = CreateFile.byteSourceFile();
        try {
            //创建文件输入流对象
            FileInputStream in = new FileInputStream(file);
            //用一个byte数组存储读取到的数据
            byte[] byteArr = new byte[len];
            //索引变量
            int n = 0;
            //将输入读取到byte数组中
            while ((n = in.read(byteArr, 0, len)) != -1) {
                //字符串读
                System.out.println(new String(byteArr, 0, n));
            }
            //输入流关闭
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Decription 字节数组输出流
     * @date 2016年10月14日 下午8:34:33
     */
    public byte[] byteArrayOutputStreamTest() {
        //定义一个字符串常量
        String str = "this is a byte array output stream";
        //ByteArrayOutputStream(int size)设置缓冲区大小
        //默认ByteArrayOutputStream()初始化一个32byte缓冲区,在无参构造方法中是使用this(32)来调用的ByteArrayOutputStream(int size
        //设置一个16字节的缓冲区，这是20字节的字符串
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(16);
        //byte数组返回变量，用来复制缓冲区的数据
        byte[] back = null;
        try {
            //write(int b)将b转换成byte插入到最后一个元素之后
            //write(byte[] b,int off,int len)从数组b索引为值为off写入长度为len的元素到缓冲区,当输入的字节数大于缓冲区时，缓冲区容量自动增加。
            byteOutput.write(str.getBytes());
            //输出写入缓冲区的byte数组
            System.out.println(byteOutput.toString());
            //返回缓冲区的byte数组
            back = byteOutput.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return back;
    }

    /**
     * @Decription 字节数组输入流
     * @date 2016年10月14日 下午8:34:33
     */
    public void byteArrayInputStreamTest() {
        //获取已在内存中的数组
        byte[] outByte = byteArrayOutputStreamTest();
        //指定输入流的源
        ByteArrayInputStream byteIn = new ByteArrayInputStream(outByte);
        //定义一个数组
        byte[] inbyte = new byte[outByte.length];
        try {
            int n = 0;
            //读完最后一个字节或抛出异常结束read方法体
            //执行一次执行一次read()方法,pos++,所以如果使用read()判断下一个位置是否有数据就读取不到第一个数据
            while ((n = byteIn.read(inbyte, 0, outByte.length)) != -1) {
                //打印数组内容,注意只打印已经读到的内容。
                System.out.println(new String(inbyte, 0, n));
            }
        } finally {
            try {
                byteIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
