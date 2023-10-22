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

            System.out.println();
            System.out.println("testing findNextChar...");

            GridAndWords testQuery = wholeFile.get(1);
            System.out.println(testQuery.toString());

            char[][] testGrid = testQuery.getGrid();
            String[] testWords = testQuery.getWords();

            HashMap<Character, ArrayList<int[]>> testCharAndPositions = charAndPositionsArrayList.get(1);
            for (char testKey : testCharAndPositions.keySet()) {
                System.out.println(testKey + "=" + Arrays.deepToString(testCharAndPositions.get(testKey).toArray()));
            }

            String testWord = testWords[0];
            System.out.println(testWord);

            char testFirstChar = testWord.charAt(0);
            System.out.print(testFirstChar + "=");

            ArrayList<int[]> testFirstCharPositions = testCharAndPositions.get(testFirstChar);
            System.out.println(Arrays.deepToString(testFirstCharPositions.toArray()));

            ArrayList<String> testPaths = new ArrayList<>();
            System.out.println("testPaths = " + testPaths);

            for (int[] testPosition : testFirstCharPositions) {
                ArrayList<String> testWordPath = new ArrayList<>();
                testWordPath.add("(" + testPosition[0] + "," + testPosition[1] + ")");
                ArrayList<String> testFindNextCharArrayList = findNextChar(testGrid, testWord, testPosition,
                        0, testWordPath);
                System.out.println("testWordPath = " + testWordPath);
                if (testWordPath.size() > 1) testPaths.addAll(testWordPath);
            }

