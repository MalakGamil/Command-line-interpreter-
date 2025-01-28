package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class MkdirCommandTest {
    private Path testDirectory;
    private MkdirCommand mkdirCommand;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        testDirectory = Files.createTempDirectory("testDir");
        mkdirCommand = new MkdirCommand(testDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temporary directory
        Files.walk(testDirectory)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testCreateSingleDirectory() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mkdirCommand.execute(new String[]{"mkdir", "newDir"});

        Path newDir = testDirectory.resolve("newDir");
        assertTrue(Files.exists(newDir) && Files.isDirectory(newDir));
        assertTrue(outContent.toString().contains("Directory created: newDir"));
    }

    @Test
    void testCreateMultipleDirectories() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mkdirCommand.execute(new String[]{"mkdir", "dir1", "dir2", "dir3"});

        assertTrue(Files.exists(testDirectory.resolve("dir1")));
        assertTrue(Files.exists(testDirectory.resolve("dir2")));
        assertTrue(Files.exists(testDirectory.resolve("dir3")));
        assertTrue(outContent.toString().contains("Directory created: dir1"));
        assertTrue(outContent.toString().contains("Directory created: dir2"));
        assertTrue(outContent.toString().contains("Directory created: dir3"));
    }

    @Test
    void testDirectoryAlreadyExists() throws IOException {
        Path existingDir = Files.createDirectory(testDirectory.resolve("existingDir"));

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        mkdirCommand.execute(new String[]{"mkdir", "existingDir"});

        assertTrue(errContent.toString().contains("mkdir: directory 'existingDir' already exists."));
    }

    @Test
    void testEmptyDirectoryName() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        mkdirCommand.execute(new String[]{"mkdir", ""});

        assertTrue(errContent.toString().contains("mkdir: directory name cannot be empty."));
    }

    @Test
    void testNoArguments() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        mkdirCommand.execute(new String[]{"mkdir"});

        assertTrue(errContent.toString().contains("Usage: mkdir <directory> [<directory> ...]"));
    }
}