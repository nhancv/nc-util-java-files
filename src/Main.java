import bill_parser.BillParserFactory;
import file_android_name.FileAndroidNameFactory;
import number_generator.NumberGeneratorFactory;

public class Main {

    public static void main(String[] args) {
//        Module.BILL_PARSER.run();
//        Module.FILE_ANDROID_NAME.run();
        Module.NUMBER_GENERATOR.run();
    }

    enum Module {
        BILL_PARSER,
        FILE_ANDROID_NAME,
        NUMBER_GENERATOR;

        public void run() {
            switch (this) {
                case BILL_PARSER:
                    new BillParserFactory().run();
                    break;
                case FILE_ANDROID_NAME:
                    new FileAndroidNameFactory().run();
                    break;
                case NUMBER_GENERATOR:
                    new NumberGeneratorFactory().run();
                    break;
            }
        }
    }

}
