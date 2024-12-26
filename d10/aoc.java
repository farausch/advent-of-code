import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class aoc {

    public static String INPUT_FILE_PATH = "input.txt";

    private static List<List<Integer>> getInputMatrix(String inputFilePath) {
        List<List<Integer>> inputMatrix = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Integer> row = new ArrayList<>();
                for (String num : line.split("")) {
                    row.add(Integer.parseInt(num));
                }
                inputMatrix.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputMatrix;
    }

    private static int[][] convertMatrix(List<List<Integer>> matrix) {
        int[][] result = new int[matrix.size()][matrix.get(0).size()];
        IntStream.range(0, matrix.size()).forEach(i -> {
            List<Integer> row = matrix.get(i);
            IntStream.range(0, row.size()).forEach(j -> {
                result[i][j] = row.get(j);
            });
        });
        return result;
    }

    private static boolean isTopStepValid(int[][] matrix, int i, int j) {
        return i - 1 >= 0 && matrix[i - 1][j] == matrix[i][j] + 1;
    }

    private static boolean isDownStepValid(int[][] matrix, int i, int j) {
        return i + 1 < matrix.length && matrix[i + 1][j] == matrix[i][j] + 1;
    }

    private static boolean isLeftStepValid(int[][] matrix, int i, int j) {
        return j - 1 >= 0 && matrix[i][j - 1] == matrix[i][j] + 1;
    }

    private static boolean isRightStepValid(int[][] matrix, int i, int j) {
        return j + 1 < matrix[0].length && matrix[i][j + 1] == matrix[i][j] + 1;
    }

    private static Set<Map<Integer, Integer>> getTrailends(int[][] matrix, Set<Map<Integer, Integer>> trailends, int startI, int startJ) {
        if (matrix[startI][startJ] == 9) {
            Map<Integer, Integer> trailend = new HashMap<>();
            trailend.put(startI, startJ);
            trailends.add(trailend);
            return trailends;
        }
        if (isTopStepValid(matrix, startI, startJ)) {
            trailends.addAll(getTrailends(matrix, trailends, startI - 1, startJ));
        }
        if (isDownStepValid(matrix, startI, startJ)) {
            trailends.addAll(getTrailends(matrix, trailends, startI + 1, startJ));
        }
        if (isLeftStepValid(matrix, startI, startJ)) {
            trailends.addAll(getTrailends(matrix, trailends, startI, startJ - 1));
        }
        if (isRightStepValid(matrix, startI, startJ)) {
            trailends.addAll(getTrailends(matrix, trailends, startI, startJ + 1));
        }
        return trailends;
    }

    private static Map<Map<Integer, Integer>, Set<Map<Integer, Integer>>> getAllTrails(int[][] matrix) {
        Map<Map<Integer, Integer>, Set<Map<Integer, Integer>>> trailends = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    continue;
                }
                Set<Map<Integer, Integer>> trailendsSet = getTrailends(matrix, new HashSet<>(), i, j);
                if (!trailendsSet.isEmpty()) {
                    Map<Integer, Integer> start = new HashMap<>();
                    start.put(i, j);
                    trailends.put(start, trailendsSet);
                }
            }
        }
        return trailends;
    }

    private static int calculateTrailheadSum(Map<Map<Integer, Integer>, Set<Map<Integer, Integer>>> trailends) {
        int sum = 0;
        for (Map.Entry<Map<Integer, Integer>, Set<Map<Integer, Integer>>> entry : trailends.entrySet()) {
            Set<Map<Integer, Integer>> trailendsSet = entry.getValue();
            sum += trailendsSet.size();
        }
        return sum;
    }

    private static void depth_first_search(int[][] matrix, int i, int j, List<List<Map<Integer, Integer>>> allPaths, List<Map<Integer, Integer>> currentPath) {
        if (matrix[i][j] == 9) {
            currentPath.add(Map.of(i, j));
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }
        currentPath.add(Map.of(i, j));
        if (isTopStepValid(matrix, i, j)) {
            depth_first_search(matrix, i - 1, j, allPaths, currentPath);
        }
        if (isDownStepValid(matrix, i, j)) {
            depth_first_search(matrix, i + 1, j, allPaths, currentPath);
        }
        if (isLeftStepValid(matrix, i, j)) {
            depth_first_search(matrix, i, j - 1, allPaths, currentPath);
        }
        if (isRightStepValid(matrix, i, j)) {
            depth_first_search(matrix, i, j + 1, allPaths, currentPath);
        }
        //currentPath.remove(currentPath.size() - 1);
    }

    private static List<List<Map<Integer, Integer>>> getPaths(int[][] matrix, int i, int j) {
        List<List<Map<Integer, Integer>>> allPaths = new ArrayList<>();
        List<Map<Integer, Integer>> currentPath = new ArrayList<>();
        depth_first_search(matrix, i, j, allPaths, currentPath);
        return allPaths;
    }

    private static Map<Map<Integer, Integer>, List<List<Map<Integer, Integer>>>> getAllPaths(int[][] matrix) {
        Map<Map<Integer, Integer>, List<List<Map<Integer, Integer>>>> paths = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    continue;
                }
                List<List<Map<Integer, Integer>>> allPaths = getPaths(matrix, i, j);
                if (!allPaths.isEmpty()) {
                    Map<Integer, Integer> start = new HashMap<>();
                    start.put(i, j);
                    paths.put(start, allPaths);
                }
            }
        }
        return paths;
    }

    private static int calculatePathSum(Map<Map<Integer, Integer>, List<List<Map<Integer, Integer>>>> paths) {
        int sum = 0;
        for (Map.Entry<Map<Integer, Integer>, List<List<Map<Integer, Integer>>>> entry : paths.entrySet()) {
            List<List<Map<Integer, Integer>>> allPaths = entry.getValue();
            sum += allPaths.size();
        }
        return sum;
    }

    public static void main(String[] args) {
        List<List<Integer>> inputMatrix = getInputMatrix(INPUT_FILE_PATH);
        int[][] matrix = convertMatrix(inputMatrix);

        // Section A
        Map<Map<Integer, Integer>, Set<Map<Integer, Integer>>> trails = getAllTrails(matrix);
        int trailheadSum = calculateTrailheadSum(trails);
        System.out.println("Section A trailhead sum: " + trailheadSum);

        // Section B
        Map<Map<Integer, Integer>, List<List<Map<Integer, Integer>>>> paths = getAllPaths(matrix);
        int pathSum = calculatePathSum(paths);
        System.out.println("Section B trailhead ratings: " + pathSum);
    }
}
