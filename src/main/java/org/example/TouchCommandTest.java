package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TouchCommandTest {

    private Path testDirectory;
    private TouchCommand touchCommand;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        testDirectory = Files.createTempDirectory("testDir");
        touchCommand = new TouchCommand(testDirectory);

        // Set up output capturing
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Set up error capturing
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testFileCreation() throws IOException {
        // Act: Create a new file using the command
        touchCommand.execute(new String[]{"touch", "newFile.txt"});

        // Assert: Verify the file is created and check the output message
        Path newFile = testDirectory.resolve("newFile.txt");
        assertTrue(Files.exists(newFile), "File should be created.");
        assertTrue(outContent.toString().contains("File created: newFile.txt"),
                "Should confirm file creation.");
    }

    @Test
    void testUpdateLastModifiedTime() throws IOException, InterruptedException {
        // Arrange: Create a file and get its initial last modified time
        Path existingFile = Files.createFile(testDirectory.resolve("existingFile.txt"));
        FileTime initialTime = Files.getLastModifiedTime(existingFile);

        // Act: Update last modified time by re-touching the file
        Thread.sleep(1000); // Ensure a visible difference in timestamp
        touchCommand.execute(new String[]{"touch", "existingFile.txt"});

        // Assert: Verify last modified time is updated
        FileTime updatedTime = Files.getLastModifiedTime(existingFile);
        assertTrue(updatedTime.toMillis() > initialTime.toMillis(),
                "File's last modified time should be updated.");
    }

//    @Test
//    void testFileCreationErrorInReadOnlyDirectory() throws IOException {
//        // Arrange: Set directory as read-only
//        Path readOnlyFile = testDirectory.resolve("readOnlyFile.txt");
//        Files.createFile(readOnlyFile);
//        readOnlyFile.toFile().setReadOnly();
//
//        // Act: Try to update the read-only file
//        touchCommand.execute(new String[]{"touch", "readOnlyFile.txt"});
//
//        // Assert: Verify error message is printed
//        assertTrue(errContent.toString().contains("touch: cannot create file 'readOnlyFile.txt'"),
//                "Should show error message when trying to touch a read-only file.");
//    }

    @Test
    void testUsageMessage() {
        // Act: Run the command without specifying a file
        touchCommand.execute(new String[]{"touch"});

        // Assert: Check for the usage message
        assertTrue(errContent.toString().contains("Usage: touch <file> [<file> ...]"),
                "Should show usage message when no file is specified.");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up created files
        Files.list(testDirectory).forEach(file -> {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Files.deleteIfExists(testDirectory);

        // Restore standard output and error streams
        System.setOut(System.out);
        System.setErr(System.err);
    }
}