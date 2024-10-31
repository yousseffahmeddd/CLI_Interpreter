import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CLIInterpreterTest {

    private CLIInterpreter cliInterpreter;
    private Path testDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory and initialize CLIInterpreter
        testDir = Files.createTempDirectory("CLI_Test");
        cliInterpreter = new CLIInterpreter();
        cliInterpreter.cd(testDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temporary directory after each test
        Files.walk(testDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testPwd() {
        cliInterpreter.pwd();
        assertEquals(testDir.toAbsolutePath().toString(), cliInterpreter.currentDirectory.toString());
    }

    @Test
    void testCd() {
        Path newDir = testDir.resolve("newDir");
        try {
            Files.createDirectory(newDir);
            cliInterpreter.cd("newDir");
            assertEquals(newDir, cliInterpreter.currentDirectory);
        } catch (IOException e) {
            fail("Directory creation failed");
        }
    }

    @Test
    void testMkdir() {
        String dirName = "testDir";
        cliInterpreter.mkdir(dirName);
        Path newDirPath = testDir.resolve(dirName);
        assertTrue(Files.exists(newDirPath) && Files.isDirectory(newDirPath));
    }

    @Test
    void testRmdir() throws IOException {
        String dirName = "testDir";
        Path dirPath = testDir.resolve(dirName);
        Files.createDirectory(dirPath);
        cliInterpreter.rmdir(dirName);
        assertFalse(Files.exists(dirPath));
    }

    @Test
    void testLs() throws IOException {
        // Create some files to list
        Files.createFile(testDir.resolve("a.txt"));
        Files.createFile(testDir.resolve("b.txt"));
        Files.createFile(testDir.resolve("c.txt"));
        cliInterpreter.ls();
        // Expected alphabetical order output in the CLI output
    }

    @Test
    void testLs_r() throws IOException {
        // Create some files to list in reverse
        Files.createFile(testDir.resolve("a.txt"));
        Files.createFile(testDir.resolve("b.txt"));
        Files.createFile(testDir.resolve("c.txt"));
        cliInterpreter.ls_r();
        // Expected reverse alphabetical order output in the CLI output
    }

    @Test
    void testLs_a() throws IOException {
        // Create hidden file and check if it's displayed
        Files.createFile(testDir.resolve(".hidden"));
        cliInterpreter.ls_a();
        // Check output contains ".hidden"
    }

    @Test
    void testMv() throws IOException {
        // Move a file to another directory
        String sourceFile = "source.txt";
        String destinationDir = "destDir";
        Files.createFile(testDir.resolve(sourceFile));
        Files.createDirectory(testDir.resolve(destinationDir));
        cliInterpreter.mv(sourceFile, destinationDir);
        assertTrue(Files.exists(testDir.resolve(destinationDir).resolve(sourceFile)));
        assertFalse(Files.exists(testDir.resolve(sourceFile)));
    }

    @Test
    void testTouch() {
        String fileName = "testFile.txt";
        cliInterpreter.touch(fileName);
        assertTrue(Files.exists(testDir.resolve(fileName)));
    }

    @Test
    void testRm() throws IOException {
        String fileName = "testFile.txt";
        Path filePath = testDir.resolve(fileName);
        Files.createFile(filePath);
        cliInterpreter.rm(fileName);
        assertFalse(Files.exists(filePath));
    }

    @Test
    void testCat() throws IOException {
        // Create a file with some content
        String fileName = "testFile.txt";
        String content = "Hello World";
        Path filePath = testDir.resolve(fileName);
        Files.write(filePath, content.getBytes());
        cliInterpreter.cat(fileName);
        // Expected output: "Hello World" in the CLI output
    }

    @Test
    void testWriteToFile() throws IOException {
        String fileName = "testFile.txt";
        String content = "New Content";
        cliInterpreter.writeToFile(fileName, content);
        assertEquals(content, Files.readString(testDir.resolve(fileName)));
    }

    @Test
    void testAppendToFile() throws IOException {
        String fileName = "testFile.txt";
        String initialContent = "Initial Content\n";
        String appendedContent = "Appended Content";
        Files.write(testDir.resolve(fileName), initialContent.getBytes());
        cliInterpreter.appendToFile(fileName, appendedContent);
        assertEquals(initialContent + appendedContent, Files.readString(testDir.resolve(fileName)));
    }

    @Test
    void testHelp() {
        cliInterpreter.help();
        // Expected output with available commands in the CLI output
    }

    @Test
    void testClear() {
        cliInterpreter.clear();
        // System clear output expected, tested visually
    }

    @Test
    void testMore() throws IOException {
        Files.createFile(testDir.resolve("a.txt"));
        Files.createFile(testDir.resolve("b.txt"));
        cliInterpreter.more(true);
        // Expected interactive output, tested visually
    }
    
    @Test
    void testLess() throws IOException {
        Files.createFile(testDir.resolve("a.txt"));
        Files.createFile(testDir.resolve("b.txt"));
        cliInterpreter.less(true);
        // Expected interactive output, tested visually
    }
}
