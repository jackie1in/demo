package com.silencecorner.io;

import java.io.*;

public class Book implements Serializable  {
    private static final long serialVersionUID = 1L;
    // 被transient修饰的成员不会被序列化，使用对象流要保证对象序列化，也要保证对象的成员被序列化
    private transient String name;
    private String author;
    private boolean isBorrowed;
    private double price;
    private String realName;

    public Book() {

    }

    public Book(String name, String author, double price, boolean isBorrowed, String realName) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.isBorrowed = isBorrowed;
        this.realName = realName;
    }

    /**
     * @Description 自定义反序列话name字段
     * @date 2016年10月17日 下午8:19:43
     */
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        // 反序列化name
        this.name = s.readUTF();
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        // 把jvm能序列化的成员序列化
        s.defaultWriteObject();
        // 虚拟化name
        s.writeUTF(this.name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        //Generate Hashcode() and equals() 不允许设置transient字段参与hashcode的计算，所以自己加一个
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((realName == null) ? 0 : realName.hashCode());
        result = prime * result + (isBorrowed ? 1231 : 1237);
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        //Generate Hashcode() and equals() 不允许设置transient字段参与equals比较，所以自己加一个
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (realName == null) {
            if (other.realName != null)
                return false;
        } else if (!realName.equals(other.realName)) {
            return false;
        }
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (isBorrowed != other.isBorrowed)
            return false;
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}