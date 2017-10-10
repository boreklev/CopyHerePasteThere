package se.boreklev.copyherepastethere;

/**
 * Created by matti on 2017-08-28.
 */

public class SendTaskParameters {
    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    private int mType;
    private String mString;
    private byte[] mByteArray;

    public SendTaskParameters(int t, String s) {
        mType = t;
        mString = s;
    }

    public SendTaskParameters(int t, byte[] b, String s) {
        mType = t;
        mByteArray = b;
        mString = s;
    }

    public int getType() { return mType; }

    public String getTypeAsString() {
        switch(mType) {
            case IMAGE:
                return "image";
            default:
                return "text";
        }
    }

    public byte[] getBytes() {
        return mByteArray;
    }

    public String getString() {
        mString.replaceAll("\\\\n", "\\r\\n");
        return mString;
    }
}
