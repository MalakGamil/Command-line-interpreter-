//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class RmdirCommand {
    private Path currentDirectory;

    public RmdirCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: rmdir <directory> [<directory> ...]");
        } else {
            for (int i = 1; i < parts.length; ++i) {
                Path dirToRemove = this.currentDirectory.resolve(parts[i].trim());

                try {
                    // Check if the path is a directory
                    if (!Files.isDirectory(dirToRemove)) {
                        System.err.println("rmdir: cannot remove '" + parts[i] + "': Not a directory");
                    } else {
                        Files.delete(dirToRemove);  // Attempt to delete only if it's a directory
                        System.out.println("Removed directory: " + parts[i]);
                    }
                } catch (DirectoryNotEmptyException e) {
                    // Handle case when the directory is not empty
                    System.err.println("rmdir: cannot remove '" + parts[i] + "': Directory not empty");
                } catch (NoSuchFileException e) {
                    // Handle case when the directory doesn't exist
                    System.err.println("rmdir: cannot remove '" + parts[i] + "': No such file or directory");
                } catch (IOException e) {
                    // Handle other IO exceptions
                    System.err.println("rmdir: cannot remove '" + parts[i] + "': " + e.getMessage());
                }
            }
        }
    }
}