//            System.out.println();
//            System.out.println("testPaths = " + testPaths);
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

    public static ArrayList<String> findNextChar(char[][] grid, String word, int[] currentPosition, int currentIndex, ArrayList<String> path) {
        // check if the current char is the last char of the word
        System.out.println();
        System.out.println("currentIndex = " + currentIndex + ", currentPosition = [" + currentPosition[0] + "," +
                currentPosition[1] + "], currentChar = " + word.charAt(currentIndex));
        System.out.println("currentIndex == word.length() - 1? " + (currentIndex == word.length() - 1));

        if (currentIndex == word.length() - 1) {
            return path;
        }

        // adjacent column and rows
        int left = currentPosition[1] - 1;
        int right = currentPosition[1] + 1;
        int top = currentPosition[0] - 1;
        int bottom = currentPosition[0] + 1;
        System.out.println("left = " + left + ", right = " + right + ", top = " + top + ", bottom = " + bottom);

        // find location of current char in the grid
        String locationInGrid = findLocationInGrid(grid, left, right, top, bottom);
        System.out.println("locationInGrid = " + locationInGrid);

        // initialize chars
        char leftChar, topLeftChar, topChar, topRightChar, rightChar, bottomRightChar, bottomChar, bottomLeftChar;

        // current char
        char currentChar = word.charAt(currentIndex);

        // next char in the word
        int nextIndex = currentIndex + 1;
        char nextChar = word.charAt(nextIndex);
        System.out.println("nextIndex = " + nextIndex + ", nextChar = " + nextChar);

        try {
            if (locationInGrid == null) throw new NullPointerException();

            // next char search
            switch (locationInGrid) {
                case "topLeftCorner" -> {
                    // adjacents chars
                    rightChar = grid[currentPosition[0]][right];
                    bottomRightChar = grid[bottom][right];
                    bottomChar = grid[bottom][currentPosition[1]];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomRightChar == nextChar) {
                        path.add("(" + bottom + "," + right + ")");
                        int[] nextPosition = new int[]{bottom, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    };
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    };
                }

                case "leftExtremity" -> {
                    // adjacents chars
                    topChar = grid[top][currentPosition[1]];
                    topRightChar = grid[top][right];
                    rightChar = grid[currentPosition[0]][right];
                    bottomRightChar = grid[bottom][right];
                    bottomChar = grid[bottom][currentPosition[1]];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topRightChar == nextChar) {
                        path.add("(" + top + "," + right + ")");
                        int[] nextPosition = new int[]{top, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomRightChar == nextChar) {
                        path.add("(" + bottom + "," + right + ")");
                        int[] nextPosition = new int[]{bottom, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "bottomLeftCorner" -> {
                    // adjacents chars
                    topChar = grid[top][currentPosition[1]];
                    topRightChar = grid[top][right];
                    rightChar = grid[currentPosition[0]][right];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topRightChar == nextChar) {
                        path.add("(" + top + "," + right + ")");
                        int[] nextPosition = new int[]{top, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "topExtremity" -> {
                    // adjacents chars
                    rightChar = grid[currentPosition[0]][right];
                    bottomRightChar = grid[bottom][right];
                    bottomChar = grid[bottom][currentPosition[1]];
                    bottomLeftChar = grid[bottom][left];
                    leftChar = grid[currentPosition[0]][left];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomRightChar == nextChar) {
                        path.add("(" + bottom + "," + right + ")");
                        int[] nextPosition = new int[]{bottom, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomLeftChar == nextChar) {
                        path.add("(" + bottom + "," + left + ")");
                        int[] nextPosition = new int[]{bottom, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "bottomExtremity" -> {
                    // adjacents chars
                    leftChar = grid[currentPosition[0]][left];
                    topLeftChar = grid[top][left];
                    topChar = grid[top][currentPosition[1]];
                    topRightChar = grid[top][right];
                    rightChar = grid[currentPosition[0]][right];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topLeftChar == nextChar) {
                        path.add("(" + top + "," + left + ")");
                        int[] nextPosition = new int[]{top, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topRightChar == nextChar) {
                        path.add("(" + top + "," + right + ")");
                        int[] nextPosition = new int[]{top, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "topRightCorner" -> {
                    // adjacents chars
                    bottomChar = grid[bottom][currentPosition[1]];
                    bottomLeftChar = grid[bottom][left];
                    leftChar = grid[currentPosition[0]][left];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomLeftChar == nextChar) {
                        path.add("(" + bottom + "," + left + ")");
                        int[] nextPosition = new int[]{bottom, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "rightExtremity" -> {
                    // adjacents chars
                    bottomChar = grid[bottom][currentPosition[1]];
                    bottomLeftChar = grid[bottom][left];
                    leftChar = grid[currentPosition[0]][left];
                    topLeftChar = grid[top][left];
                    topChar = grid[top][currentPosition[1]];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomLeftChar == nextChar) {
                        path.add("(" + bottom + "," + left + ")");
                        int[] nextPosition = new int[]{bottom, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topLeftChar == nextChar) {
                        path.add("(" + top + "," + left + ")");
                        int[] nextPosition = new int[]{top, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "bottomRightCorner" -> {
                    // adjacents chars
                    leftChar = grid[currentPosition[0]][left];
                    topLeftChar = grid[top][left];
                    topChar = grid[top][currentPosition[1]];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topLeftChar == nextChar) {
                        path.add("(" + top + "," + left + ")");
                        int[] nextPosition = new int[]{top, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }

                case "middle" -> {
                    // adjacents chars
                    leftChar = grid[currentPosition[0]][left];
                    topLeftChar = grid[top][left];
                    topChar = grid[top][currentPosition[1]];
                    topRightChar = grid[top][right];
                    rightChar = grid[currentPosition[0]][right];
                    bottomRightChar = grid[bottom][right];
                    bottomChar = grid[bottom][currentPosition[1]];
                    bottomLeftChar = grid[bottom][left];

                    // search
                    if (currentChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{currentPosition[0], currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (leftChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + left + ")");
                        int[] nextPosition = new int[]{currentPosition[0], left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topLeftChar == nextChar) {
                        path.add("(" + top + "," + left + ")");
                        int[] nextPosition = new int[]{top, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topChar == nextChar) {
                        path.add("(" + top + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{top, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (topRightChar == nextChar) {
                        path.add("(" + top + "," + right + ")");
                        int[] nextPosition = new int[]{top, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (rightChar == nextChar) {
                        path.add("(" + currentPosition[0] + "," + right + ")");
                        int[] nextPosition = new int[]{currentPosition[0], right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomRightChar == nextChar) {
                        path.add("(" + bottom + "," + right + ")");
                        int[] nextPosition = new int[]{bottom, right};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomChar == nextChar) {
                        path.add("(" + bottom + "," + currentPosition[1] + ")");
                        int[] nextPosition = new int[]{bottom, currentPosition[1]};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                    if (bottomLeftChar == nextChar) {
                        path.add("(" + top + "," + left + ")");
                        int[] nextPosition = new int[]{top, left};
                        findNextChar(grid, word, nextPosition, nextIndex, path);
                    }
                }
            }

            return path;
        } catch (NullPointerException e) {
            System.out.println("Unexpected value: position in grid is " + locationInGrid);
        }

        return null;
    }

    public ArrayList<String> findWord(char[][] grid, HashMap<Character, ArrayList<int[]>> charAndPositions, String word) {
        // list of all the paths found for one word
        ArrayList<String> paths = new ArrayList<>();

        // find list of positions of 1st char in the hashmap
        char firstChar = word.charAt(0);
        ArrayList<int[]> firstCharPositions = charAndPositions.get(firstChar);

        // begin the search at each position of the 1st char
        for (int[] position : firstCharPositions) {
            // list of positions of one word
            ArrayList<String> path = new ArrayList<>();
            path.add("(" + position[0] + "," + position[1] + ")");
            ArrayList<String> charArrayList = findNextChar(grid, word, position, 0, path);
            // TODO : findNextChar
        }

        return paths;
    }

    public void findWords(ArrayList<GridAndWords> wholeFile, HashMap<Character, ArrayList<int[]>> charAndPositions) {
        // query counter
        int  queryNumber = 1;

        // process each query
        for (GridAndWords query : wholeFile) {
            // list of all the results found for one query
            ArrayList<String> results = new ArrayList<>();

            // process each word
            for (String word : query.getWords()) {
                // list of all the paths found for one word
                ArrayList<String> paths = new ArrayList<>();
                paths = findWord(query.getGrid(), charAndPositions, word);

                if (paths == null) continue;

                // TODO : findWord
            }

            // print the results for one query
            System.out.println("Query " + queryNumber + ":");
            for (String result : results) {
                System.out.println(result);
            }

            // increase the query counter
            queryNumber++;
        }
    }
}