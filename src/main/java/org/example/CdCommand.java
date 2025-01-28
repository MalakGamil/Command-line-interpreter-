package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CdCommand {
    private Path currentDirectory;

    public CdCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: cd <directory>");
            return;
        }

        String dirName = parts[1];
        Path newDirectory;

        if (dirName.equals("..")) {
            newDirectory = currentDirectory.getParent();
        } else {
            newDirectory = currentDirectory.resolve(dirName).normalize();
        }

        if (newDirectory != null && java.nio.file.Files.isDirectory(newDirectory)) {
            currentDirectory = newDirectory;  // Update the currentDirectory
            System.out.println("Changed directory to: " + currentDirectory);
        } else {
            System.err.println("No such directory: " + dirName);
        }
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }
}
