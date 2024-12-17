import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class aoc {

    public static String INPUT_FILE_PATH = "input.txt";

    private static Map<Long, List<Integer>> getInputData(String inputFilePath) {
        Map<Long, List<Integer>> inputData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                Long key = Long.parseLong(parts[0]);
                List<Integer> values = Arrays.asList(parts[1].trim().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
                inputData.put(key, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static void main(String[] args) {
        Map<Long, List<Integer>> inputData = getInputData(INPUT_FILE_PATH);
        List<Long> validKeys = new ArrayList<>();
        for (Map.Entry<Long, List<Integer>> entry : inputData.entrySet()) {
            Long key = entry.getKey();
            List<Integer> values = entry.getValue();
            List<Long> possibleResults = new ArrayList<>();
            possibleResults.add(values.get(0).longValue());
            for (int i = 1; i < values.size(); i++) {
                List<Long> newPossibleResults = new ArrayList<>();
                for (int j = 0; j < possibleResults.size(); j++) {
                    newPossibleResults.add(possibleResults.get(j) + values.get(i));
                    newPossibleResults.add(possibleResults.get(j) * values.get(i));
                    // Section B
                    newPossibleResults.add(Long.parseLong(possibleResults.get(j).toString() + values.get(i).toString()));
                }
                possibleResults = new ArrayList<>(newPossibleResults);
            }
            if (possibleResults.contains(key)) {
                validKeys.add(key);
            }
        }
        System.out.println(validKeys.stream().mapToLong(Long::longValue).sum());
    }
}
