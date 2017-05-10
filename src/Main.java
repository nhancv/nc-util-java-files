import bill_parser.BillParserFactory;
import file_android_name.FileAndroidNameFactory;
import map_name.MapNameFactory;
import number_generator.NumberGeneratorFactory;
import png_to_jpg.PngToJpgFactory;
import task.NTask;
import task.NTaskManager;

public class Main {

    public static void main(String[] args) {
//        Module.BILL_PARSER.run();
//        Module.FILE_ANDROID_NAME.run();
//        Module.NUMBER_GENERATOR.run();
//        Module.PNG_TO_JPG.run();
//        Module.MAP_NAME.run();

        NTaskManager taskManager = new NTaskManager();
        taskManager.genData();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Change ActiveGroup: " + 1);
            taskManager.updateActiveGroup(1);
            taskManager.showList();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Change ActiveGroup: " + 2);
            taskManager.updateActiveGroup(2);
            taskManager.showList();

        }).start();
        while (taskManager.hasNext()) {

            NTask nTask = taskManager.next();
            System.out.println("Process: " + nTask.getId());

            taskManager.popTask(nTask);
            taskManager.refreshTaskList();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


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
