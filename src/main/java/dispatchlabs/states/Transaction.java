package dispatchlabs.states;

import dispatchlabs.crypto.Crypto;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;

/**
 * Transaction
 */
public class Transaction extends AJson {

    private static final long serialVersionUID = -6784018212068358301L;

    public static class Type {

        /**
         * Class level-declarations.
         */
        public final static byte TRANSFER_TOKENS = 0;
        public final static byte SET_NAME = 1;
        public final static byte SMART_CONTRACT = 2;
    }

    /**
     * Class level-declarations.
     */
    private String hash; // Hash = (Type + From + To + Value + Code + Method + Time)
    private byte type;
    private String from;
    private String to;
    private String value;
    private String code;
    private String method;
    private String params;
    private long time;
    private String signature;
    private String hertz;
    // Transients
    private String fromName;
    private String toName;

    /**
     *
     * @return
     */
    public String getHash() {
        return hash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     *
     * @return
     */
    public byte getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *
     * @return
     */
    public String getSignature() {
        return signature;
    }

    /**
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     *
     * @return
     */
    public String getParams() {
        return params;
    }

    /**
     *
     * @param params
     */
    public void setParams(String params) {
        this.params = params;
    }

    public String getHertz() {
        return hertz;
    }

    public void setHertz(String hertz) {
        this.hertz = hertz;
    }


    /**
     *
     * @return
     */
    public String getFromName() {
        return fromName;
    }

    /**
     *
     * @param fromName
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     *
     * @return
     */
    public String getToName() {
        return toName;
    }

    /**
     *
     * @param toName
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     *
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param code
     * @param method
     * @param params
     * @param time
     * @return
     * @throws Exception
     */
    public static Transaction create(String privateKey, String from, String to, byte type, String value, String code, String method, String params, long time) throws Exception {
        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKey);
        byte[] typeBytes = {type};
        byte[] fromBytes = DatatypeConverter.parseHexBinary(from);
        byte[] toBytes = DatatypeConverter.parseHexBinary(to);
        byte[] valueBytes = value.getBytes();
        byte[] codeBytes = DatatypeConverter.parseHexBinary(code);
        byte[] methodBytes = method.getBytes();
        byte[] paramsBytes = params.getBytes();
        byte[] timeBytes = Utils.longToBytes(time);

        // Hash bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
        byteArrayOutputStream.write(typeBytes);
        byteArrayOutputStream.write(fromBytes);
        byteArrayOutputStream.write(toBytes);
        byteArrayOutputStream.write(valueBytes);
        byteArrayOutputStream.write(codeBytes);
        byteArrayOutputStream.write(methodBytes);
        byteArrayOutputStream.write(paramsBytes);
        byteArrayOutputStream.write(timeBytes);
        byte[] hashBytes = Crypto.hash(byteArrayOutputStream.toByteArray());
        byte[] signatureBytes = Crypto.sign(privateKeyBytes, hashBytes);

        // Create transaction.
        Transaction transaction = new Transaction();
        transaction.setHash(Utils.toHexString(hashBytes));
        transaction.setType(type);
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setValue(value);
        transaction.setTime(time);
        transaction.setCode(code);
        transaction.setMethod(method);
        transaction.setParams(params);
        transaction.setHertz("0");
        transaction.setSignature(Utils.toHexString(signatureBytes));

        return transaction;
    }
}
