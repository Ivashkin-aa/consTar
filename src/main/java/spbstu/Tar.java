package spbstu;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;



public class Tar {
    @Option(name = "-out", metaVar = "Connect", usage = "Mix files", forbids = {"-u"})
    private File con;

    @Option(name = "-u", metaVar = "Split", usage = "Split files", forbids = {"-out"})
    private File spl;

    @Argument(metaVar = "InputName", usage = "Input file name")
    private ArrayList<File> inputFilesName;

    private String sep = File.separator;
    private String dir = "src" + sep + "test" + sep + "resources";

    public static void main(String[] args) {
        new Tar().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Command Line: -u filename.txt OR file1.txt file2.txt ... -out output.txt");
            parser.printUsage(System.err);
            return;
        }

        if (spl == null) {
            try {
                PrintWriter record = new PrintWriter(dir + sep + con);
                Map<String, Integer> info = new HashMap<>();
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
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else {
            try (ReversedLinesFileReader str = new ReversedLinesFileReader(spl)) {
                String line = str.readLine();
                if (line != null) {
                    Map<String, Integer> content = new HashMap<>();
                    while (!line.equals("")) {
                        String[] part = line.split(" ");
                        Path dr = Paths.get(part[0]);
                        Path name = dr.getFileName();
                        Integer s = Integer.valueOf(part[1]);
                        content.put(String.valueOf(name), s);
                        line = str.readLine();
                    }
                    Path drNew = Paths.get(String.valueOf(spl));
                    String name = String.valueOf(drNew.getFileName());
                    String[] nameForDir = name.split("\\.txt");
                    boolean dirNew = new File(dir + sep + nameForDir[0]).mkdir();
                    try (BufferedReader s = new BufferedReader(new FileReader(spl))) {
                        String line2 = s.readLine();
                        for (Map.Entry<String, Integer> pair : content.entrySet()) {
                            PrintWriter rec = new PrintWriter(dir + sep + nameForDir[0] + sep + pair.getKey());
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
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
