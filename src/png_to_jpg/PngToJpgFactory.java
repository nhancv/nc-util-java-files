package png_to_jpg;

import base.Factory;
import base.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nhancao on 1/6/17.
 */
public class PngToJpgFactory implements Factory {

    @Override
    public void run() {

        try {
            Files.find(Paths.get("res/png_to_jpg"), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .filter(path -> {
                        String fileExt = FileUtils.getFileExtension(path.toFile());
                        return fileExt != null && fileExt.toLowerCase().equals("png");
                    })
                    .forEach(path -> {
                        FileUtils.convertToJpg(path.toString());
                        FileUtils.deleteFile(path.toString());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
