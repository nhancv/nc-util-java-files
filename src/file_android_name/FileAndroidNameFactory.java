package file_android_name;

import base.Factory;
import base.FileUtils;

import java.io.File;

/**
 * Created by nhancao on 1/6/17.
 */
public class FileAndroidNameFactory implements Factory {

    @Override
    public void run() {
        File folder = new File("res/file_android_name");
        File[] parentFolders = folder.listFiles();

        if (parentFolders != null) {
            for (File parentDir : parentFolders) {
                if (parentDir.isDirectory()) {
                    String newName = parentDir.getName();
                    String fileExt = null;
                    File[] lv1ListDir = parentDir.listFiles();
                    if (lv1ListDir != null) {
                        for (File file : lv1ListDir) {
                            if (file.isDirectory()) {
                                File[] lv2ListFile = file.listFiles();
                                if (lv2ListFile != null) {
                                    for (File file1 : lv2ListFile) {
                                        if (file1.isFile()) {
                                            String currentName = file1.getName();
                                            if (fileExt == null) {
                                                fileExt = FileUtils.getFileExtension(file1);
                                                if (fileExt != null) {
                                                    newName += "." + fileExt;
                                                }
                                            }
                                            if (!currentName.startsWith(".DS_Store")) {
                                                System.out.println(String.format("File %s to %s", currentName, newName));
                                                FileUtils.renameFile(file1, newName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
