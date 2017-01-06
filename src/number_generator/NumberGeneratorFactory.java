package number_generator;

import base.Factory;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by nhancao on 1/6/17.
 */
public class NumberGeneratorFactory implements Factory {

    private String outputFileName = "res/number_generator/output.txt";
    private Charset charset = Charset.forName("UTF-8");

    @Override
    public void run() {
        genNumListFixLength(6);
    }

    public void genNumListFixLength(int numDigits) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName), charset);
            long left = (long) Math.pow(10, numDigits - 1);
            long right = (long) Math.pow(10, numDigits) - 1;
            for (long i = left; i <= right; i++) {
                writer.write(String.valueOf(i) + "\n");
            }
            System.out.println("Generate number list successfully with " + (right - left + 1) + " nums.");
            writer.close();
        } catch (Exception ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    public void permutation(String str) {
        permutation("", str);
    }

    private void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
        }
    }


}
