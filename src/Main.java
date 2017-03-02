import bill_parser.BillParserFactory;
import file_android_name.FileAndroidNameFactory;
import map_name.MapNameFactory;
import number_generator.NumberGeneratorFactory;
import png_to_jpg.PngToJpgFactory;

public class Main {

    public static void main(String[] args) {
//        Module.BILL_PARSER.run();
//        Module.FILE_ANDROID_NAME.run();
//        Module.NUMBER_GENERATOR.run();
//        Module.PNG_TO_JPG.run();
        Module.MAP_NAME.run();

    }

    enum Module {
        BILL_PARSER,
        FILE_ANDROID_NAME,
        NUMBER_GENERATOR,
        PNG_TO_JPG,
        MAP_NAME;

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
                case PNG_TO_JPG:
                    new PngToJpgFactory().run();
                    break;
                case MAP_NAME:
                    new MapNameFactory().run();
                    break;
            }
        }
    }

}
