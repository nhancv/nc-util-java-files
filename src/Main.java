import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        FileUtils fileUtils = new FileUtils("input.txt", "output.txt");
        fileUtils.handle(new FileUtils.IFile() {
            List<String> inputArray = new ArrayList<>();
            List<String> strParse = new ArrayList<>();

            @Override
            public void iRead(final BufferedReader reader) {
                reader.lines().forEach(s -> {
                    inputArray.add(s);
                });
            }

            @Override
            public void iWrite(final BufferedWriter writer) {
                inputArray.forEach((x) -> {
                    x = verifyString(x);
                    x = verifyString(x);
                    if (x != null) {
                        strParse.add(x);
                        int index = containsString(x, "invoice date|debit on|due date|total due|payment due|payment due by");
                        if (index > -1) {
                            printMatches(x, "$");
                            System.out.println(index + " " + x);
                        }
                    }
                });

                strParse.forEach(s -> {
                    try {
                        writer.write(s + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


            }
        });
    }

    /**
     * Verify string
     *
     * @param input
     * @return
     */
    public static String verifyString(String input) {
        if (input == null) return null;
        //Verify String: remove more space, tab, null, return, ..., special character
        String tmp = input.replaceAll("\\s{2,}", " ").toUpperCase().replaceAll("[+^:,]", "").trim();
        if (tmp.length() > 0) return tmp;
        return null;
    }

    public static void printMatches(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
        }
    }

    public static List<String> parseList(List<String> inputList, PARSE parseType) {

        List<String> list = new ArrayList<>();
        for (String s : inputList) {
            int index = containsString(s, parseType.getKeyWord());
            if (index > -1) {
                System.out.println(index + " " + s);
            }


        }


        return list;
    }

    /**
     * @param input
     * @param keyWord ex: invoice date|debit on
     * @return
     */
    public static int containsString(String input, String keyWord) {
        if (input == null) return -1;
        String[] tmp = keyWord.toUpperCase().split("[|]");
        if (tmp.length > 0) {
            for (String s : tmp) {
                int index = input.indexOf(s);
                if (index > -1) return index;
            }
        }
        return -1;
    }

    /**
     * @param input
     * @param regx  ex: (\\d+)(\\/|-|\\s|\\.)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|\\d+|)(\\/|-|\\s|\\.)(\\d+)
     * @return
     */
    public static boolean match(String input, String regx) {
        if (input == null) return false;
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    enum PARSE {
        DUE_DATE(
                "invoice date|debit on|due date|total due|payment due|payment due by",
                "(\\d+)(\\/|-|\\s|\\.)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|\\d+|)(\\/|-|\\s|\\.)(\\d+)"),

        AMOUNT(
                "amount|debited|total|payment due|total due|amount due",
                "$"),

        INVOICE_NO(
                "invoice number|invoice no|bill number|bill no",
                "(\\w*\\d+)");


        private String keyWord;
        private String regx;

        PARSE(String keyWord, String regx) {
            this.keyWord = keyWord;
            this.regx = regx;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public String getRegx() {
            return regx;
        }

    }


}
