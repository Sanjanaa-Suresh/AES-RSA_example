import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

public class AES_RSA {
    public static void main(String[] args) throws Exception {
        // Initialize RSA
        RSA rsa = new RSA();
        rsa.initFromStrings();

        // Get input string
        String inputString = "Hello, RSA and AES encryption!";

        // Encode with RSA
        String rsaEncrypted = rsa.encrypt(inputString);

        // Encode with AES
        String aesEncrypted = encryptWithAES(rsaEncrypted);

        // Decrypt with AES
        String decryptedWithAES = decryptWithAES(aesEncrypted);

        // Decrypt with RSA
        String decryptedWithRSA = rsa.decrypt(decryptedWithAES);

        // Output results
        System.out.println("Input String: " + inputString);
        System.out.println("RSA Encrypted: " + rsaEncrypted);
        System.out.println("AES Encrypted: " + aesEncrypted);
        System.out.println("Decrypted with AES: " + decryptedWithAES);
        System.out.println("Decrypted with RSA: " + decryptedWithRSA);
    }

    public static String encryptWithAES(String input) throws Exception {
        // Generate AES key
        PrivateKey aesKey = (PrivateKey) generateAESKey();

        // Encrypt with AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = aesCipher.doFinal(input.getBytes());

        // Encode encrypted bytes
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptWithAES(String input) throws Exception {
        // Decode input
        byte[] encryptedBytes = Base64.getDecoder().decode(input);

        // Decrypt with AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, generateAESKey());
        byte[] decryptedBytes = aesCipher.doFinal(encryptedBytes);

        // Convert decrypted bytes to string
        return new String(decryptedBytes);
    }

    public static Key generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }
}
