package bill_parser;

import base.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 1/6/17.
 */
public class BillParserFactory implements Factory {

    @Override
    public void run() {
        for (int i = 0; i < 14; i++) {
            BillParser billParser = new BillParser();
            billParser.setCompany();
            List<String> filePaths = new ArrayList<>();
            filePaths.add("res/bill_parser/ocr" + (i + 1) + ".txt");
            System.out.println("Bill " + (i + 1) + ": " + billParser.process(filePaths));
        }
    }
}
