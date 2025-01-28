package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MkdirCommand {
    private Path currentDirectory;

    public MkdirCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: mkdir <directory> [<directory> ...]");
            return;
        }

        for (int i = 1; i < parts.length; ++i) {
            String dirName = parts[i].trim();
            if (dirName.isEmpty()) {
                System.err.println("mkdir: directory name cannot be empty.");
                continue;
            }

            Path newDir = this.currentDirectory.resolve(dirName);

            // Check if the directory already exists
            if (Files.exists(newDir)) {
                System.err.println("mkdir: directory '" + dirName + "' already exists.");
                continue;
            }

            try {
                // Create the directory and its parents if needed
                Files.createDirectories(newDir);
                System.out.println("Directory created: " + dirName); // Changed to print just the directory name
            } catch (IOException e) {
                System.err.println("mkdir: cannot create directory '" + dirName + "': " + e.getMessage());
            }
        }
    }
}
