package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatCommandTest {
    private Path testDirectory;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() throws IOException {
        testDirectory = Paths.get(System.getProperty("user.dir"), "testDir");

        // Clean up any existing test directory and files
        if (Files.exists(testDirectory)) {
            Files.walk(testDirectory)
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }
        Files.createDirectories(testDirectory);

        // Create test files
        Files.writeString(testDirectory.resolve("file1.txt"), "Hello, world!\nThis is a test file.");
        Files.writeString(testDirectory.resolve("file2.txt"), "Another test file.\nWith more content.");

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private String normalize(String output) {
        return output.replace("\r\n", "\n").trim();
    }

    @Test
    void testReadSingleFile() {
        CatCommand catCommand = new CatCommand(testDirectory);
        catCommand.execute(new String[]{"cat", "file1.txt"});

        String expectedOutput = "Hello, world!\nThis is a test file.";
        assertEquals(normalize(expectedOutput), normalize(outContent.toString()),
                "Should print the contents of the specified file.");
    }

    @Test
    void testReadMultipleFiles() {
        CatCommand catCommand = new CatCommand(testDirectory);
        catCommand.execute(new String[]{"cat", "file1.txt", "file2.txt"});

        String expectedOutput = "Hello, world!\nThis is a test file.\nAnother test file.\nWith more content.";
        assertEquals(normalize(expectedOutput), normalize(outContent.toString()),
                "Should print the contents of multiple files in order.");
    }

    @Test
    void testFileNotFound() {
        CatCommand catCommand = new CatCommand(testDirectory);
        catCommand.execute(new String[]{"cat", "nonExistentFile.txt"});

        assertTrue(errContent.toString().contains("cat: cannot read 'nonExistentFile.txt'"),
                "Should show an error message for non-existent files.");
    }

    @Test
    void testNoFileSpecified() {
        CatCommand catCommand = new CatCommand(testDirectory);
        catCommand.execute(new String[]{"cat"});

        assertTrue(errContent.toString().contains("Usage: cat <file> [<file> ...]"),
                "Should show usage message when no file is specified.");
    }
}
