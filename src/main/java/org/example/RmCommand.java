package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;

public class RmCommand {
    private Path currentDirectory;

    public RmCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: rm <file> [<file> ...]");
        } else {
            for (int i = 1; i < parts.length; ++i) {
                Path fileToRemove = this.currentDirectory.resolve(parts[i].trim());

                try {
                    if (Files.notExists(fileToRemove)) {
                        System.err.println("rm: cannot remove '" + parts[i] + "': No such file or directory");
                    } else if (Files.isDirectory(fileToRemove)) {
                        System.err.println("rm: cannot remove '" + parts[i] + "': Is a directory");
                    } else {
                        Files.delete(fileToRemove);
                        System.out.println("Removed file: " + parts[i]);
                    }
                } catch (NoSuchFileException e) {
                    System.err.println("rm: cannot remove '" + parts[i] + "': No such file or directory");
                } catch (IOException e) {
                    System.err.println("rm: cannot remove '" + parts[i] + "': " + e.getMessage());
                }
            }
        }
    }
}
