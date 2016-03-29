package pl.edu.spring.encryption;

import java.io.File;

/**
 * Created by Pawel on 2016-03-19.
 */
public class EncryptionParameters {
    private EncryptionAlgorithms algorithm;
    private String stringKey;
    private File fileKey;

    public EncryptionParameters(EncryptionAlgorithms algorithm, String key) {
        this.algorithm = algorithm;
        this.stringKey = key;
    }

    public EncryptionParameters(EncryptionAlgorithms algorithm, File fileKey) {
        this.algorithm = algorithm;
        this.fileKey = fileKey;
    }

    public EncryptionAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(EncryptionAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public String getStringKey() {
        return stringKey;
    }

    public void setStringKey(String stringKey) {
        this.stringKey = stringKey;
    }

    public File getFileKey() {
        return fileKey;
    }

    public void setFileKey(File fileKey) {
        this.fileKey = fileKey;
    }
}
