import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class aoc {

    public static String INPUT_FILE_PATH = "input.txt";

    private static List<Integer> getInputData(String inputFilePath) {
        List<Integer> inputData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line = reader.readLine();
            String[] blocks = line.split("");
            Arrays.stream(blocks).forEach(block -> inputData.add(Integer.parseInt(block)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    private static List<Integer> getDecompressedBlocks(List<Integer> compressedBlocks) {
        List<Integer> decompressedBlocks = new ArrayList<>();
        for (int i = 0; i < compressedBlocks.size(); i++) {
            int block = compressedBlocks.get(i);
            if (i % 2 == 0) {
                for (int j = 0; j < block; j++) {
                    int div = i / 2;
                    decompressedBlocks.add(div);
                }
            } else {
                for (int j = 0; j < block; j++) {
                    decompressedBlocks.add(-1);
                }
            }
        }
        return decompressedBlocks;
    }

    private static int getFirstOpenSpot(List<Integer> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == -1) {
                return i;
            }
        }
        return -1;
    }

    private static List<Integer> shiftLeftBlock(List<Integer> blocks, int from, int to) {
        List<Integer> newBlocks = new ArrayList<>(blocks);
        newBlocks.set(to, newBlocks.get(from));
        newBlocks.set(from, -1);
        return newBlocks;
    }

    private static boolean isBlockShiftLeftPossible(List<Integer> blocks) {
        int firstOpenSpot = getFirstOpenSpot(blocks);
        for (int i = firstOpenSpot; i < blocks.size(); i++) {
            if (blocks.get(i) != -1) {
                return true;
            }
        }
        return false;
    }

    private static List<Integer> defragmentBlocks(List<Integer> blocks) {
        List<Integer> defragmentedBlocks = new ArrayList<>(blocks);
        int currentIndex = blocks.size() - 1;
        while (isBlockShiftLeftPossible(defragmentedBlocks)) {
            defragmentedBlocks = shiftLeftBlock(defragmentedBlocks, currentIndex, getFirstOpenSpot(defragmentedBlocks));
            currentIndex--;
        }
        return defragmentedBlocks;
    }

    private static long calculateFileSystemChecksum(List<Integer> blocks) {
        int lastCheckIndex = getFirstOpenSpot(blocks);
        return IntStream.range(0, lastCheckIndex)
                .mapToLong(i -> blocks.get(i) * i)
                .sum();
    }

    private static List<String> getPrettifiedBlocks(List<Integer> blocks) {
        List<String> prettifiedBlocks = new ArrayList<>();
        for (int block : blocks) {
            if (block == -1) {
                prettifiedBlocks.add(".");
            } else {
                prettifiedBlocks.add(String.valueOf(block));
            }
        }
        return prettifiedBlocks;
    }

    public static void main(String[] args) {
        List<Integer> inputData = getInputData(INPUT_FILE_PATH);
        List<Integer> decompressedBlocks = getDecompressedBlocks(inputData);
        List<Integer> defragmentedBlocks = defragmentBlocks(decompressedBlocks);
        long checksum = calculateFileSystemChecksum(defragmentedBlocks);
        System.out.println("Section A checksum: " + checksum);
    }
}
