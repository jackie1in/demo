package com.silencecorner.io.streams;

import com.silencecorner.io.CreateFile;

import java.io.*;

public class BufferedStreams {
    /**
     * @Description 缓冲字节输入流
     * @date 2016年10月16日 下午2:45:44
     */
    public void bufferedInputStreamTest() {
        File file = CreateFile.byteSourceFile();
        BufferedInputStream bufIn = null;
        try {
            bufIn = new BufferedInputStream(new FileInputStream(file), 10);
            byte[] byteIn = new byte[1024];
            int n = 0;
            while ((n = bufIn.read(byteIn, 0, byteIn.length)) != -1) {
                System.out.println(new String(byteIn, 0, n));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                //只需要关闭包装的流，底层流会自动关闭;不关闭流可造成别的程序无法使用。
                bufIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description 缓冲字节输出流
     * @date 2016年10月16日 下午3:06:54
     */
    public void bufferedOutputStream() {
        File file = CreateFile.byteSourceFile();
        BufferedOutputStream bufOut = null;
        try {
            bufOut = new BufferedOutputStream(new FileOutputStream(file));
            String strOut = "这是一个缓冲字节输出流";
            bufOut.write(strOut.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description 缓冲输入流
     * @date 2016年10月16日 下午2:43:00
     */
    public void bufferedReaderTest() {
        File file = CreateFile.charSourceFile();
        BufferedReader reader = null;
        try {
            Reader fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            String strIn = null;
            while ((strIn = reader.readLine()) != null) {
                System.out.println(strIn + "\n");
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
     * @Description 缓冲输出流
     * @date 2016年10月16日 下午2:43:46
     */
    public void bufferedWriterTest() {
        File file = CreateFile.charSourceFile();
        BufferedWriter bufWriter = null;
        try {
            FileWriter fileWriter = new FileWriter(file);
            //默认8kb的缓冲区
            bufWriter = new BufferedWriter(fileWriter);
            bufWriter.write("这是第一个缓冲输出流");
            //将数据强制写入文件
            bufWriter.flush();
            //查看数据是否插入(肯定是插入了的）
            bufferedReaderTest();
            //换行
            bufWriter.newLine();
            bufWriter.write("这是第二个缓冲输出流");
            //查看数据是否插入(肯定是没有插入了的）
            bufferedReaderTest();
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bufWriter.close();
                //查看数据是否全部写入
                bufferedReaderTest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
