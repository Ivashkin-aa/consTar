package spbstu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

class consTarTest {
    private String sep = File.separator;
    private String inputFile1 = "src" + sep + "test" + sep + "resources" + sep + "test1.txt";
    private String InputFile2 = "src" + sep + "test" + sep + "resources" + sep + "test2.txt";
    private String outputFile = "src" + sep + "test" + sep + "resources" + sep + "pin.txt";
    private String inputFile11 = "src" + sep + "test" + sep + "resources" + sep + "pin" + sep + "test1.txt";
    private String inputFile21 = "src" + sep + "test" + sep + "resources" + sep + "pin" + sep + "test2.txt";

    @Test
    public void conFiles() throws IOException {
        Tar.main(new String[]{inputFile1, InputFile2, "-out", "pin.txt"});
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            assertEquals("Каждый может писать?", line);
            line = reader.readLine();
            assertEquals("!Yeap!", line);
            line = reader.readLine();
            assertEquals("Привет, мир?", line);
            line = reader.readLine();
            assertEquals("            XYZ", line);
        }
    }

    @Test
    public void splFile() throws IOException {
        Tar.main(new String[]{"-u", outputFile});
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile11))) {
            String line = reader.readLine();
            assertEquals("Каждый может писать?", line);
            line = reader.readLine();
            assertEquals("!Yeap!", line);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile21))) {
            String line = reader.readLine();
            assertEquals("Привет, мир?", line);
            line = reader.readLine();
            assertEquals("            XYZ", line);
        }
    }
}
