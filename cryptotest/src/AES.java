import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * KEY_SIZE values : 128, 192 and 256
 * T_LEN values : 128, 120, 112, 104 and 96
 */

public class AES {
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int T_LEN = 96;
    private Cipher encryptionCipher;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, encryptionCipher.getIV());
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {
        try {
            AES aes = new AES();
            aes.init();
            Scanner myObj = new Scanner(System.in);
//            StringBuilder jsonStringBuilder = new StringBuilder();
//            while (myObj.hasNextLine()) {
//                String line = myObj.nextLine();
//                if (line.trim().isEmpty()) {
//                    break; // Break if an empty line is entered
//                }
//                jsonStringBuilder.append(line).append("\n");
//            }
//
//            String jsonString = jsonStringBuilder.toString().trim();

            // Get parameters from the user
            System.out.println("Enter Max Assets:");
            int maxAssets = Integer.parseInt(myObj.nextLine());
            String encryptMaxAssets = aes.encrypt(String.valueOf(maxAssets));

            System.out.println("Enter Max Devices:");
            int maxDevices = Integer.parseInt(myObj.nextLine());
            String encryptMaxDevices = aes.encrypt(String.valueOf(maxDevices));

            System.out.println("Enter Max Instances:");
            int maxInstances = Integer.parseInt(myObj.nextLine());
            String encryptMaxInstances = aes.encrypt(String.valueOf(maxInstances));

            System.out.println("Enter Expiry Date (YYYY-MM-DD):");
            String expiryDate = myObj.nextLine();
            String encryptExpiryDate = aes.encrypt(expiryDate);

            System.out.println("Enter Start Date (YYYY-MM-DD):");
            String startDate = myObj.nextLine();
            String encryptStartDate = aes.encrypt(startDate);

            // Construct the structured data
//            String structuredData = String.format("%04d%04d%04d%-10s%-10s", maxAssets, maxDevices, maxInstances, expiryDate, startDate);
//
//            String encryptedMessage = aes.encrypt(structuredData);
//            String decryptedMessage = aes.decrypt(encryptedMessage);

//            System.err.println("Encrypted Message : " + encryptedMessage);
//            System.err.println("Decrypted Message : " + decryptedMessage);
//            String decryptedStructuredData = decryptedMessage.substring(0, 22); // Adjust the length based on your structure
//
//            System.out.println("Decrypted:\n" + decryptedMessage);
            System.out.println("Restored Parameters:");
            System.out.println("Max Assets: " + encryptMaxAssets);
            System.out.println("Max Devices: " + encryptMaxAssets);
            System.out.println("Max Instances: " + encryptMaxInstances);
            System.out.println("Expiry Date: " + encryptExpiryDate);
            System.out.println("Start Date: " + encryptStartDate);

            String finalMessage = encryptMaxAssets+encryptMaxDevices+"-"+encryptMaxInstances+"-"+encryptExpiryDate+"-"+encryptStartDate;
            System.out.println("Final encrypted Message : "+ finalMessage);
        } catch (Exception ignored) {
        }
    }
}