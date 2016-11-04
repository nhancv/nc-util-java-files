import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        FileUtils fileUtils = new FileUtils("input.txt", "output.txt");
        fileUtils.handle(new FileUtils.IFile() {
            List<String> inputArray = new ArrayList<>();
            List<String> strParses = new ArrayList<>();
            List<String> strOriginals = new ArrayList<>();

            List<String> dueDates = new ArrayList<>();
            List<String> amounts = new ArrayList<>();
            List<String> invoidNos = new ArrayList<>();

            @Override
            public void iRead(final BufferedReader reader) {
                reader.lines().forEach(s -> {
                    inputArray.add(s);
                });
            }

            @Override
            public void iWrite(final BufferedWriter writer) {
                String all = "";
                List<String> l1 = new ArrayList<>();
                inputArray.forEach((x) -> {
                    l1.add(x.replaceAll("[+^:,]", "").toUpperCase().trim());
                });

                //Concat to one str
                for (int i = 0; i < l1.size(); i++) {
                    all += l1.get(i) + "\n";
                }
                //Replace more than 2 newline to one line
                all = all.replaceAll("[\n]{2,}", "\n\n");
                //Build to array
                String[] subStrArr = all.split("[\n]");
                for (int i = 0; i < subStrArr.length; i++) {
                    String parse = verifyString(subStrArr[i]);
                    strParses.add(parse);
                    strOriginals.add(subStrArr[i]);
                }

                //Parse
                for (int i = 0; i < strParses.size(); i++) {
                    String parse = strParses.get(i);
                    PARSE parseType = PARSE.DUE_DATE;
                    int index = containsString(parse, parseType.getKeyWord());
                    if (index > -1) {
                        dueDates.addAll(findInList(strOriginals, i, parseType));
                    }
                    parseType = PARSE.AMOUNT;
                    index = containsString(parse, parseType.getKeyWord());
                    if (index > -1) {
                        amounts.addAll(findInList(strOriginals, i, parseType));
                    }
                    parseType = PARSE.INVOICE_NO;
                    index = containsString(parse, parseType.getKeyWord());
                    if (index > -1) {
                        invoidNos.addAll(findInList(strOriginals, i, parseType));
                    }
                }

                System.out.println(findFinalResult(dueDates, PARSE.DUE_DATE));
                System.out.println(findFinalResult(amounts, PARSE.AMOUNT));
                System.out.println(findFinalResult(invoidNos, PARSE.INVOICE_NO));

                strParses.forEach(s -> {
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
        if (input == null) return "";
        String tmp = input.replaceAll("\\s{2,}", " ").toUpperCase().replaceAll("[+^:,]", "").trim();
        if (tmp.length() > 0) return tmp;
        return "";
    }

    /**
     * Find all string satisfied in inputList
     *
     * @param inputList
     * @param index
     * @param parseType
     * @return
     */
    public static List<String> findInList(List<String> inputList, int index, PARSE parseType) {
        List<String> list = new ArrayList<>();
        list.addAll(addMatchedToList(inputList, index - 1, parseType));
        list.addAll(addMatchedToList(inputList, index, parseType));
        list.addAll(addMatchedToList(inputList, index + 1, parseType));
        return list;
    }

    public static String findFinalResult(List<String> inputList, PARSE parseType) {
        String finalResult = null;
        //Verify
        switch (parseType) {
            case AMOUNT:
            case INVOICE_NO:
                String max = null;
                for (String s : inputList) {
                    if (max == null || s.length() >= max.length()) {
                        max = s;
                    }
                }
                finalResult = max;
                break;
            case DUE_DATE:
                Date tmp;
                Date minDate = new Date();
                for (String s : inputList) {
                    tmp = isDateValid(s);
                    if (tmp != null && minDate.compareTo(tmp) > 0) {
                        minDate = tmp;
                    }
                }
                finalResult = new SimpleDateFormat("dd MMM yyyy").format(minDate);
                break;
        }
        return finalResult;
    }

    /**
     * Check date is valid
     *
     * @param inputDate
     * @return
     */
    public static Date isDateValid(String inputDate) {
        List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
            {
                add(new SimpleDateFormat("M/dd/yyyy"));
                add(new SimpleDateFormat("dd.M.yyyy"));
                add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
                add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
                add(new SimpleDateFormat("dd MMMMM yyyy"));
                add(new SimpleDateFormat("dd MMM yyyy"));
                add(new SimpleDateFormat("dd M yyyy"));
                add(new SimpleDateFormat("dd.MMM.yyyy"));
                add(new SimpleDateFormat("dd-MMM-yyyy"));
            }
        };

        Date date = null;
        if (inputDate == null) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(inputDate);
            } catch (ParseException ignored) {
            }
            if (date != null) {
                break;
            }
        }
        return date;
    }

    /**
     * Add item matched to result list
     *
     * @param inputList
     * @param index
     * @param parseType
     * @return
     */
    public static List<String> addMatchedToList(List<String> inputList, int index, PARSE parseType) {
        List<String> list = new ArrayList<>();
        if (index >= 0 && index < inputList.size()) {
            String tmp = inputList.get(index);
            if (parseType == PARSE.AMOUNT) {
                String[] res = tmp.split("\\s{2,}+");
                for (int i = 0; i < res.length; i++) {
                    if (res[i].contains(parseType.getRegx())) {
                        list.add(res[i]);
                    }
                }
            } else {
                String[] res = tmp.split("\\s{2,}+");
                for (int i = 0; i < res.length; i++) {
                    Matcher matcher = match(res[i], parseType.getRegx());
                    if (matcher != null) {
                        list.add(res[i]);
                    }
                }
            }
        }
        return list;
    }


    /**
     * Check keyWord is exist in input String or not
     *
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
     * Check regx is match with input string
     *
     * @param input
     * @param regx  ex: (\\d+)(\\/|-|\\s|\\.)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC|\\d+|)(\\/|-|\\s|\\.)(\\d+)
     * @return
     */
    public static Matcher match(String input, String regx) {
        if (input == null) return null;
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }


    enum PARSE {
        DUE_DATE(
                "INVOICE DATE|DEBIT ON|DUE DATE|TOTAL DUE|PAYMENT DUE|PAYMENT DUE BY",
                "(\\d+)(\\/|-|\\s|\\.)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC|\\d+|)(\\/|-|\\s|\\.)(\\d+)"),

        AMOUNT(
                "AMOUNT|DEBITED|TOTAL|PAYMENT DUE|TOTAL DUE|AMOUNT DUE",
                "$"),

        INVOICE_NO(
                "INVOICE NUMBER|INVOICE NO|BILL NUMBER|BILL NO",
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
