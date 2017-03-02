package map_name;

import base.Factory;
import base.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhancao on 1/6/17.
 */
public class MapNameFactory implements Factory {

    private Map<String, String> nameMapping = new HashMap<String, String>() {{
        put("Add photo icon", "ic_add_photo");
        put("Add photo message icon", "ic_add_photo_message");

    }};

    @Override
    public void run() {

        try {
            Files.find(Paths.get("res/map_name/src"), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        String fromF = "res/map_name/src/" + fileName, toF = null;
                        if (fileName.contains("@4x.png")) {
                            String f = fileName.replace("@4x.png", "");
                            if (nameMapping.containsKey(f)) {
                                toF = "res/map_name/dest/drawable-xxxhdpi/" + nameMapping.get(f) + ".png";
                            }
                        } else if (fileName.contains("@3x.png")) {
                            String f = fileName.replace("@3x.png", "");
                            if (nameMapping.containsKey(f)) {
                                toF = "res/map_name/dest/drawable-xxhdpi/" + nameMapping.get(f) + ".png";
                            }
                        } else if (fileName.contains("@2x.png")) {
                            String f = fileName.replace("@2x.png", "");
                            if (nameMapping.containsKey(f)) {
                                toF = "res/map_name/dest/drawable-xhdpi/" + nameMapping.get(f) + ".png";
                            }
                        } else if (fileName.contains("@1.5x.png")) {
                            String f = fileName.replace("@1.5x.png", "");
                            if (nameMapping.containsKey(f)) {
                                toF = "res/map_name/dest/drawable-hdpi/" + nameMapping.get(f) + ".png";
                            }
                        } else if (!fileName.contains("@0.5x.png")) {
                            String f = fileName.replace(".png", "");
                            if (nameMapping.containsKey(f)) {
                                toF = "res/map_name/dest/drawable-mdpi/" + nameMapping.get(f) + ".png";
                            }
                        }

                        if (toF != null) {
                            System.out.println(fromF + " -> " + toF);
                            FileUtils.moveFile(fromF, toF);
                        }
                        FileUtils.deleteFile(path.toString());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
