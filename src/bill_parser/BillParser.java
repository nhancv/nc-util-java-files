package bill_parser;

import javafx.util.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nhancao on 11/7/16.
 */
public class BillParser {

    private List<String> inputArray = new ArrayList<>();
    private List<String> strParses = new ArrayList<>();
    private List<String> strOriginals = new ArrayList<>();

    private List<String> dueDates = new ArrayList<>();
    private List<String> amounts = new ArrayList<>();
    private List<String> invoiceNos = new ArrayList<>();
    private List<String> companies = new ArrayList<>();

    public BillParser() {
    }

    public void setCompany() {
        PARSE.COMPANY.setKeyWord("WATER|OPTUS|SYNERGY|IINET|VIVID|DODO|VODAFONE|ENGIN|ALINTA|TELSTRA|WESTPAC|RAC|OVERNIGHT|TEST-QA|PRIMUS");
    }

    /**
     * Check date is valid with specific format
     *
     * @param inputDate
     * @param formatDate
     * @return
     */
    private boolean isValidDateWithFormat(String inputDate, String formatDate) {
        if (inputDate == null) {
            return false;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatDate, Locale.US);
            format.setLenient(false);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            format.parse(inputDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    /**
     * Check date is valid
     *
     * @param inputDate
     * @return
     */
    private Date isDateValid(String inputDate) {
        List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
            {
                add(new SimpleDateFormat("M/dd/yyyy", Locale.US));
                add(new SimpleDateFormat("dd.M.yyyy", Locale.US));
                add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a", Locale.US));
                add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a", Locale.US));
                add(new SimpleDateFormat("dd MMMMM yyyy", Locale.US));
                add(new SimpleDateFormat("ddMMMMMyyyy", Locale.US));
                add(new SimpleDateFormat("ddMMMMMyy", Locale.US));
                add(new SimpleDateFormat("dd MMM yyyy", Locale.US));
                add(new SimpleDateFormat("ddMMMyyyy", Locale.US));
                add(new SimpleDateFormat("ddMMMyy", Locale.US));
                add(new SimpleDateFormat("dd MMMyyyy", Locale.US));
                add(new SimpleDateFormat("ddMMM yyyy", Locale.US));
                add(new SimpleDateFormat("dd MMMyy", Locale.US));
                add(new SimpleDateFormat("ddMMM yy", Locale.US));
                add(new SimpleDateFormat("dd M yyyy", Locale.US));
                add(new SimpleDateFormat("ddMyyyy", Locale.US));
                add(new SimpleDateFormat("ddMyy", Locale.US));
                add(new SimpleDateFormat("dd.MMM.yyyy", Locale.US));
                add(new SimpleDateFormat("dd/MM/yy", Locale.US));
                add(new SimpleDateFormat("dd/MM/yyyy", Locale.US));
                add(new SimpleDateFormat("MM/dd/yyyy", Locale.US));
                add(new SimpleDateFormat("dd-MMM-yyyy", Locale.US));
            }
        };

        Date date = null;
        if (inputDate == null) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
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
     * Recognize text processing
     *
     * @param filePaths
     * @return
     */
    public Info process(List<String> filePaths) {

        try {
            for (String filePath : filePaths) {
                FileInputStream fstream = new FileInputStream(filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream, Charset.forName("UTF-8")));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    inputArray.add(strLine);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String all = "";
        List<String> l1 = new ArrayList<>();
        for (String x : inputArray) {
            l1.add(x.replaceAll("[+^,()]", "").replaceAll("[:]", "\n").toUpperCase().trim());
        }

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
            parseType = PARSE.REF_NO;
            index = containsString(parse, parseType.getKeyWord());
            if (index > -1) {
                invoiceNos.addAll(findInList(strOriginals, i, parseType));
            }

            parseType = PARSE.COMPANY;
            index = containsString(parse, parseType.getKeyWord());
            if (index > -1) {
                companies.add(parse);
            }
        }

        return new Info(
                findFinalResult(dueDates, PARSE.DUE_DATE),
                findFinalResult(amounts, PARSE.AMOUNT),
                findFinalResult(invoiceNos, PARSE.REF_NO),
                findFinalResult(companies, PARSE.COMPANY)
        );

    }

    /**
     * Verify string
     *
     * @param input
     * @return
     */
    private String verifyString(String input) {
        if (input == null) return "";
        String tmp = input.replaceAll("\\s{2,}", " ").toUpperCase().replaceAll("[+^,]", "").trim();
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
    private List<String> findInList(List<String> inputList, int index, PARSE parseType) {
        List<String> list = new ArrayList<>();
        List<String> top = addMatchedToList(inputList, index - 1, parseType);
        List<String> current = addMatchedToList(inputList, index, parseType);
        List<String> bottom = addMatchedToList(inputList, index + 1, parseType);

        list.addAll(top);
        list.addAll(current);
        list.addAll(bottom);
        if (list.size() == 0) {
            list.addAll(addMatchedToList(inputList, index + 2, parseType));
        }
        return list;
    }

    /**
     * Add item matched to result list
     *
     * @param inputList
     * @param index
     * @param parseType
     * @return
     */
    private List<String> addMatchedToList(List<String> inputList, int index, PARSE parseType) {
        List<String> list = new ArrayList<>();
        if (index >= 0 && index < inputList.size()) {
            String tmp = inputList.get(index);
            if (parseType == PARSE.AMOUNT) {
                String[] res = tmp.trim().split("\\s{2,}+");
                for (int i = 0; i < res.length; i++) {
                    if (res[i].contains(parseType.getRegx())) {
                        list.add(res[i].trim());
                    }
                }
            } else if (parseType == PARSE.DUE_DATE) {
                String[] res = tmp.trim().split("\\s{2,}+");
                for (int i = 0; i < res.length; i++) {
                    if (isDateValid(res[i].trim()) != null) {
                        list.add(res[i].trim());
                    }
                }
            } else if (parseType == PARSE.REF_NO) {
                String[] res = tmp.trim().split("\\s{2,}+");
                for (int i = 0; i < res.length; i++) {
                    if (!isValidDateWithFormat(res[i], "dd/MM/yyyy") && !res[i].contains("PAGE ")) {
                        Matcher matcher = match(res[i], parseType.getRegx());
                        if (matcher != null) {
                            list.add(res[i].trim());
                        }
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
    private int containsString(String input, String keyWord) {
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
    private Matcher match(String input, String regx) {
        if (input == null) return null;
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    /**
     * Find final result belong inputList and parse type
     *
     * @param inputList
     * @param parseType
     * @return
     */
    private String findFinalResult(List<String> inputList, PARSE parseType) {
        String finalResult = null;
        //Verify
        switch (parseType) {
            case AMOUNT:
                String max = null;
                if (inputList.size() > 0) {
                    String[] splits = inputList.get(inputList.size() - 1).replace("( ", "").replace(" )", "").replace("LIP ", "").split(" ");
                    if (splits.length > 0) {
                        max = splits[0].replace("$", "").replaceAll("\\s", "").replace("CR", "");
                        if (max.isEmpty()) max = null;
                    }
                }
                finalResult = max;
                break;
            case REF_NO:
                max = null;
                if (inputList.size() > 0) {
                    max = inputList.get(inputList.size() - 1).replace("( ", "").replace(" )", "").replace("LIP ", "");
                    if (!max.replaceAll("\\s", "").matches(PARSE.REF_NO.getRegx())) {
                        max = null;
                    }
                }
                finalResult = max;
                break;
            case DUE_DATE:
                Date tmp;
                boolean hasValue = false;
                Date minDate = new Date();
                for (String s : inputList) {
                    tmp = isDateValid(s);
                    if (tmp != null && minDate.compareTo(tmp) > 0) {
                        minDate = tmp;
                        hasValue = true;
                    }
                }
                if (hasValue) {
                    finalResult = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(minDate).replace("00", "");
                }
                break;
            case COMPANY:
                max = null;
                if (inputList.size() > 0) {
                    String[] token = parseType.getKeyWord().split("[|]");
                    Map<String, Integer> tokenMap = new LinkedHashMap<>();

                    for (String s : inputList) {
                        String[] subStr = s.split(" ");
                        for (String s1 : subStr) {
                            int index = containsString(s1, parseType.getKeyWord());
                            if (index > -1) {
                                for (String s2 : token) {
                                    if (s1.substring(index).startsWith(s2)) {
                                        if (tokenMap.containsKey(s2)) {
                                            tokenMap.put(s2, tokenMap.get(s2) + 1);
                                        } else {
                                            tokenMap.put(s2, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (tokenMap.size() > 0) {
                        Pair<String, Integer> maxItem = null;
                        for (Map.Entry<String, Integer> stringIntegerEntry : tokenMap.entrySet()) {
                            if (maxItem == null) {
                                maxItem = new Pair<>(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                            } else if (maxItem.getValue() < stringIntegerEntry.getValue()) {
                                maxItem = new Pair<>(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                            }
                        }
                        if (maxItem != null)
                            max = maxItem.getKey();
                    }
                }
                finalResult = max;
                break;
        }
        return finalResult;
    }

    private enum PARSE {
        DUE_DATE(
                "INVOICE DATE|DEBIT ON|DUE DATE|TOTAL DUE|PAYMENT DUE|PAYMENT DUE BY|DATE TO BE DEBITED|DIRECT DEBIT ON|DIRECT DEBITON|CHARGES DUE|CHARGES OLE|DATE DUE",
                "(\\d+)(\\/|-|\\s|\\.)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC|\\d+|)(\\/|-|\\s|\\.)(\\d+)"),

        AMOUNT(
                "AMOUNT|DEBITED|TOTAL|PAYMENT DUE|TOTAL DUE|AMOUNT DUE|TOTAL PAYABLE",
                "$"),

        REF_NO(
                "ACCOUNT NUMBER|CUSTOMER NUMBER|REFERENCE|PAYMENT NUMBER",
                "(\\d+)"),

        COMPANY("", "[A-Za-z]");

        private String keyWord;
        private String regx;

        PARSE(String keyWord, String regx) {
            this.keyWord = keyWord;
            this.regx = regx;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getRegx() {
            return regx;
        }

    }

    public static class Info implements Serializable {
        private String dueDate;
        private String amount;
        private String refNo;
        private String companyName;

        public Info(String dueDate, String amount, String refNo, String companyName) {
            this.dueDate = dueDate;
            this.amount = amount;
            this.refNo = refNo;
            this.companyName = companyName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getDueDate() {
            return dueDate;
        }

        public String getAmount() {
            return amount;
        }

        public String getRefNo() {
            return refNo;
        }

        public boolean isNull() {
            return (dueDate == null && amount == null && refNo == null);
        }

        @Override
        public String toString() {
            return "Info{" +
                    "dueDate='" + dueDate + '\'' +
                    ", amount='" + amount + '\'' +
                    ", refNo='" + refNo + '\'' +
                    ", companyName='" + companyName + '\'' +
                    '}';
        }
    }

}
