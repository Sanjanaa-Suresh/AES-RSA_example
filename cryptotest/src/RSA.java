
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class RSA {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final String PRIVATE_KEY_STRING = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhBgzcXBm5A0srvFFu4FsBy+LLW+X0sH/9RvP40VIGOCusY0/CqA65YXWqyQE5jQCegBmnAeVYSvK+3PU4Y1fmr1uiquE6sZB5sl96T0ka+PKzPf4oKoAi6nwLUSenj5xTFjLsFGiuMXrCpMCPImf9JBVk89TJV43Xs3DSNKoj1AgMBAAECgYBsDysCgVv2ChnRH4eSZP/4zGCIBR0C4rs+6RM6U4eaf2ZuXqulBfUg2uRKIoKTX8ubk+6ZRZqYJSo3h9SBxgyuUrTehhOqmkMDo/oa9v7aUqAKw/uoaZKHlj+3p4L3EK0ZBpz8jjs/PXJc77Lk9ZKOUY+T0AW2Fz4syMaQOiETzQJBANF5q1lntAXN2TUWkzgir+H66HyyOpMu4meaSiktU8HWmKHa0tSB/v7LTfctnMjAbrcXywmb4ddixOgJLlAjEncCQQC6Enf3gfhEEgZTEz7WG9ev/M6hym4C+FhYKbDwk+PVLMVR7sBAtfPkiHVTVAqC082E1buZMzSKWHKAQzFL7o7zAkBye0VLOmLnnSWtXuYcktB+92qh46IhmEkCCA+py2zwDgEiy/3XSCh9Rc0ZXqNGD+0yQV2kpb3awc8NZR8bit9nAkBo4TgVnoCdfbtq4BIvBQqR++FMeJmBuxGwv+8n63QkGFQwVm6vCuAqFHBtQ5WZIGFbWk2fkKkwwaHogfcrYY/ZAkEAm5ibtJx/jZdPEF9VknswFTDJl9xjIfbwtUb6GDMc0KH7v+QTBW4GsHwt/gL+kGvLOLcEdLL5rau3IC7EQT0ZYg==";
    private static final String PUBLIC_KEY_STRING =  "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYQYM3FwZuQNLK7xRbuBbAcviy1vl9LB//Ubz+NFSBjgrrGNPwqgOuWF1qskBOY0AnoAZpwHlWEryvtz1OGNX5q9boqrhOrGQebJfek9JGvjysz3+KCqAIup8C1Enp4+cUxYy7BRorjF6wqTAjyJn/SQVZPPUyVeN17Nw0jSqI9QIDAQAB";

    public void init(){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public void initFromStrings(){
        try{
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(PUBLIC_KEY_STRING));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate);
        }catch (Exception ignored){}
    }


    public void printKeys(){
        System.err.println("Public key\n"+ encode(publicKey.getEncoded()));
        System.err.println("Private key\n"+ encode(privateKey.getEncoded()));
    }

    public String encrypt(String message) throws Exception {
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, "UTF8");
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.initFromStrings();
        Scanner myObj = new Scanner(System.in);
//        System.out.println("Enter a string to encrypt :'\n");
//        StringBuilder jsonStringBuilder = new StringBuilder();
//        while (myObj.hasNextLine()) {
//            String line = myObj.nextLine();
//            if (line.trim().isEmpty()) {
//                break; // Break if an empty line is entered
//            }
//            jsonStringBuilder.append(line).append("\n");
//        }
//
//        String jsonString = jsonStringBuilder.toString().trim().replace("\n", "").replace(" ", "");
//        System.err.println("String is : '\n"+jsonString);

        // Get parameters from the user
        System.out.println("Enter Max Assets:");
        int maxAssets = Integer.parseInt(myObj.nextLine());

        System.out.println("Enter Max Devices:");
        int maxDevices = Integer.parseInt(myObj.nextLine());

        System.out.println("Enter Max Instances:");
        int maxInstances = Integer.parseInt(myObj.nextLine());

        System.out.println("Enter Expiry Date (YYYY-MM-DD):");
        String expiryDate = myObj.nextLine();

        System.out.println("Enter Start Date (YYYY-MM-DD):");
        String startDate = myObj.nextLine();

        // Construct the structured data
        String structuredData = String.format("%04d%04d%04d%-10s%-10s", maxAssets, maxDevices, maxInstances, expiryDate, startDate);

        System.out.println("Enter data to encrypt (Press Enter to finish):");

        StringBuilder jsonDataBuilder = new StringBuilder();
        while (myObj.hasNextLine()) {
            String line = myObj.nextLine();
            if (line.trim().isEmpty()) {
                break; // Break if an empty line is entered
            }
            jsonDataBuilder.append(line).append("\n");
        }

        // Remove trailing newline characters
        String jsonData = jsonDataBuilder.toString().trim();

        // Combine parameters and data
        String completeData = structuredData + jsonData;
        System.out.println("Structured Data is:\n" + structuredData);

        System.out.println("String is:\n" + completeData);

        try{
            String encryptedMessage = rsa.encrypt(completeData);
            String decryptedMessage = rsa.decrypt(encryptedMessage);

            System.err.println("Encrypted:\n"+encryptedMessage);
            System.err.println("Decrypted:\n"+decryptedMessage);
            String decryptedStructuredData = decryptedMessage.substring(0, 22); // Adjust the length based on your structure

            // Extract parameters
            maxAssets = Integer.parseInt(decryptedStructuredData.substring(0, 4));
            maxDevices = Integer.parseInt(decryptedStructuredData.substring(4, 8));
            maxInstances = Integer.parseInt(decryptedStructuredData.substring(8, 12));
            expiryDate = decryptedStructuredData.substring(12, 22).trim();
            startDate = decryptedStructuredData.substring(22).trim();

            System.out.println("Decrypted:\n" + decryptedMessage);
            System.out.println("Restored Parameters:");
            System.out.println("Max Assets: " + maxAssets);
            System.out.println("Max Devices: " + maxDevices);
            System.out.println("Max Instances: " + maxInstances);
            System.out.println("Expiry Date: " + expiryDate);
            System.out.println("Start Date: " + startDate);

        }catch (Exception ingored){}



    }
}