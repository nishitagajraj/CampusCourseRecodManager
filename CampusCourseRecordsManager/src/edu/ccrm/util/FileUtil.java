package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

//A utility class for file-related operations.

public class FileUtil {

    // @return The total size of the directory in bytes.
    public static long calculateDirectorySize(Path path) {
        long size = 0;
        if (!Files.isDirectory(path)) {
            try {
                return Files.size(path);
            } catch (IOException e) {
                System.err.println("Cannot get size for file: " + path);
                return 0;
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    size += calculateDirectorySize(entry); // Recursive call for sub-directory
                } else {
                    size += Files.size(entry); // Add file size
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading directory: " + path + " - " + e.getMessage());
        }
        return size;
    }
}