import base.Factory;
import bill_parser.BillParserFactory;

public class Main {

    public static void main(String[] args) {

        Factory factory = new BillParserFactory();
        factory.run();

    }


}
