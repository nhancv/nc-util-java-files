import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        for (int i = 0; i < 13; i++) {
            BillParser billParser = new BillParser();
            List<String> filePaths = new ArrayList<>();
            filePaths.add("files/ocr/ocr" + (i + 1) + ".txt");
            System.out.println("Bill " + (i + 1) + ": " + billParser.process(filePaths));
        }
    }


}
