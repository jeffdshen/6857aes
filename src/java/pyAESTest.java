import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Quick check to see that AES.py works
 */
public class pyAESTest {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        String algorithm = "AES/CBC/NoPadding";
        String cleartext = "0123456789012345";

        Key key = new SecretKeySpec(new byte[]{
            (byte) 143, (byte) 194,34, (byte) 208, (byte) 145, (byte) 203, (byte) 230, (byte) 143, (byte) 177,
            (byte) 246, 97, (byte) 206, (byte) 145, 92, (byte) 255, 84
        }, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[] {
            103, 35, (byte) 148, (byte) 239, 76, (byte) 213, 47, 118, (byte) 255, (byte) 222,
            123, (byte) 176, 106, (byte) 134, 98, 92
        });
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] bytes = cipher.doFinal(cleartext.getBytes());
        for (byte b : bytes) {
            System.out.print((b & 0xFF) + ",");
        }
        System.out.println();
    }
}
