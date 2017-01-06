package base;

import bill_parser.BillParserFactory;
import file_android_name.FileAndroidNameFactory;

/**
 * Created by nhancao on 1/6/17.
 */
public enum Module {
    BILL_PARSER,
    FILE_ANDROID_NAME;

    public void run() {
        switch (this) {
            case BILL_PARSER:
                new BillParserFactory().run();
                break;
            case FILE_ANDROID_NAME:
                new FileAndroidNameFactory().run();
                break;
        }
    }
}
