package com.silencecorner.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CreateFile {
    private static final String byteSourceFile = "byteSourceFile.txt";
    private static final String charSourceFile = "charSourceFile.txt";
    private static final String dataFile = "dataFile.txt";
    public static File byteSourceFile() {
        File file = new File(byteSourceFile);
        if (file.exists()){
           return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File charSourceFile() {
        File file = new File(charSourceFile);
        if (file.exists()){
            return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File dataFile() {
        File file = new File(dataFile);
        if (file.exists()){
            return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
