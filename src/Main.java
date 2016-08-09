import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FileUtils fileUtils = new FileUtils("input.txt", "output.txt");
        fileUtils.handle(new FileUtils.IFile() {
            List<String> inputArray = new ArrayList<>();

            @Override
            public void iRead(final BufferedReader reader) {
                reader.lines().forEach(s -> {
                    inputArray.add(s);
                });
            }

            @Override
            public void iWrite(final BufferedWriter writer) {
                inputArray.forEach((x) -> {
                    try {
                        writer.write("'" + x + "',\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });

    }
}
