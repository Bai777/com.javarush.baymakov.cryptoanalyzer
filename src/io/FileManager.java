package io;

import cipher.CaesarCipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {

    private static final String[] SYSTEM_DIRECTORIES = {
            "C:\\Windows", "C:\\Program Files", "C:\\Program Files (x86)",
            "/usr", "/bin", "/sbin", "/etc", "/var", "/sys", "/proc"
    };


    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public void writeFile(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        checkWritePermissions(path);
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    public void encryptFile(String inputPath, String outputPath, CaesarCipher cipher, int key) throws IOException {
        processFileWithCipher(inputPath, outputPath, cipher, key, true);
    }

    public void decryptFile(String inputPath, String outputPath, CaesarCipher cipher, int key) throws IOException {
        processFileWithCipher(inputPath, outputPath, cipher, key, false);
    }


    private void processFileWithCipher(String inputPath, String outputPath,
                                       CaesarCipher cipher, int key, boolean encrypt) throws IOException {
        Path input = Paths.get(inputPath);
        Path output;
        if (Paths.get(outputPath).getParent() == null) {
            Path parentDir = input.getParent();
            if (parentDir != null) {
                output = parentDir.resolve(outputPath);
            } else {
                output = Paths.get(outputPath);
            }
        } else {
            output = Paths.get(outputPath);
            checkWritePermissions(output);
            Files.createDirectories(output.getParent());
        }

        try(BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)){

            String line;
            while ((line = reader.readLine()) != null){
                String processedLine;
                if (encrypt){
                    processedLine = cipher.encrypt(line, key);
                }
                else {
                    processedLine = cipher.decrypt(line, key);
                }

                writer.write(processedLine);
                writer.newLine();
            }
        }
    }

    private void checkWritePermissions(Path path) throws IOException {
        Path parentDir = path.getParent();
        if (parentDir == null) {
            return;
        }

        String absolutePath = parentDir.toAbsolutePath().toString().toLowerCase();


        for (String sysDir : SYSTEM_DIRECTORIES) {
            if (absolutePath.startsWith(sysDir.toLowerCase())) {
                throw new IOException("Запрещено создавать файлы в системных директориях: " + sysDir);
            }
        }


        if (Files.exists(parentDir) && !Files.isWritable(parentDir)) {
            throw new IOException("Нет прав на запись в директорию: " + parentDir);
        }


        if (isRootDirectory(parentDir)) {
            throw new IOException("Запрещено создавать файлы в корневой директории диска");
        }
    }


    private boolean isRootDirectory(Path path) {
        Path absolutePath = path.toAbsolutePath();
        Path root = absolutePath.getRoot();

        if (root != null) {
            return absolutePath.equals(root);
        }

        return false;
    }


    public String getSafeWorkingDirectory() {
        String userHome = System.getProperty("user.home");
        String workingDir = System.getProperty("user.dir");

        try {
            Path workingPath = Paths.get(workingDir);
            checkWritePermissions(workingPath);
            return workingDir;
        } catch (IOException e) {
            return userHome;
        }
    }


    public String createSafeOutputPath(String inputPath, String outputFileName) throws IOException {
        Path input = Paths.get(inputPath);
        Path parentDir = input.getParent();

        if (parentDir != null) {
            try {
                checkWritePermissions(parentDir);
                return parentDir.resolve(outputFileName).toString();
            } catch (IOException e) {
                throw new IOException("Невозможно создать файл в безопасной директории: " +
                        parentDir + ". Причина: " + e.getMessage(), e);
            }
        }

        String safeDir = getSafeWorkingDirectory();
        return Paths.get(safeDir, outputFileName).toString();
    }

    public void processFile(String inputPath, String outputPath,
                            FileProcessor processor) throws IOException {
        Path input = Paths.get(inputPath);
        Path output = Paths.get(outputPath);

        checkWritePermissions(output);

        if (output.getParent() != null) {
            Files.createDirectories(output.getParent());
        }

        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = processor.processLine(line);
                writer.write(processedLine);
                writer.newLine();
            }
        }
    }

    public List<String> readFileAsLines(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }


    public void writeFileFromLines(List<String> lines, String filePath) throws IOException {
        Path path = Paths.get(filePath);

        checkWritePermissions(path);

        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}
