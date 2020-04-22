package spbstu;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Tar {
    @Option(name = "-out", metaVar = "Connect", usage = "Mix files", forbids = {"-u"})
    private File con;

    @Option(name = "-u", metaVar = "Split", usage = "Split files", forbids = {"-out"})
    private File spl;

    @Argument(metaVar = "InputName", usage = "Input file name")
    private ArrayList<File> inputFilesName;

    private String sep = File.separator;

    public static void main(String[] args) throws IOException {
        new Tar().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Command Line: -u filename.txt OR file1.txt file2.txt ... -out output.txt");
            parser.printUsage(System.err);
            return;
        }

        /*
         * @value info - связь между, файлом и его последней строкой. Выходной файл
         * @value content - связь между, файлом и его последней строкой . Входной файл
         * @value dirNew  - создание директории с именем файла
         */

        if (spl == null) {
            PrintWriter record = new PrintWriter(con);
            Map<String, Integer> info = new TreeMap<>();
            for (File f : inputFilesName) {
                try (BufferedReader str = new BufferedReader(new FileReader(f))) {
                    String line;
                    Integer cout = 0;
                    while ((line = str.readLine()) != null) {
                        record.println(line);
                        cout++;
                    }
                    info.put(f.toString(), cout);
                }
            }
            record.write("\n");
            for (Map.Entry<String, Integer> pair : info.entrySet())
                record.println(pair.getKey() + " " + pair.getValue());
            record.close();
        } else {
            try (ReversedLinesFileReader str = new ReversedLinesFileReader(spl, UTF_8)) {
                String line = str.readLine();
                if (line != null) {
                    Map<String, Integer> content = new TreeMap<>();
                    while (!line.equals("")) {
                        String[] part = line.split(" ");
                        Path dr = Paths.get(part[0]);
                        Path nameFile = dr.getFileName();
                        String name = nameFile.toString();
                        Integer s = Integer.valueOf(part[1]);

                        //проверка имени файла на уникальность
                        int count = 0;
                        while (content.containsKey(name)) {
                            count++;
                            name = FilenameUtils.removeExtension(name);
                            String num = "(" + count + ")";
                            name = name + num;
                            if (content.containsKey(name + ".txt"))
                                name = name.substring(0, name.length() - num.length());
                            name = name + ".txt";
                        }

                        content.put(name, s);
                        line = str.readLine();
                    }

                    //создание папки с именем текстового файла
                    Path drNew = Paths.get(String.valueOf(spl));
                    String name = String.valueOf(drNew.getFileName());
                    String nameForDir = FilenameUtils.removeExtension(name);
                    boolean dirNew = new File(nameForDir).mkdir();

                    try (BufferedReader s = new BufferedReader(new FileReader(spl))) {
                        String line2 = s.readLine();
                        for (Map.Entry<String, Integer> pair : content.entrySet()) {
                            PrintWriter rec = new PrintWriter(nameForDir + sep + pair.getKey());
                            Integer cout = pair.getValue();
                            while (cout != 0) {
                                rec.println(line2);
                                line2 = s.readLine();
                                cout--;
                            }
                            rec.close();
                        }
                    }
                }
            }
        }
    }
}
