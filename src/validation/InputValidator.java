package validation;

import io.ValidationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputValidator {

    public void validateFileExists(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)){
            throw new ValidationException("Файл не существует: " + filePath);
        }
        if (!Files.isReadable(path)){
            throw new ValidationException("Файл не читается: " + filePath);
        }
    }

    public void validateOutputPath(String filePath) {
        try {
            Paths.get(filePath);
        } catch (Exception e) {
            throw new ValidationException("Неверный путь вывода: " + filePath);
        }
    }

    public void validateKey(int key) {
        if (key < 0){
            throw new ValidationException("Ключ не может быть отрицательным значением!");
        }
    }
}
