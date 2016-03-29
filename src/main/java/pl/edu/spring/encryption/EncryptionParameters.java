package pl.edu.spring.encryption;

/**
 * Created by Pawel on 2016-03-19.
 */
public class EncryptionParameters {
    private EncryptionAlgorithms algorithm;
    private String key;

    public EncryptionParameters(EncryptionAlgorithms algorithm, String key) {
        this.algorithm = algorithm;
        this.key = key;
    }

    public EncryptionAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(EncryptionAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
