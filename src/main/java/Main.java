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

            ArrayList<HashMap<Character, ArrayList<int[]>>> charAndPositions = charAndPositionsArrayList(wholeFile);

            for (HashMap<Character, ArrayList<int[]>> query : charAndPositions) {
                for (char key : query.keySet()) {
                    System.out.println(key + "=" + Arrays.deepToString(query.get(key).toArray()));
                }
                System.out.println();
            }

            wholeFile = updateWholeFile(wholeFile, charAndPositions);

            for (GridAndWords query : wholeFile) {
                System.out.println(query.toString());
            }

            System.out.println();
            System.out.println("process queries...");
            System.out.println();

            for (int query = 0; query < wholeFile.size(); query++) {
                System.out.println("Query " + (query + 1) + ":");
                HashMap<Character, ArrayList<int[]>> queryCharAndPositions = charAndPositions.get(query);
                ArrayList<String> paths = new ArrayList<>();
                findWords(wholeFile.get(query).getWords(), wholeFile.get(query).getGrid(), queryCharAndPositions, paths);
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
        // possible locations in the grid
        // top left corner
        if (left == -1 && right == 1 && top == -1 && bottom == 1) return "topLeftCorner";
        // left extremity
        if (left == -1 && right == 1 && top >= 0 && bottom < grid.length) return "leftExtremity";
        // bottom left corner
        if (left == -1 && right == 1 && top >= 0 && bottom == grid.length) return "bottomLeftCorner";
        // top extremity
        if (left >= 0 && right < grid[0].length && top == -1 && bottom < grid.length) return "topExtremity";
        // bottom extremity
        if (left >= 0 && right < grid[0].length && top >= 0 && bottom == grid.length) return "bottomExtremity";
        // top right corner
        if (left >= 0 && right == grid[0].length && top == -1 && bottom < grid.length) return "topRightCorner";
        // right extremity
        if (left >= 0 && right == grid[0].length && top >= 0 && bottom < grid.length) return "rightExtremity";
        // bottom right corner
        if (left >= 0 && right == grid[0].length && top >= 0 && bottom == grid.length) return "bottomRightCorner";
        // middle
        if (left >= 0 && right < grid[0].length && top >= 0 && bottom < grid.length) return "middle";

        return null;
    }

    public static void findWord(String word, int currentWordIndex, int[] currentPosition, ArrayList<String> currentPath, char[][] grid, ArrayList<String> wordPaths) {
        // check if the end of the word is reached
        if (currentWordIndex == -1) {
            String path = word + " ";
            path += String.join("->", currentPath);
            wordPaths.add(path);
            return;
        }

        // append the current position to the current path
        currentPath.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");

        // columns and rows adjacent to the current position
        int left = currentPosition[1] - 1;
        int right = currentPosition[1] + 1;
        int top = currentPosition[0] - 1;
        int bottom = currentPosition[0] + 1;

        // find the location of the current position in the grid
        String locationInGrid = findLocationInGrid(grid, left, right, top, bottom);
        if (locationInGrid == null) return;

        // next character in the word
        int nextWordIndex;
        char nextWordChar = 0;
        if (currentWordIndex == word.length() - 1) {
            nextWordIndex = -1;
            findWord(word, -1, currentPosition, currentPath, grid, wordPaths);
        } else {
            nextWordIndex = currentWordIndex + 1;
            nextWordChar = word.charAt(nextWordIndex);
        }

        // positions adjacent to the current position according to its location in the grid
        int[] leftPos = new int[]{currentPosition[0], left};
        int[] topLeftPos = new int[]{top, left};
        int[] topPos = new int[]{top, currentPosition[1]};
        int[] topRightPos = new int[]{top, right};
        int[] rightPos = new int[]{currentPosition[0], right};
        int[] bottomRightPos = new int[]{bottom, right};
        int[] bottomPos = new int[]{bottom, currentPosition[1]};
        int[] bottomLeftPos = new int[]{bottom, left};

        // compare adjacent chars to the next word char
        // if an adjacent char corresponds to the next word char, continue the search recursively with the position
        //      of the adjacent char
        // self char
        if (word.charAt(currentWordIndex) == nextWordChar)
            findWord(word, nextWordIndex, currentPosition, currentPath, grid, wordPaths);
        // compare adjacent chars according the location in the grid
        switch (locationInGrid) {
            case "topLeftCorner" -> {
                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);

                // bottom right char
                if (grid[bottom][right] == nextWordChar)
                    findWord(word, nextWordIndex, bottomRightPos, currentPath, grid, wordPaths);

                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);
            }

            case "topExtremity" -> {
                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);

                // bottom right char
                if (grid[bottom][right] == nextWordChar)
                    findWord(word, nextWordIndex, bottomRightPos, currentPath, grid, wordPaths);

                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);

                // bottom left char
                if (grid[bottom][left] == nextWordChar)
                    findWord(word, nextWordIndex,bottomLeftPos , currentPath, grid, wordPaths);

                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);
            }

            case "topRightCorner" -> {
                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);

                // bottom left char
                if (grid[bottom][left] == nextWordChar)
                    findWord(word, nextWordIndex, bottomLeftPos, currentPath, grid, wordPaths);

                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);
            }

            case "rightExtremity" -> {
                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);

                // bottom left char
                if (grid[bottom][left] == nextWordChar)
                    findWord(word, nextWordIndex, bottomLeftPos, currentPath, grid, wordPaths);

                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);

                // top left char
                if (grid[top][left] == nextWordChar)
                    findWord(word, nextWordIndex, topLeftPos, currentPath, grid, wordPaths);

                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);
            }

            case "bottomRightCorner" -> {
                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);

                // top left char
                if (grid[top][left] == nextWordChar)
                    findWord(word, nextWordIndex, topLeftPos, currentPath, grid, wordPaths);

                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);
            }

            case "bottomExtremity" -> {
                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);

                // top left char
                if (grid[top][left] == nextWordChar)
                    findWord(word, nextWordIndex, topLeftPos, currentPath, grid, wordPaths);

                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);

                // top right char
                if (grid[top][right] == nextWordChar)
                    findWord(word, nextWordIndex, topRightPos, currentPath, grid, wordPaths);

                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);
            }

            case "bottomLeftCorner" -> {
                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);

                // top right char
                if (grid[top][right] == nextWordChar)
                    findWord(word, nextWordIndex, topRightPos, currentPath, grid, wordPaths);

                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);
            }

            case "leftExtremity" -> {
                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);

                // top right char
                if (grid[top][right] == nextWordChar)
                    findWord(word, nextWordIndex, topRightPos, currentPath, grid, wordPaths);

                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);

                // bottom right char
                if (grid[bottom][right] == nextWordChar)
                    findWord(word, nextWordIndex, bottomRightPos, currentPath, grid, wordPaths);

                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);
            }

            case "middle" -> {
                // left char
                if (grid[currentPosition[0]][left] == nextWordChar)
                    findWord(word, nextWordIndex, leftPos, currentPath, grid, wordPaths);

                // top left char
                if (grid[top][left] == nextWordChar)
                    findWord(word, nextWordIndex, topLeftPos, currentPath, grid, wordPaths);

                // top char
                if (grid[top][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, topPos, currentPath, grid, wordPaths);

                // top right char
                if (grid[top][right] == nextWordChar)
                    findWord(word, nextWordIndex, topRightPos, currentPath, grid, wordPaths);

                // right char
                if (grid[currentPosition[0]][right] == nextWordChar)
                    findWord(word, nextWordIndex, rightPos, currentPath, grid, wordPaths);

                // bottom right char
                if (grid[bottom][right] == nextWordChar)
                    findWord(word, nextWordIndex, bottomRightPos, currentPath, grid, wordPaths);

                // bottom char
                if (grid[bottom][currentPosition[1]] == nextWordChar)
                    findWord(word, nextWordIndex, bottomPos, currentPath, grid, wordPaths);

                // bottom left char
                if (grid[bottom][left] == nextWordChar)
                    findWord(word, nextWordIndex, bottomLeftPos, currentPath, grid, wordPaths);
            }
        }
    }

    public static void findWords(String[] words, char[][] grid, HashMap<Character, ArrayList<int[]>> charAndPositions, ArrayList<String> paths) {
        for (String word : words) {
            // array list of paths found
            ArrayList<String> wordPaths = new ArrayList<>();

            // take the array list of positons of the first char
            ArrayList<int[]> firstCharPositions = charAndPositions.get(word.charAt(0));

            // search the paths
            for (int[] position : firstCharPositions) {
                // array list of positions
                ArrayList<String> currentPath = new ArrayList<>();

                // search
                findWord(word, 0, position, currentPath, grid, wordPaths);

                if (currentPath.size() != word.length()) continue;
            }

            // append the paths of the word into the list of paths
            paths.addAll(wordPaths);
        }

        // sort the paths lexicographically
        Collections.sort(paths);

        // print the paths
        for (String path : paths) System.out.println(path);
    }
}