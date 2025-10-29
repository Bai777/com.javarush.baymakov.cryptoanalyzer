import analyzer.BruteForceAnalyzer;
import analyzer.StatisticalAnalyzer;
import cipher.Alphabet;
import cipher.CaesarCipher;
import io.FileManager;
import validation.InputValidator;
import static constants.Constants.*;

import java.util.Scanner;


public class Main {
    private final CaesarCipher cipher;
    private final FileManager fileManager;
    private final InputValidator validator;
    private final BruteForceAnalyzer bruteForceAnalyzer;
    private final StatisticalAnalyzer statisticalAnalyzer;
    private final Scanner scanner;

    public Main() {
        Alphabet alphabet = Alphabet.russianAlphabet();
        this.cipher = new CaesarCipher(alphabet);
        this.fileManager = new FileManager();
        this.validator = new InputValidator();
        this.bruteForceAnalyzer = new BruteForceAnalyzer(cipher);
        this.statisticalAnalyzer = new StatisticalAnalyzer(cipher,
                StatisticalAnalyzer.russianFrequency());
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readIntInput(ACTION);

            try {
                switch (choice) {
                    case 1 -> encrypt();
                    case 2 -> decryptWithKey();
                    case 3 -> bruteForce();
                    case 4 -> statisticalAnalysis();
                    case 0 -> {
                        System.out.println(QUIT);
                        return;
                    }
                    default -> System.out.println(WRONG_CHOICE);
                }
            } catch (Exception e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println(TITLE);
        System.out.println(ONE_POSITION_MENU);
        System.out.println(TWO_POSITION_MENU);
        System.out.println(THREE_POSITION_MENU);
        System.out.println(FOUR_POSITION_MENU);
        System.out.println(ZERO_POSITION_MENU);
    }

    private void encrypt() throws Exception {
        String inputFile = readStringInput(INPUT_FILE_PATH);
        String outputFile = readStringInput(OUTPUT_FILE_PATH);
        int key = readIntInput(INPUT_KEY);

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);
        validator.validateKey(key);

        String text = fileManager.readFile(inputFile);
        String encrypted = cipher.encrypt(text, key);
        fileManager.writeFile(encrypted, outputFile);

        System.out.println(INFO_FROM_STATUS_ENCRYPT_FILE);
    }

    private void decryptWithKey() throws Exception {
        String inputFile = readStringInput(PATH_FROM_ENCRYPT_FILE);
        String outputFile = readStringInput(PATH_FROM_DECRYPT_FILE);
        int key = readIntInput(KEY_DECRYPT);

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);
        validator.validateKey(key);

        String text = fileManager.readFile(inputFile);
        String decrypted = cipher.decrypt(text, key);
        fileManager.writeFile(decrypted, outputFile);

        System.out.println(INFO_FROM_STATUS_DECRYPT_FILE);
    }

    private void bruteForce() throws Exception {
        String inputFile = readStringInput(BRUT_FORCE_INPUT_FILE_PATH);
        String outputFile = readStringInput(BRUT_FORCE_OUTPUT_FILE_PATH);

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);

        String text = fileManager.readFile(inputFile);
        BruteForceAnalyzer.BruteForceResult result = bruteForceAnalyzer.decrypt(text);

        fileManager.writeFile(result.getDecryptedText(), outputFile);

        System.out.printf("Найден ключ: %d (уверенность: %.2f)%n",
                result.getKey(), result.getConfidence());
        System.out.println(INFO_FROM_STATUS_DECRYPT_FILE);
    }

    private void statisticalAnalysis() throws Exception {
        String inputFile = readStringInput(BRUT_FORCE_INPUT_FILE_PATH);
        String outputFile = readStringInput(BRUT_FORCE_OUTPUT_FILE_PATH);

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);

        String text = fileManager.readFile(inputFile);
        StatisticalAnalyzer.StatisticalResult result = statisticalAnalyzer.decrypt(text);

        fileManager.writeFile(result.getDecryptedText(), outputFile);

        System.out.printf("Найден ключ: %d (расстояние: %.4f)%n",
                result.getKey(), result.getConfidence());
        System.out.println(INFO_FROM_STATUS_DECRYPT_FILE);
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(INFO_FROM_USER);
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
