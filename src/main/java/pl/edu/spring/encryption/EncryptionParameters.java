package pl.edu.spring.encryption;

import java.io.File;

/**
 * Created by Pawel on 2016-03-19.
 */
public class EncryptionParameters {
    private EncryptionAlgorithms algorithm;
    private String stringKey;
    private String initVector;
    private File fileKey;

    public EncryptionParameters(EncryptionAlgorithms algorithm, String stringKey, String initVector, File fileKey) {
        this.algorithm = algorithm;
        this.stringKey = stringKey;
        this.initVector = initVector;
        this.fileKey = fileKey;
    }

    public EncryptionParameters(EncryptionAlgorithms algorithm, String stringKey, String initVector) {
        this.algorithm = algorithm;
        this.stringKey = stringKey;
        this.initVector = initVector;
    }

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

    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }
}
