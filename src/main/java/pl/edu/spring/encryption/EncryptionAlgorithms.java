package pl.edu.spring.encryption;

/**
 * Created by Pawel on 2016-03-19.
 */
public enum EncryptionAlgorithms {
    NO_ALGORITHM("no algorithm"),
    CAESAR("Caesar algorithm"),
    ROT_13("ROT 13");

    private final String desc;

    EncryptionAlgorithms(String position) {
        this.desc = position;
    }

    public String getposition() {
        return this.desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
