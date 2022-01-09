package com.silencecorner.io.streams;

import com.silencecorner.io.Book;
import com.silencecorner.io.BookExternalizable;
import com.silencecorner.io.CreateFile;

import java.io.*;

public class ObjectStreams {

    public void objectOutputStreamTest() {
        //将文件对象写入文件中
        File file = CreateFile.dataFile();
        Book book = new Book("金瓶梅", "兰陵笑笑生", 22.00, false,"黄书");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(book);
            System.out.println("对象输出流写入成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void objectInputStreamTest() {
        File file = CreateFile.dataFile();
        try {
            //将对象写入文件中
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Book book = (Book) ois.readObject();
            System.out.println("name:" + book.getName());
            System.out.println("realName:" + book.getRealName());
            System.out.println("author:" + book.getAuthor());
            System.out.println("price:" + book.getPrice());
            System.out.println("isBorrowed:" + book.isBorrowed());
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void objectOutputStreamExternalizableTest() {
        //将文件对象写入文件中
        BookExternalizable book = new BookExternalizable("金瓶梅", "兰陵笑笑生");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("BookExternalizable.txt"))) {
            oos.writeObject(book);
            System.out.println("对象输出流写入成功！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void objectInputStreamExternalizableTest() {
        try {
            //将对象写入文件中
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("BookExternalizable.txt"));
            BookExternalizable book = (BookExternalizable) ois.readObject();
            System.out.println("name:" + book.getName());
            System.out.println("author:" + book.getAuthor());
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Decription 使用数组和对象流实现对象克隆
     * @date 2016年10月17日 下午11:29:37
     */
    public void objectClone() {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(byteOut);) {
            //定义一个book对象
            Book book = new Book("金品梅", "兰陵笑笑生", 39.50, false,"黄书");
            //先将对象写入内存中,相当于拷贝了一份
            oos.writeObject(book);
            //从内存中读取出来转换成Book对象
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(byteIn)) {
                Book bookClone = (Book) ois.readObject();
                //不重写equals方法会使用java.lang.Object默认的equals方法(直接判断两个对象的地址值是否相等，显然不符合这里我们的逻辑),
                //所以Book重写equals方法,生成的hashcode方法不允许设置transient字段参与equals比较，所以自己加一个
                if (bookClone.equals(book)) {
                    if (bookClone != book) {
                        System.out.println("克隆成功");
                    } else {
                        System.out.println("克隆失败");
                    }
                } else {
                    System.out.println("克隆失败");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
