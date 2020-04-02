package spbstu;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Tar {
    @Option(name = "-out", metaVar = "Connect", usage = "Mix files", forbids = {"-u"})
    private File con;

    @Option(name = "-u", metaVar = "Split", usage = "Split files", forbids = {"-out"})
    private File spl;

    @Argument(metaVar = "InputName", usage = "Input file name")
    private ArrayList<File> inputFilesName;

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

        //if (con == null) {
            try {
                PrintWriter record = new PrintWriter(con);
                List<String> info = new ArrayList<String>();
                for (File f : inputFilesName) {
                    BufferedReader str = new BufferedReader(new FileReader(f));
                    String line;
                    while ((line = str.readLine()) != null) {
                        record.println(line);
                    }
                    info.add(f.toString());
                }
                record.write("\n");
                for (String i : info)
                    record.println(i);
                record.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        //} else {
            try {
                ReversedLinesFileReader str = new ReversedLinesFileReader(spl);
                String line = str.readLine();
                if (line != null) {
                    String regex = ".*\\.txt";
                    List<String> content = new ArrayList<>();
                    while (!line.equals("")) {
                        for (String n : line.split("\\\\")) {
                            if (n.matches(regex))
                                content.add(n);
                        }
                        line = str.readLine();
                    }
                    System.out.println("Файл " + spl + " состоит из:");
                    for (int i = content.size() - 1; i >= 0; i--) {
                        String con = content.get(i);
                        System.out.println(con);
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
//}
