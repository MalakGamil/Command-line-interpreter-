import org.example.MvCommand;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

public class MvCommandTest {
    private Path tempDir;
    private MvCommand mvCommand;

    @BeforeEach
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDir");
        mvCommand = new MvCommand(tempDir);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> file.deleteOnExit());
    }

    @Test
    public void testMoveFileToDirectory() throws IOException {
        Path sourceFile = Files.createFile(tempDir.resolve("sourceFile.txt"));
        Path destinationDir = Files.createDirectory(tempDir.resolve("destination"));

        mvCommand.execute(new String[]{"mv", "sourceFile.txt", "destination"});

        assertFalse(Files.exists(sourceFile), "يجب حذف الملف المصدر بعد النقل");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile.txt")), "يجب أن يكون الملف المصدر داخل الوجهة");
    }

    @Test
    public void testMoveFileToFile() throws IOException {
        Path sourceFile = Files.createFile(tempDir.resolve("sourceFile.txt"));
        Path destinationFile = tempDir.resolve("newFile.txt");

        mvCommand.execute(new String[]{"mv", "sourceFile.txt", "newFile.txt"});

        assertFalse(Files.exists(sourceFile), "يجب حذف الملف المصدر بعد النقل");
        assertTrue(Files.exists(destinationFile), "يجب أن يكون الملف المصدر تم نقله باسم الملف الجديد");
    }

    @Test
    public void testMoveNonexistentFile() {
        String nonExistentFile = "nonExistent.txt";
        mvCommand.execute(new String[]{"mv", nonExistentFile, "destination"});

        // Expected output on the console
        // Note: Check manually that "No such file or directory" message appears
    }

    @Test
    public void testOverwritePrompt() throws IOException {
        Path sourceFile = Files.createFile(tempDir.resolve("sourceFile.txt"));
        Path destinationFile = Files.createFile(tempDir.resolve("destinationFile.txt"));

        // To simulate user input for overwriting, set the response as "y"
        System.setIn(new java.io.ByteArrayInputStream("y\n".getBytes()));
        mvCommand.execute(new String[]{"mv", "sourceFile.txt", "destinationFile.txt"});

        assertFalse(Files.exists(sourceFile), "يجب حذف الملف المصدر بعد النقل");
        assertTrue(Files.exists(destinationFile), "يجب أن يتم استبدال الملف الوجهة");
    }
}
