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

    private static List<Integer> shiftLeftSingleBlock(List<Integer> blocks, int from, int to) {
        List<Integer> newBlocks = new ArrayList<>(blocks);
        newBlocks.set(to, newBlocks.get(from));
        newBlocks.set(from, -1);
        return newBlocks;
    }

    private static List<Integer> shiftLeftMultiBlock(List<Integer> blocks, int multiBlockLeftIdx, int multiBlockRightIdx) {
        List<Integer> newBlocks = new ArrayList<>(blocks);
        int fileId = newBlocks.get(multiBlockLeftIdx);
        int multiBlockSize = multiBlockRightIdx - multiBlockLeftIdx + 1;
        int suitableGapStart = getSuitableMultiBlockGapStart(newBlocks, multiBlockLeftIdx, multiBlockRightIdx);
        if (suitableGapStart == -1) {
            return newBlocks;
        }
        for (int i = 0; i < multiBlockSize; i++) {
            newBlocks.set(suitableGapStart + i, fileId);
            newBlocks.set(multiBlockLeftIdx + i, -1);
        }
        return newBlocks;
    }

    private static boolean isSingleBlockShiftLeftPossible(List<Integer> blocks) {
        int firstOpenSpot = getFirstOpenSpot(blocks);
        for (int i = firstOpenSpot; i < blocks.size(); i++) {
            if (blocks.get(i) != -1) {
                return true;
            }
        }
        return false;
    }

    private static int getSuitableMultiBlockGapStart(List<Integer> blocks, int multiBlockLeftIdx, int multiBlockRightIdx) {
        int multiBlockSize = multiBlockRightIdx - multiBlockLeftIdx + 1;
        for (int i = 0; i < multiBlockLeftIdx; i++) {
            if (blocks.get(i) == -1) {
                int gapSize = 0;
                for (int j = i; j < blocks.size(); j++) {
                    if (blocks.get(j) == -1) {
                        gapSize++;
                    } else {
                        break;
                    }
                }
                if (gapSize >= multiBlockSize) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int getMultiBlockLeftIdx(List<Integer> blocks, int multiBlockRightIdx) {
        int fileId = blocks.get(multiBlockRightIdx);
        int multiBlockLeftIdx = multiBlockRightIdx;
        while (blocks.get(multiBlockLeftIdx) == fileId) {
            if (multiBlockLeftIdx == 0) {
                return 0;
            }
            multiBlockLeftIdx--;
        }
        return multiBlockLeftIdx + 1;
    }

    private static List<Integer> defragmentSingleBlocks(List<Integer> blocks) {
        List<Integer> defragmentedBlocks = new ArrayList<>(blocks);
        int currentIndex = blocks.size() - 1;
        while (isSingleBlockShiftLeftPossible(defragmentedBlocks)) {
            defragmentedBlocks = shiftLeftSingleBlock(defragmentedBlocks, currentIndex, getFirstOpenSpot(defragmentedBlocks));
            currentIndex--;
        }
        return defragmentedBlocks;
    }

    private static List<Integer> defragmentMultiBlocks(List<Integer> blocks) {
        List<Integer> defragmentedBlocks = new ArrayList<>(blocks);
        int currentPointer = defragmentedBlocks.size() - 1;
        while (currentPointer >= 0) {
            if (defragmentedBlocks.get(currentPointer) != -1) {
                int multiBlockRightIdx = currentPointer;
                int multiBlockLeftIdx = getMultiBlockLeftIdx(defragmentedBlocks, multiBlockRightIdx);
                defragmentedBlocks = shiftLeftMultiBlock(defragmentedBlocks, multiBlockLeftIdx, multiBlockRightIdx);
                currentPointer = multiBlockLeftIdx - 1;
            } else {
                currentPointer--;
            }
        }
        return defragmentedBlocks;
    }

    private static long calculateFileSystemChecksum(List<Integer> blocks) {
        return IntStream.range(0, blocks.size() - 1)
                .filter(i -> blocks.get(i) != -1)
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

        // Section A
        List<Integer> defragmentedBlocks = defragmentSingleBlocks(decompressedBlocks);
        long checksumSingleBlocks = calculateFileSystemChecksum(defragmentedBlocks);
        System.out.println("Section A checksum: " + checksumSingleBlocks);

        // Section B
        List<Integer> defragmentedMultiBlocks = defragmentMultiBlocks(decompressedBlocks);
        long checksumMultiBlocks = calculateFileSystemChecksum(defragmentedMultiBlocks);
        System.out.println("Section B checksum: " + checksumMultiBlocks);
    }
}
