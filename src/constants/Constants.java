package constants;

public interface Constants {
    String ACTION = "Выберите действие: ";
    String QUIT = "Выход из программы.";
    String WRONG_CHOICE = "Неверный выбор.";
    String ERROR = "Ошибка: ";
    String TITLE =  "\n=== Шифр Цезаря ===";
    String ONE_POSITION_MENU = "1. Зашифровать текст";
    String TWO_POSITION_MENU = "2. Расшифровать текст с ключом";
    String THREE_POSITION_MENU = "3. Взлом brute force";
    String FOUR_POSITION_MENU = "4. Статистический анализ";
    String ZERO_POSITION_MENU = "0. Выход";
    String INPUT_FILE_PATH = "Введите путь к исходному файлу: ";
    String OUTPUT_FILE_PATH = "Введите путь для зашифрованного файла: ";
    String INPUT_KEY = "Введите ключ шифрования: ";
    String INFO_FROM_STATUS_ENCRYPT_FILE = "Файл успешно зашифрован.";
    String INFO_FROM_STATUS_DECRYPT_FILE = "Файл успешно расшифрован.";
    String PATH_FROM_ENCRYPT_FILE = "Введите путь к зашифрованному файлу: ";
    String PATH_FROM_DECRYPT_FILE = "Введите путь для расшифрованного файла: ";
    String KEY_DECRYPT = "Введите ключ дешифровки: ";
    String BRUT_FORCE_INPUT_FILE_PATH = "Введите путь к зашифрованному файлу: ";
    String BRUT_FORCE_OUTPUT_FILE_PATH = "Введите путь для расшифрованного файла: ";
    String INFO_FROM_USER = "Пожалуйста, введите целое число.";
}
