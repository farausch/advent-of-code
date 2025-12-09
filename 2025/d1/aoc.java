import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class AOC {

    public static final String INPUT_FILE_PATH = "input_part_2.txt";

    public record SingleSafeDialInstruction(String direction, int stepsAmount) { }
    public record SingleSafeDialResult(int zeroes, int finalPosition) { }

    private static List<SingleSafeDialInstruction> getInputData(String inputFilePath) {
        List<SingleSafeDialInstruction> inputData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String direction = line.charAt(0) + "";
                int stepsAmount = Integer.parseInt(line.substring(1).trim());
                inputData.add(new SingleSafeDialInstruction(direction, stepsAmount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    private static SingleSafeDialResult dialSafe(SingleSafeDialInstruction dial, int currentPosition) {
        String direction = dial.direction();
        int stepsAmount = dial.stepsAmount();
        int zeroCount = 0;
        if (direction.equals("R")) {
            for (int i = 0; i < stepsAmount; i += 1) {
                currentPosition += 1;
                if (currentPosition % 100 == 0) {
                    zeroCount += 1;
                }
            }
        }
        else if (direction.equals("L")) {
            for (int i = 0; i < stepsAmount; i += 1) {
                currentPosition -= 1;
                if (currentPosition % 100 == 0) {
                    zeroCount += 1;
                }
            }
        }
        return new SingleSafeDialResult(zeroCount, currentPosition);
    }

    void main() {
        List<SingleSafeDialInstruction> inputData = getInputData(INPUT_FILE_PATH);

        int currentPosition = 50;
        int amountZeroEndpoints = 0;
        int amountZeroHitsAll = 0;

        for (SingleSafeDialInstruction dial : inputData) {
            SingleSafeDialResult result = dialSafe(dial, currentPosition);
            amountZeroHitsAll += result.zeroes();
            currentPosition = result.finalPosition();
            if (currentPosition % 100 == 0) {
                amountZeroEndpoints += 1;
            }
        }

        System.out.println("Amount of zero endpoints: " + amountZeroEndpoints);
        System.out.println("Amount of zero hits all: " + amountZeroHitsAll);
    }

}