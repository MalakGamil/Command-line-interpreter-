package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public class TouchCommand {
    private Path currentDirectory;

    public TouchCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: touch <file> [<file> ...]");
        } else {
            for (int i = 1; i < parts.length; ++i) {
                Path filePath = this.currentDirectory.resolve(parts[i].trim());

                try {
                    if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
                        // Check if the file is writable before setting the modified time
                        if (Files.isWritable(filePath)) {
                            Files.setLastModifiedTime(filePath, FileTime.fromMillis(System.currentTimeMillis()));
                        } else {
                            System.err.println("touch: cannot modify '" + parts[i] + "': Permission denied");
                        }
                    } else {
                        Files.createFile(filePath);
                        System.out.println("File created: " + filePath.getFileName());
                    }
                } catch (IOException e) {
                    System.err.println("touch: cannot create file '" + parts[i] + "': " + e.getMessage());
                }
            }
        }
    }
}