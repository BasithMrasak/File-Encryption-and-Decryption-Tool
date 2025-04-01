import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static void encryptFile(File inputFile, File outputFile, String password) throws Exception {
        SecretKey secretKey = deriveKeyFromPassword(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = inputStream.readAllBytes();
        byte[] outputBytes = cipher.doFinal(inputBytes);
        inputStream.close();

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);
        outputStream.close();
    }

    private static SecretKey deriveKeyFromPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(password.getBytes(StandardCharsets.UTF_8));
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }
}
