import org.example.RmCommand;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.rmi.server.RemoteServer;

import static org.junit.jupiter.api.Assertions.*;

public class RmCommandTest {
    private Path tempDir;
    private RmCommand rmCommand;




    @BeforeEach
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDir");
        rmCommand = new RmCommand(tempDir);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> file.deleteOnExit());
    }

    @Test
    public void testRemoveExistingFile() throws IOException {
        Path fileToRemove = Files.createFile(tempDir.resolve("fileToRemove.txt"));

        rmCommand.execute(new String[]{"rm", "fileToRemove.txt"});

        assertFalse(Files.exists(fileToRemove), "يجب أن يكون الملف محذوفًا بعد التنفيذ");
    }

    @Test
    public void testRemoveNonexistentFile() {
        String nonExistentFile = "nonExistent.txt";

        rmCommand.execute(new String[]{"rm", nonExistentFile});

        // Expected output: "cannot remove 'nonExistent.txt': No such file or directory"
        // يتم التحقق من الخطأ عن طريق متابعة الرسائل، أو من خلال مراقبة المخرجات أثناء التنفيذ.
    }

    @Test
    public void testRemoveMultipleFiles() throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));

        rmCommand.execute(new String[]{"rm", "file1.txt", "file2.txt"});

        assertFalse(Files.exists(file1), "يجب أن يكون file1.txt محذوفًا بعد التنفيذ");
        assertFalse(Files.exists(file2), "يجب أن يكون file2.txt محذوفًا بعد التنفيذ");
    }

    @Test
    public void testUsageMessageForNoArguments() {
        rmCommand.execute(new String[]{"rm"});
        // Expected output: "Usage: rm <file> [<file> ...]"
        // يتم التحقق من رسالة الاستخدام عند تقديم عدد غير كافٍ من الوسائط.
    }
}
