
package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LsCommandTest {
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
        Files.createFile(testDirectory.resolve("file1.txt"));
        Files.createFile(testDirectory.resolve("file2.txt"));
        Files.createFile(testDirectory.resolve(".hiddenFile.txt"));

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private String normalize(String output) {
        return output.replace("\r\n", "\n").trim();
    }

    @Test
    void testListFiles() {
        LsCommand lsCommand = new LsCommand(testDirectory);
        lsCommand.execute(new String[]{"ls"});

        List<String> expectedOutput = Arrays.asList("file1.txt", "file2.txt");
        assertEquals(normalize(String.join("\n", expectedOutput)), normalize(outContent.toString()),
                "Should list only visible files in alphabetical order.");
    }

    @Test
    void testListAllFiles() {
        LsCommand lsCommand = new LsCommand(testDirectory);
        lsCommand.execute(new String[]{"ls", "-a"});

        List<String> expectedOutput = Arrays.asList(".hiddenFile.txt", "file1.txt", "file2.txt");
        assertEquals(normalize(String.join("\n", expectedOutput)), normalize(outContent.toString()),
                "Should list all files including hidden ones.");
    }

    @Test
    void testListFilesInReverse() {
        LsCommand lsCommand = new LsCommand(testDirectory);
        lsCommand.execute(new String[]{"ls", "-r"});

        List<String> expectedOutput = Arrays.asList("file2.txt", "file1.txt");
        assertEquals(normalize(String.join("\n", expectedOutput)), normalize(outContent.toString()),
                "Should list visible files in reverse alphabetical order.");
    }

    @Test
    void testListAllFilesInReverse() {
        LsCommand lsCommand = new LsCommand(testDirectory);
        lsCommand.execute(new String[]{"ls", "-a", "-r"});

        List<String> expectedOutput = Arrays.asList("file2.txt", "file1.txt", ".hiddenFile.txt");
        assertEquals(normalize(String.join("\n", expectedOutput)), normalize(outContent.toString()),
                "Should list all files including hidden ones in reverse order.");
    }

    @Test
    void testErrorOnInvalidDirectory() {
        // Use a non-existent directory to simulate an error
        LsCommand lsCommand = new LsCommand(testDirectory.resolve("nonExistentDir"));
        lsCommand.execute(new String[]{"ls"});

        assertTrue(errContent.toString().contains("ls: Error listing files"),
                "Error output should indicate failure to list files in a non-existent directory.");
    }
}
