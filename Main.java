import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

/**
 * Program converts a file into a base64 string and then outputs it to a text file
 * You can then read this textfile and decode it from base64 String into a file
 */
public class Main {

    private static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        mainMenu();
    }

    /**
     * Display the main menu and handles actions
     */
    public static void mainMenu(){
        while (true)
        {
            System.out.printf("Menu\n-------\n%s\n%s\n%s\n\n%s",
                    "1) encode",
                    "2) decode",
                    "3) exit",
                    "Enter \"1\",  \"2\", or \"3\":");
            char choice = scanner.nextLine().charAt(0);
            switch (choice){
                case '1':
                    encode();
                case '2':
                    decode();
                case '3':
                    System.exit(0);
                default:
                    System.out.println("\"" + choice + "\"" + " is not an option, please try again");
            }
        }
    }

    /**
     *
     */
    private static void encode() {
        banner("Encode");

        //read uncrypted file
        System.out.println("\nEnter the path to the file you want to encode");
        String fileName = scanner.nextLine();
        String clearText = convertFileToB64String(fileName);
        String cipherText = encrypt(clearText);

        //write cipher text to file
        System.out.println("Enter the path to the text file you'd like to save to (we add .txt automatically)");
        String cipherTextFilePath = scanner.nextLine();
        writeTextToTextFile(cipherTextFilePath,cipherText);

    }

    /**
     * Writes string to a text file
     * @param fileName
     * @param text
     */
    private static void writeTextToTextFile(String fileName, String text){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(fileName + ".txt");
        } catch (FileNotFoundException e) {
            System.out.println("unable to write cipher text of length: " + text.length() + " to " + fileName + ".txt");
            e.printStackTrace();
            System.exit(0);
        }
        pw.write(text);
        pw.close();
    }

    /**
     * Opens a file and converts it's it into a base64 string
     * @param filePath
     * @return
     */
    public static String convertFileToB64String(String filePath){
        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            String b64 = Base64.getEncoder().encodeToString(data);
            return b64;
        } catch (IOException e) {
            System.out.println("Unable to convert " + filePath + " to base64 string");
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    /**
     * Utility to display a banner message to the console
     * @param bannerText
     */
    private static void banner(String bannerText) {
        for(int i = 0; i < bannerText.length() + 4; i++)
            System.out.print("#");
        System.out.println();
        for(int i = 0; i < bannerText.length() + 4; i++) {
            if(i == 0 || i == bannerText.length() + 4 - 1)
                System.out.print("#");
            else
                System.out.print(" ");
        }
        System.out.println();
        System.out.println("# " + bannerText + " #");
        for(int i = 0; i < bannerText.length() + 4; i++) {
            if(i == 0 || i == bannerText.length() + 4 - 1)
                System.out.print("#");
            else
                System.out.print(" ");
        }
        System.out.println();
        for(int i = 0; i < bannerText.length() + 4; i++)
            System.out.print("#");
        System.out.println();
    }

    /**
     * TODO: break up into multiple methods
     */
    private static void decode(){
        banner("Decode");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to file you would like to read the encoded text from");
        String payload = null;
        try {
            Scanner reader = new Scanner(new File(scanner.nextLine()));
            payload = reader.next();
            payload = decrypt(payload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] bytes = Base64.getDecoder().decode(payload);
        FileOutputStream fos = null;
        try {
            System.out.println("Enter path to file you would like to decode to");
            fos = new FileOutputStream(scanner.nextLine());
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //TODO later on refine encryption instead of just taking some code from stack,
    //TODO eventually read encryptionKey from file
    private static final String encryptionKey = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";


    /**
     * Method for Encrypt Plain String Data
     *
     * @param plainText
     * @return encryptedText
     */
    public static String encrypt(String plainText) {
        String encryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            System.err.println("Encrypt Exception : " + E.getMessage());
        }
        return encryptedText;
    }

    /**
     * Method For Get encryptedText and Decrypted provided String
     *
     * @param encryptedText
     * @return decryptedText
     */
    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : " + E.getMessage());
        }
        return decryptedText;
    }
}
