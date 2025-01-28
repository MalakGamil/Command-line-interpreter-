package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class MvCommand {
    private final Path currentDirectory;

    public MvCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 3) {
            System.err.println("Usage: mv <source1> <source2> ... <destination>");
            return;
        }

        // Extract the last argument as the destination
        Path destination = currentDirectory.resolve(parts[parts.length - 1]);
        boolean isDestinationDirectory = Files.isDirectory(destination);

        for (int i = 1; i < parts.length - 1; i++) {
            Path source = currentDirectory.resolve(parts[i]);

            // Check if the source file exists
            if (Files.notExists(source)) {
                System.err.println("mv: cannot stat '" + parts[i] + "': No such file or directory.");
                continue;
            }

            Path targetPath = isDestinationDirectory ? destination.resolve(source.getFileName()) : destination;

            // If the target file exists and is not writable, prompt for overwrite
            if (Files.exists(targetPath) && !Files.isWritable(targetPath)) {
                if (!promptOverwrite(targetPath)) {
                    System.out.println("Skipped moving " + source.getFileName());
                    continue; // Skip this file
                }
            }

            // Attempt to move the file
            try {
                Files.move(source, targetPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                System.err.println("mv: cannot move '" + parts[i] + "' to '" + targetPath.getFileName() + "': " + e.getMessage());
            }
        }
    }

    private boolean promptOverwrite(Path targetPath) {
        System.out.print("mv: overwrite '" + targetPath.getFileName() + "'? (y/n): ");
        try (Scanner scanner = new Scanner(System.in)) {
            String response = scanner.nextLine().trim();
            return response.equalsIgnoreCase("y");
        }
    }
}
