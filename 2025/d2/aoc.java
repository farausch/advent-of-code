import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class AOC {

    public static final String INPUT_FILE_PATH = "input.txt";

    public record IdRange(long start, long end) {}

    private static List<IdRange> getInputData(String inputFilePath) {
        List<IdRange> inputData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line = reader.readLine();
            String[] ranges = line.split(",");
            for (String range : ranges) {
                String[] bounds = range.split("-");
                long start = Long.parseLong(bounds[0]);
                long end = Long.parseLong(bounds[1]);
                inputData.add(new IdRange(start, end));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static boolean isConcatenationTwice(String str) {
        int len = str.length();
        if (len % 2 != 0) {
            return false;
        }
        String firstHalf = str.substring(0, len / 2);
        String secondHalf = str.substring(len / 2);
        return firstHalf.equals(secondHalf);
    }

    public static boolean isConcatenationNtimes(String str) {
        for (int i = 1; i < str.length(); i += 1) {
            String substr = str.substring(0, i);
            String repeated = substr.repeat(str.length() / i);
            if (repeated.equals(str)) {
                return true;
            }
        }
        return false;
    }

    void main() {
        List<IdRange> inputData = getInputData(INPUT_FILE_PATH);
        long concatTwiceSum = 0L;
        for (IdRange range : inputData) {
            for (long numValue = range.start; numValue <= range.end; numValue += 1) {
                if (isConcatenationTwice(String.valueOf(numValue))) {
                    concatTwiceSum += numValue;
                }
            }
        }
        System.out.println("Sum (twice concatenated): " + concatTwiceSum);
        long concatNTimesSum = 0L;
        for (IdRange range : inputData) {
            for (long numValue = range.start; numValue <= range.end; numValue += 1) {
                if (isConcatenationNtimes(String.valueOf(numValue))) {
                    concatNTimesSum += numValue;
                }
            }
        }
        System.out.println("Sum (n times concatenated): " + concatNTimesSum);
    }

}