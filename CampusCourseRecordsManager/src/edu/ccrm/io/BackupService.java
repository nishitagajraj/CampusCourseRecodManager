package edu.ccrm.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*Handles the creation of timestamped backups of the application data.
 */
public class BackupService {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path BACKUP_DIRECTORY = Paths.get("backups");

    public void createBackup() {
        // Use Date/Time API for the timestamped folder name
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path destinationDir = BACKUP_DIRECTORY.resolve("backup_" + timestamp);

        try {
            if (!Files.exists(DATA_DIRECTORY)) {
                System.out.println("Data directory does not exist. Nothing to back up.");
                return;
            }

            Files.createDirectories(destinationDir);

            // Use Files.walk to go through all files in the data directory
            Files.walk(DATA_DIRECTORY)
                .filter(Files::isRegularFile)
                .forEach(sourcePath -> {
                    try {
                        Path destinationPath = destinationDir.resolve(DATA_DIRECTORY.relativize(sourcePath));
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("Failed to copy file to backup: " + sourcePath);
                    }
                });

            System.out.println("Backup created successfully at: " + destinationDir);

        } catch (IOException e) {
            System.err.println("Could not create backup: " + e.getMessage());
        }
    }
}
