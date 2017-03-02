package base;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public FileUtils(final String inputFileName, final String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.charset = Charset.forName("UTF-8");
    }

    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    public static String getFileExtension(String fullFileName) {
        try {
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

    public static boolean moveFile(String fromPath, String toPath) {
        File fromF = new File(fromPath);
        File toF = new File(toPath);
        return fromF.renameTo(toF);
    }

    public static void deleteFile(String path) {
        try {
            new File(path).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertToJpg(String path) {
        BufferedImage bufferedImage;
        try {
            //read image file
            bufferedImage = ImageIO.read(new File(path));
            //create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            // write to jpeg file
            String jpgName = (path + ".jpg").toLowerCase().replace(".png.jpg", ".jpg");
            ImageIO.write(newBufferedImage, "jpg", new File(jpgName));
            System.out.println("Done: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public interface IFile {
        void iRead(final BufferedReader reader);

        void iWrite(final BufferedWriter writer);
    }

}
