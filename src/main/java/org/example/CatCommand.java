package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class CatCommand {
    private Path currentDirectory;

    public CatCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        // If no arguments are provided (i.e., only "cat"), read from the console input
        if (parts.length < 2) {
            System.out.println("Enter text (press Enter to print the input and press ctrl+D to finish):");
            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
            return;
        }

        // Otherwise, read from the specified file(s)
        for (int i = 1; i < parts.length; i++) {
            Path fileToRead = this.currentDirectory.resolve(parts[i].trim());

            try {
                Stream<String> lines = Files.lines(fileToRead);
                PrintStream out = System.out;
                lines.forEach(out::println);
            } catch (IOException e) {
                System.err.println("cat: cannot read '" + parts[i] + "': " + e.getMessage());
            }
        }
    }
}
