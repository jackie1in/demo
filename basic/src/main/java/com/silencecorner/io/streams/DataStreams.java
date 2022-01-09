package com.silencecorner.io.streams;

import com.silencecorner.io.CreateFile;

import java.io.*;

public class DataStreams {
    /**
     * @Description 数据输出流测试
     * @date 2016年10月17日 下午5:43:14
     */
    public void dataOutputStreamTest() {
        File file = CreateFile.byteSourceFile();
        DataOutputStream dataOut = null;
        try {
            OutputStream out = new FileOutputStream(file);
            dataOut = new DataOutputStream(out);
            int b = 252;
            dataOut.writeInt(b);
            dataOut.writeBoolean(true);
            //char字符复制使用单引号
            String str = "这是一个数据输出流";
            dataOut.writeUTF(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataOut.close();
            } catch (IOException e) {
                e.printStackTrace();


            }
        }
    }

    /**
     * @Description 数据输入流测试方法
     * @date 2016年10月17日 下午5:36:54
     */
    public void dataInputStreamTest() {

        File file = CreateFile.byteSourceFile();
        DataInputStream dataIn = null;
        try {
            InputStream in = new FileInputStream(file);
            dataIn = new DataInputStream(in);
            System.out.println(dataIn.readInt());
            System.out.println(dataIn.readBoolean());
            System.out.println(dataIn.readUTF());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
