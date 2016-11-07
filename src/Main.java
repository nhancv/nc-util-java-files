import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println(BillParser.isDateValid("17JUN2013"));
        BillParser billParser = new BillParser();
        List<String> filePaths = new ArrayList<>();
        filePaths.add("files/ocr/ocr3.txt");
        System.out.println(billParser.process(filePaths));
    }



}
