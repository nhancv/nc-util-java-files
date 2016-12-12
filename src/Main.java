import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) {
        File folder = new File("files");
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
                                                fileExt = getFileExtension(file1);
                                                if (fileExt != null) {
                                                    newName += "." + fileExt;
                                                }
                                            }
                                            if (!currentName.startsWith(".DS_Store")) {
                                                System.out.println(String.format("File %s to %s", currentName, newName));
                                                renameFile(file1, newName);
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
