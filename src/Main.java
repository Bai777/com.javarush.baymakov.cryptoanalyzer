import analyzer.BruteForceAnalyzer;
import analyzer.StatisticalAnalyzer;
import cipher.Alphabet;
import cipher.CaesarCipher;
import io.FileManager;
import validation.InputValidator;

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
            int choice = readIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1 -> encrypt();
                    case 2 -> decryptWithKey();
                    case 3 -> bruteForce();
                    case 4 -> statisticalAnalysis();
                    case 0 -> {
                        System.out.println("Выход из программы.");
                        return;
                    }
                    default -> System.out.println("Неверный выбор.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Шифр Цезаря ===");
        System.out.println("1. Зашифровать текст");
        System.out.println("2. Расшифровать текст с ключом");
        System.out.println("3. Взлом brute force");
        System.out.println("4. Статистический анализ");
        System.out.println("0. Выход");
    }

    private void encrypt() throws Exception {
        String inputFile = readStringInput("Введите путь к исходному файлу: ");
        String outputFile = readStringInput("Введите путь для зашифрованного файла: ");
        int key = readIntInput("Введите ключ шифрования: ");

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);
        validator.validateKey(key, cipher.getAlphabet());

        String text = fileManager.readFile(inputFile);
        String encrypted = cipher.encrypt(text, key);
        fileManager.writeFile(encrypted, outputFile);

        System.out.println("Файл успешно зашифрован.");
    }

    private void decryptWithKey() throws Exception {
        String inputFile = readStringInput("Введите путь к зашифрованному файлу: ");
        String outputFile = readStringInput("Введите путь для расшифрованного файла: ");
        int key = readIntInput("Введите ключ дешифровки: ");

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);
        validator.validateKey(key, cipher.getAlphabet());

        String text = fileManager.readFile(inputFile);
        String decrypted = cipher.decrypt(text, key);
        fileManager.writeFile(decrypted, outputFile);

        System.out.println("Файл успешно расшифрован.");
    }

    private void bruteForce() throws Exception {
        String inputFile = readStringInput("Введите путь к зашифрованному файлу: ");
        String outputFile = readStringInput("Введите путь для расшифрованного файла: ");

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);

        String text = fileManager.readFile(inputFile);
        BruteForceAnalyzer.BruteForceResult result = bruteForceAnalyzer.decrypt(text);

        fileManager.writeFile(result.getDecryptedText(), outputFile);

        System.out.printf("Найден ключ: %d (уверенность: %.2f)%n",
                result.getKey(), result.getConfidence());
        System.out.println("Файл успешно расшифрован.");
    }

    private void statisticalAnalysis() throws Exception {
        String inputFile = readStringInput("Введите путь к зашифрованному файлу: ");
        String outputFile = readStringInput("Введите путь для расшифрованного файла: ");

        validator.validateFileExists(inputFile);
        validator.validateOutputPath(outputFile);

        String text = fileManager.readFile(inputFile);
        StatisticalAnalyzer.StatisticalResult result = statisticalAnalyzer.decrypt(text);

        fileManager.writeFile(result.getDecryptedText(), outputFile);

        System.out.printf("Найден ключ: %d (расстояние: %.4f)%n",
                result.getKey(), result.getConfidence());
        System.out.println("Файл успешно расшифрован.");
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
                System.out.println("Пожалуйста, введите целое число.");
            }
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
