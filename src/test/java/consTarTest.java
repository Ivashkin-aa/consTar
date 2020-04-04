import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.jupiter.api.Test;
import spbstu.Tar;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

class consTarTest {
    private String fs = File.separator;
    private String inputFile1 = "src" + fs + "test" + fs + "java" + fs + "test1.txt";
    private String InputFile2 = "src" + fs + "test" + fs + "java" + fs + "test2.txt";
    private String outputFile = "pin.txt";

    @Test
    public void conFiles() throws IOException {
        Tar.main(new String[]{inputFile1, InputFile2, "-out", outputFile});
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        String line = reader.readLine();
        assertEquals("Привет, мир!", line);
        line = reader.readLine();
        assertEquals("            XYZ", line);
        line = reader.readLine();
        assertEquals("Каждый может писать?", line);
        line = reader.readLine();
        assertEquals("!Yeap!", line);
    }

    @Test
    public void splFile() throws IOException {
        Tar.main(new String[]{"-u", outputFile});
        ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(outputFile));
        String line = reader.readLine();
        assertEquals("src\\test\\java\\test2.txt", line);
        line = reader.readLine();
        assertEquals("src\\test\\java\\test1.txt", line);
    }
}
