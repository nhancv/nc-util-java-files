package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nhancao on 8/9/16.
 */
public class FileUtils {

    private String inputFileName;
    private String outputFileName;
    private Charset charset;

    public interface IFile {
        void iRead(final BufferedReader reader);

        void iWrite(final BufferedWriter writer);
    }

    public FileUtils(final String inputFileName, final String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.charset = Charset.forName("UTF-8");
    }

    public void handle(final IFile fileHandle) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFileName), charset)) {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName), charset);
            fileHandle.iRead(reader);
            fileHandle.iWrite(writer);
            reader.close();
            writer.close();
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    public static String getFileExtension(File file) {
        try {
            String fullFileName = file.getName();
            String[] split = fullFileName.split("\\.");
            if (split.length > 1) {
                return split[split.length - 1];
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void renameFile(File fileName, String newFileName) {
        try {
            File newFile = new File(fileName.getParent(), newFileName);
            Files.move(fileName.toPath(), newFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
