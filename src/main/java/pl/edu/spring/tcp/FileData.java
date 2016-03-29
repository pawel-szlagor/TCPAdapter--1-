package pl.edu.spring.tcp;

import java.io.Serializable;

/**
 * Created by Pawel on 2016-03-12.
 */
public class FileData implements Serializable{
   private byte[] byteArray;
    private String fileName;

    public FileData(byte[] byteArray, String fileName) {
        this.byteArray = byteArray;
        this.fileName = fileName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
