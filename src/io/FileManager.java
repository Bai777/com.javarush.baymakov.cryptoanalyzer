package io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
