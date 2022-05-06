package com.silencecorner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JavaElementaryKnowledge {
    Object[] s;
    Object ss;
    public Object[] getS() {
        return s;
    }

    public void setS(Object[] s) {
        this.s = s;
    }

    public Object getSs() {
        return ss;
    }

    public void setSs(Object ss) {
        this.ss = ss;
    }

    // try-with-resource
    public static void main(String[] args) {
        String[] s = new String[]{"数组转换测试"};
        JavaElementaryKnowledge knowledge = new JavaElementaryKnowledge();
        knowledge.setS(s);
        knowledge.setSs("单个对象转换");
        // String sss = (String) knowledge.getSs();
        // String[] ss = (String[])knowledge.getS();
        knowledge.setS(Arrays.asList("List to array","testing").toArray());
        // String[] ss1 = (String[])knowledge.getS();
        knowledge.setS(new ArrayList<String>(){{
            add("array list to array");
            add("testing");
        }}.toArray());
        try {
            String[] ss3 = (String[])knowledge.getS();
            System.out.println(ss3);
        }catch (ClassCastException e){
            System.out.println("deep copy不能使用强制转换");
        }

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(ClassLoader.getSystemResource("test.txt").getFile());
            // System.exit(1);
            // return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try (FileInputStream fis = new FileInputStream("xxxx")) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // error和exception 举例子发现系统发生一个异常代码查错的过程
        // } catch (FileNotFoundException | IOException e) {
        // } catch (IOException e) {
        /*} catch (IOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("xxxxdfesfewfewfewfwe");
            e.printStackTrace();
        } */
    }
}
