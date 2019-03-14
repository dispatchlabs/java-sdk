package dispatchlabs.crypto;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import org.web3j.crypto.*;

/**
 *
 */
public class Crypto {

    /**
     *
     * @param privateKey
     * @param hash
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] privateKey, byte[] hash) throws Exception {
        BigInteger privKey = new BigInteger(privateKey);
        BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
        ECKeyPair keyPair = new ECKeyPair(privKey, pubKey);
        Sign.SignatureData signature = Sign.signMessage(hash, keyPair, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(signature.getR());
        byteArrayOutputStream.write(signature.getS());
        byteArrayOutputStream.write(new byte[] {(byte)(signature.getV() - 27)});
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * @param bytes
     * @return
     */
    public static byte[] hash(byte[] bytes) {
        return Hash.sha3(bytes);
    }
}
