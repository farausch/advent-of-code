import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class aoc {

    public static String INPUT_FILE_PATH = "input.txt";

    private static List<List<Character>> getInputData(String inputFilePath) {
        List<List<Character>> inputData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Character> row = line.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
                inputData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    private static void prettyPrint(List<List<Character>> data) {
        for (List<Character> row : data) {
            for (Character c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static Map<Character, List<Map<Integer, Integer>>> getAntennasWithCoordinates(List<List<Character>> data) {
        Map<Character, List<Map<Integer, Integer>>> antennasWithCoordinates = new HashMap<>();
        for (int row = 0; row < data.size(); row++) {
            for (int col = 0; col < data.get(row).size(); col++) {
                Character c = data.get(row).get(col);
                if (c != '.') {
                    if (!antennasWithCoordinates.containsKey(c)) {
                        antennasWithCoordinates.put(c, new ArrayList<>());
                    }
                    Map<Integer, Integer> coordinates = new HashMap<>();
                    coordinates.put(row, col);
                    antennasWithCoordinates.get(c).add(coordinates);
                }
            }
        }
        return antennasWithCoordinates;
    }

    private static Map<Character, List<Map<Integer, Integer>>> getAntinodes(Map<Character, List<Map<Integer, Integer>>> antennasWithCoordinates) {
        Map<Character, List<Map<Integer, Integer>>> antinodes = new HashMap<>();
        for (Map.Entry<Character, List<Map<Integer, Integer>>> entry : antennasWithCoordinates.entrySet()) {
            Character antenna = entry.getKey();
            List<Map<Integer, Integer>> coordinates = entry.getValue();
            for (int c1 = 0; c1 < coordinates.size(); c1++) {
                for (int c2 = c1 + 1; c2 < coordinates.size(); c2++) {
                    if (c1 != c2) {
                        int x1 = coordinates.get(c1).keySet().iterator().next();
                        int y1 = coordinates.get(c1).values().iterator().next();
                        int x2 = coordinates.get(c2).keySet().iterator().next();
                        int y2 = coordinates.get(c2).values().iterator().next();
                        int x1Antinode = x1 + (x1 - x2);
                        int y1Antinode = y1 + (y1 - y2);
                        int x2Antinode = x2 + (x2 - x1);
                        int y2Antinode = y2 + (y2 - y1);
                        antinodes.putIfAbsent(antenna, new ArrayList<>());
                        Map<Integer, Integer> antinode1 = new HashMap<>();
                        antinode1.put(x1Antinode, y1Antinode);
                        antinodes.get(antenna).add(antinode1);
                        Map<Integer, Integer> antinode2 = new HashMap<>();
                        antinode2.put(x2Antinode, y2Antinode);
                        antinodes.get(antenna).add(antinode2);
                    }
                }
            }
        }
        return antinodes;
    }

    private static Map<Character, List<Map<Integer, Integer>>> getAntinodesExtended(Map<Character, List<Map<Integer, Integer>>> antennasWithCoordinates, int rows, int cols) {
        Map<Character, List<Map<Integer, Integer>>> antinodes = new HashMap<>();
        for (Map.Entry<Character, List<Map<Integer, Integer>>> entry : antennasWithCoordinates.entrySet()) {
            Character antenna = entry.getKey();
            List<Map<Integer, Integer>> coordinates = entry.getValue();
            for (int c1 = 0; c1 < coordinates.size(); c1++) {
                for (int c2 = c1 + 1; c2 < coordinates.size(); c2++) {
                    if (c1 != c2) {
                        int x1 = coordinates.get(c1).keySet().iterator().next();
                        int y1 = coordinates.get(c1).values().iterator().next();
                        int x2 = coordinates.get(c2).keySet().iterator().next();
                        int y2 = coordinates.get(c2).values().iterator().next();
                        boolean isCoordinate1OffGrid = false, isCoordinate2OffGrid = false;
                        int multiplier = 0;
                        while (!isCoordinate1OffGrid || !isCoordinate2OffGrid) {
                            int x1Antinode = x1 + multiplier * (x1 - x2);
                            int y1Antinode = y1 + multiplier * (y1 - y2);
                            int x2Antinode = x2 + multiplier * (x2 - x1);
                            int y2Antinode = y2 + multiplier * (y2 - y1);
                            antinodes.putIfAbsent(antenna, new ArrayList<>());
                            Map<Integer, Integer> antinode1 = new HashMap<>();
                            antinode1.put(x1Antinode, y1Antinode);
                            antinodes.get(antenna).add(antinode1);
                            Map<Integer, Integer> antinode2 = new HashMap<>();
                            antinode2.put(x2Antinode, y2Antinode);
                            antinodes.get(antenna).add(antinode2);
                            if (x1Antinode < 0 || x1Antinode >= rows || y1Antinode < 0 || y1Antinode >= cols) {
                                isCoordinate1OffGrid = true;
                            }
                            if (x2Antinode < 0 || x2Antinode >= rows || y2Antinode < 0 || y2Antinode >= cols) {
                                isCoordinate2OffGrid = true;
                            }
                            multiplier++;
                        }
                    }
                }
            }
        }
        return antinodes;
    }

    private static Map<Character, List<Map<Integer, Integer>>> filterOffGridCoordinates(Map<Character, List<Map<Integer, Integer>>> antinodes, int rows, int cols) {
        Map<Character, List<Map<Integer, Integer>>> filteredAntinodes = new HashMap<>();
        for (Map.Entry<Character, List<Map<Integer, Integer>>> entry : antinodes.entrySet()) {
            Character antenna = entry.getKey();
            List<Map<Integer, Integer>> coordinates = entry.getValue();
            List<Map<Integer, Integer>> filteredCoordinates = new ArrayList<>();
            for (Map<Integer, Integer> coordinate : coordinates) {
                int x = coordinate.keySet().iterator().next();
                int y = coordinate.values().iterator().next();
                if (x >= 0 && x < rows && y >= 0 && y < cols) {
                    filteredCoordinates.add(coordinate);
                }
            }
            if (!filteredCoordinates.isEmpty()) {
                filteredAntinodes.put(antenna, filteredCoordinates);
            }
        }
        return filteredAntinodes;
    }

    private static int countUniqueAntinodes(Map<Character, List<Map<Integer, Integer>>> antinodes) {
        Set<Map<Integer, Integer>> uniqueAntinodes = new HashSet<>();
        for (List<Map<Integer, Integer>> coordinates : antinodes.values()) {
            uniqueAntinodes.addAll(coordinates);
        }
        return uniqueAntinodes.size();
    }

    public static void main(String[] args) {
        List<List<Character>> inputData = getInputData(INPUT_FILE_PATH);
        Map<Character, List<Map<Integer, Integer>>> antennasWithCoordinates = getAntennasWithCoordinates(inputData);

        // Section A
        Map<Character, List<Map<Integer, Integer>>> antinodes = getAntinodes(antennasWithCoordinates);
        Map<Character, List<Map<Integer, Integer>>> filteredOffGridAntinodes = filterOffGridCoordinates(antinodes, inputData.size(), inputData.get(0).size());
        System.out.println("Section A unique antinodes: " + countUniqueAntinodes(filteredOffGridAntinodes));

        // Section B
        Map<Character, List<Map<Integer, Integer>>> antinodesExtended = getAntinodesExtended(antennasWithCoordinates, inputData.size(), inputData.get(0).size());
        Map<Character, List<Map<Integer, Integer>>> filteredOffGridAntinodesExtended = filterOffGridCoordinates(antinodesExtended, inputData.size(), inputData.get(0).size());
        System.out.println("Section B unique antinodes: " + countUniqueAntinodes(filteredOffGridAntinodesExtended));
    }
}
