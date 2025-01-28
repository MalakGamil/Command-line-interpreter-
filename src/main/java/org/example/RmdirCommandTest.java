package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RmdirCommandTest {

    private Path testDirectory;
    private RmdirCommand rmdirCommand;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        testDirectory = Files.createTempDirectory("testDir");
        rmdirCommand = new RmdirCommand(testDirectory);

        // Set up output capturing
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Set up error capturing
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testRemoveDirectorySuccess() throws IOException {
        // Arrange: Create a directory inside the test directory
        Path dirToRemove = Files.createDirectory(testDirectory.resolve("dirToRemove"));

        // Act: Attempt to remove the directory
        rmdirCommand.execute(new String[]{"rmdir", "dirToRemove"});

        // Assert: Check that the directory was removed and correct output was printed
        assertTrue(outContent.toString().contains("Removed directory: dirToRemove"),
                "Should confirm directory was removed.");
        assertTrue(Files.notExists(dirToRemove), "Directory should no longer exist.");
    }

    @Test
    void testRemoveNonExistentDirectory() {
        // Act: Attempt to remove a directory that does not exist
        rmdirCommand.execute(new String[]{"rmdir", "nonExistentDir"});

        // Assert: Check for the error message indicating the directory couldn't be removed
        assertTrue(errContent.toString().contains("rmdir: cannot remove 'nonExistentDir'"),
                "Should show error message when trying to remove a non-existent directory.");
    }

    @Test
    void testRemoveDirectoryWithContents() throws IOException {
        // Arrange: Create a directory and add a file inside it
        Path dirWithContents = Files.createDirectory(testDirectory.resolve("dirWithContents"));
        Files.createFile(dirWithContents.resolve("fileInside.txt"));

        // Act: Attempt to remove the non-empty directory
        rmdirCommand.execute(new String[]{"rmdir", "dirWithContents"});

        // Assert: Check for the error message and confirm directory was not deleted
        assertTrue(errContent.toString().contains("rmdir: cannot remove 'dirWithContents'"),
                "Should show error message when trying to remove a non-empty directory.");
        assertTrue(Files.exists(dirWithContents), "Non-empty directory should still exist.");
    }

    @Test
    void testUsageMessage() {
        // Act: Run the command without specifying a directory
        rmdirCommand.execute(new String[]{"rmdir"});

        // Assert: Check for the usage message
        assertTrue(errContent.toString().contains("Usage: rmdir <directory> [<directory> ...]"),
                "Should show usage message when no directory is specified.");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up any created files or directories
        if (Files.exists(testDirectory.resolve("dirToRemove"))) {
            Files.delete(testDirectory.resolve("dirToRemove"));
        }
        if (Files.exists(testDirectory.resolve("dirWithContents/fileInside.txt"))) {
            Files.delete(testDirectory.resolve("dirWithContents/fileInside.txt"));
        }
        if (Files.exists(testDirectory.resolve("dirWithContents"))) {
            Files.delete(testDirectory.resolve("dirWithContents"));
        }
        Files.deleteIfExists(testDirectory);

        // Restore standard output and error streams
        System.setOut(System.out);
        System.setErr(System.err);
    }
}

