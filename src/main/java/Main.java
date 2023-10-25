import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        ArrayList<GridAndWords> wholeFile = readFile(args[0]);
        try {
            if (wholeFile == null) throw new NullPointerException();

            ArrayList<HashMap<Character, ArrayList<int[]>>> charAndPositions = charAndPositionsArrayList(wholeFile);

            for (int query = 0; query < wholeFile.size(); query++) {
                System.out.println("Query " + (query + 1) + ":");
                HashMap<Character, ArrayList<int[]>> queryCharAndPositions = charAndPositions.get(query);
                findWords(wholeFile.get(query).getWords(), queryCharAndPositions);
            }
        } catch (NullPointerException e) {
            System.out.println("Cannot process queries.");
        }
    }

    /**
     * readFile reads a text file and returns an array list containing the queries from the file.
     *
     * @param fileName  the name of the file to be read
     * @return          an array list containing the file's queries
     */
    public static ArrayList<GridAndWords> readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int gridHeight;
            int gridWidth;
            ArrayList<GridAndWords> wholeFile = new ArrayList<>();

            while (true) {
                String line = br.readLine();

                if (line == null) break;

                gridHeight = Integer.parseInt(line.split(" ")[0]);
                gridWidth = Integer.parseInt(line.split(" ")[1]);

                char[][] grid = new char[gridHeight][gridWidth];

                for (int row = 0; row < gridHeight; row++) {
                    line = br.readLine();

                    for (int column = 0; column < gridWidth; column++) {
                        grid[row][column] = line.split(" ")[column].charAt(0);
                    }
                }

                String[] wordsLine = br.readLine().split(" ");
                GridAndWords query = new GridAndWords(grid, wordsLine);
                wholeFile.add(query);
            }

            return wholeFile;
        } catch (IOException e) {
            System.out.println("Unable to read file " + fileName);
        }
        return null;
    }

    /**
     * charAndPositionsHashMap uses the grid of a query and returns a hashmap that contains characters as keys and array
     * lists of positions as values.
     *
     * @param query query which the grid is taken from
     * @return      a hashmap with characters as keys and array lists of positions as values. A position is represented
     *              as an array of integers with length 2.
     */
    public static HashMap<Character, ArrayList<int[]>> charAndPositionsHashMap(GridAndWords query) {
        HashMap<Character, ArrayList<int[]>> hashMap = new HashMap<>();
        char[][] grid = query.getGrid();

        for (int row = 0; row < grid.length; row++ ) {
            for (int column = 0; column < grid[row].length; column++) {
                char letter = grid[row][column];
                int[] position = {row, column};

                hashMap.computeIfAbsent(letter, k -> new ArrayList<>()).add(position);
            }
        }
        return hashMap;
    }

    /**
     * charAndPositionsArrayList creates an array list of hashmaps from an array of queries.
     *
     * @param wholeFile an array list of GridAndWords objects
     * @return          an array list of hashmaps
     */
    public static ArrayList<HashMap<Character, ArrayList<int[]>>> charAndPositionsArrayList(ArrayList<GridAndWords> wholeFile) {
        ArrayList<HashMap<Character, ArrayList<int[]>>> arrayList = new ArrayList<>();
        for (GridAndWords query : wholeFile) {
            HashMap<Character, ArrayList<int[]>> hashMap = charAndPositionsHashMap(query);
            arrayList.add(hashMap);
        }
        return arrayList;
    }


    /**
     * Searches for all possible paths of a word within a grid and returns them.
     *
     * @param word the target word to search for.
     * @param charAndPositions a hashmap containing characters as keys and lists of their positions in the grid as values.
     * @return a list of paths, where each path is an array of positions (i, j) indicating where each character of the word can be found in the grid.
     */
    public static List<int[][]> findWord(String word, HashMap<Character, ArrayList<int[]>> charAndPositions) {
        // Initialize the collection of paths
        List<int[][]> allPaths = new ArrayList<>();

        // Start the search with the first character of the word
        if (charAndPositions.containsKey(word.charAt(0))) {
            for (int[] start : charAndPositions.get(word.charAt(0))) {
                int[][] currentPath = new int[word.length()][];
                currentPath[0] = start;
                search(word, 1, charAndPositions, currentPath, allPaths);
            }
        }
        // Once the search has added all paths to the collection, return it
        return allPaths;
    }

    /**
     * Recursively searches for paths of the word within the grid starting from a given index.
     *
     * @param word the target word to search for.
     * @param wordIndex the current index within the word.
     * @param charAndPositions a hashmap containing characters as keys and lists of their positions in the grid as values.
     * @param currentPath the current path being formed.
     * @param allPaths a collection of all found paths for the word.
     */
    private static void search(String word, int wordIndex, HashMap<Character, ArrayList<int[]>> charAndPositions, int[][] currentPath, List<int[][]> allPaths) {
        // If the word has been found, add the current path to the collection of paths
        if (wordIndex == word.length()) {
            allPaths.add(currentPath.clone());
            return;
        }

        // Get the next character and the last position inputted in the path
        char nextChar = word.charAt(wordIndex);
        int[] lastPosition = currentPath[wordIndex - 1];

        if (charAndPositions.containsKey(nextChar)) {
            for (int[] nextPos : charAndPositions.get(nextChar)) {
                // Check if next position is adjacent (or the same) to the last position
                if (Math.abs(nextPos[0] - lastPosition[0]) <= 1 && Math.abs(nextPos[1] - lastPosition[1]) <= 1) {
                    currentPath[wordIndex] = nextPos;
                    search(word, wordIndex + 1, charAndPositions, currentPath, allPaths);
                }
            }
        }
    }

    /**
     * Searches for all words in the provided list within the grid and prints their paths.
     *
     * @param words an array of words to search for.
     * @param charAndPositions a hashmap containing characters as keys and lists of their positions in the grid as values.
     */
    public static void findWords(String[] words, HashMap<Character, ArrayList<int[]>> charAndPositions) {
        ArrayList<String> output = new ArrayList<>();

        for (String word : words) {
            List<int[][]> wordPaths = findWord(word, charAndPositions);

            for (int[][] path : wordPaths) {
                StringBuilder sb = new StringBuilder(word + " ");
                for (int i = 0; i < path.length; i++) {
                    sb.append("(").append(path[i][0]).append(",").append(path[i][1]).append(")");
                    if (i < path.length - 1) sb.append("->");
                }
                output.add(sb.toString());
            }
        }

        Collections.sort(output);
        for (String line : output) {
            System.out.println(line);
        }
    }
}