package pl.edu.spring.encryption;

/**
 * Created by Pawel on 2016-03-19.
 */
public enum EncryptionAlgorithms {
    NO_ALGORITHM("no algorithm"),
    CAESAR("Caesar algorithm"),
    ROT_13("ROT 13"),
    VIGENERE("Vigenère'a"),
    AES ("AES");

    private final String desc;

    EncryptionAlgorithms(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
