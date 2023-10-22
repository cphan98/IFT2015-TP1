import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<GridAndWords> wholeFile = readFile(args[0]);

        try {
            if (wholeFile == null) throw new NullPointerException();

            for (GridAndWords query : wholeFile) {
                System.out.println(query);
            }

            System.out.println();

            ArrayList<HashMap<Character, ArrayList<int[]>>> charAndPositionsArrayList = charAndPositionsArrayList(wholeFile);

            for (HashMap<Character, ArrayList<int[]>> query : charAndPositionsArrayList) {
                for (char key : query.keySet()) {
                    System.out.println(key + "=" + Arrays.deepToString(query.get(key).toArray()));
                }
                System.out.println();
            }

            wholeFile = updateWholeFile(wholeFile, charAndPositionsArrayList);

            for (GridAndWords query : wholeFile) {
                System.out.println(query.toString());
            }
        } catch (NullPointerException e) {
            System.out.println("Cannot process queries.");
        }

        System.out.println();
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
                Arrays.sort(wordsLine);
                GridAndWords query = new GridAndWords(grid, wordsLine);
                wholeFile.add(query);
            }

            return wholeFile;
        } catch (IOException e) {
            System.out.println("Unable to read fille " + fileName);
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
        int gridHeight = grid.length;
        int gridWidth = grid[0].length;

        for (int row = 0; row < gridHeight; row++ ) {
            for (int column = 0; column < gridWidth; column++) {
                char letter = grid[row][column];
                int[] position = new int[2];
                position[0] = row;
                position[1] = column;

                if (!hashMap.containsKey(letter)) {
                    ArrayList<int[]> positions = new ArrayList<>();
                    positions.add(position);
                    hashMap.put(letter, positions);
                    continue;
                }

                ArrayList<int[]> positions = hashMap.get(letter);
                positions.add(position);
                hashMap.replace(letter, positions);
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
     * For a given word, charIsInGrid verifies if all the characters are present in a given grid.
     *
     * @param charAndPositionsHashMap   a hashmap with characters as keys and array lists of positions as value
     * @param word                      a string; a word of a query
     * @return                          false if a character of the word is not present in the hashmap
     */
    public static boolean charsIsInGrid(HashMap<Character, ArrayList<int[]>> charAndPositionsHashMap, String word) {
        for (int letter = 0; letter < word.length(); letter++) {
            if (!charAndPositionsHashMap.containsKey(word.charAt(letter))) {
                return false;
            }
        }
        return true;
    }

    /**
     * wordsToRemove creates an array list of words that are not present in a given grid.
     *
     * @param charAndPositionsHashMap   a hashmap with characters as keys and array lists of positions as value
     * @param words                     an array of strings; words of a query
     * @return                          an array list of strings
     */
    public static ArrayList<String> wordsToRemove(HashMap<Character, ArrayList<int[]>> charAndPositionsHashMap, String[] words) {
        ArrayList<String> wordsToRemove = new ArrayList<>();
        for (String word : words) {
            if (!charsIsInGrid(charAndPositionsHashMap, word)) {
                wordsToRemove.add(word);
            }
        }

        return wordsToRemove;
    }

    /**
     * removeWords removes words not present in a given grid from the original array of words to be found, and creates
     * an updated array of words to be found.
     *
     * @param words         the original array of strings; original array of words
     * @param wordsToRemove an array list of strings; words to remove
     * @return              an array of strings; the updated array of words to be found
     */
    public static String[] removeWords(String[] words, ArrayList<String> wordsToRemove) {
        List<String> tempUpdatedWords = new ArrayList<>(Arrays.asList(words));

        for (String wordToRemove : wordsToRemove) {
            tempUpdatedWords.remove(wordToRemove);
        }

        return tempUpdatedWords.toArray(new String[0]);
    }

    /**
     * updateWholeFile creates a new array list of queries with their updated words.
     *
     * @param wholeFile                 the original array list of GridAndWords
     * @param charAndPositionsArrayList an array list of hashmaps
     * @return                          a new array list of GridAndWords
     */
    public static ArrayList<GridAndWords> updateWholeFile(ArrayList<GridAndWords> wholeFile, ArrayList<HashMap<Character, ArrayList<int[]>>> charAndPositionsArrayList) {
        ArrayList<GridAndWords> updatedWholeFile = new ArrayList<>();

        try {
            if (wholeFile.size() != charAndPositionsArrayList.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }

            for (int i = 0; i < wholeFile.size(); i++) {
                HashMap<Character, ArrayList<int[]>> charAndPositions = charAndPositionsArrayList.get(i);
                GridAndWords query = wholeFile.get(i);
                String[] words = query.getWords();

                ArrayList<String> wordsToRemove = wordsToRemove(charAndPositions, words);

                if (!wordsToRemove.isEmpty()) {
                    String[] updatedWords = removeWords(words, wordsToRemove);

                    query.setWords(updatedWords);
                    updatedWholeFile.add(query);

                    continue;
                }

                updatedWholeFile.add(query);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("The wholeFile length does not match the charAndPositionsArrayList length.");
        }

        return updatedWholeFile;
    }

    public static String findLocationInGrid(char[][] grid, int left, int right, int top, int bottom) {
        String positionInGrid = null;

        // possible locations in the grid
        // top left corner
        if (left == -1 && right == 1 && top == -1 && bottom == 1) positionInGrid = "topLeftCorner";
        // left extremity
        if (left == -1 && right == 1 && top >= 0 && bottom < grid.length) positionInGrid = "leftExtremity";
        // bottom left corner
        if (left == -1 && right == 1 && top >= 0 && bottom == grid.length) positionInGrid = "bottomLeftCorner";
        // top extremity
        if (left >= 0 && right < grid[0].length && top == -1 && bottom < grid.length) positionInGrid = "topExtremity";
        // bottom extremity
        if (left >= 0 && right < grid[0].length && top >= 0 && bottom == grid.length) positionInGrid = "bottomExtremity";
        // top right corner
        if (left >= 0 && right == grid[0].length && top == -1 && bottom < grid.length) positionInGrid = "topRightCorner";
        // right extremity
        if (left >= 0 && right == grid[0].length && top >= 0 && bottom < grid.length) positionInGrid = "rightExtremity";
        // bottom right corner
        if (left >= 0 && right == grid[0].length && top >= 0 && bottom == grid.length) positionInGrid = "bottomRightCorner";
        // middle
        if (left >= 0 && right < grid[0].length && top >= 0 && bottom < grid.length) positionInGrid = "middle";

        return positionInGrid;
    }
}