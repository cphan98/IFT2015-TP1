import java.util.Arrays;

public class GridAndWords {
    // attributes
    private char[][] grid;
    private String[] words;

    // constructor
    public GridAndWords(char[][] grid, String[] words) {
        this.grid = grid;
        this.words = words;
    }

    // getters
    public char[][] getGrid() {
        return grid;
    }
    public String[] getWords() {
        return words;
    }

    // setters
    public void setGrid(char[][] grid) {
        this.grid = grid;
    }
    public void setWords(String[] words) {
        this.words = words;
    }

    // pretty print
    @Override
    public String toString() {
        return "[" + Arrays.deepToString(grid) + "," + Arrays.toString(words) + "]";
    }
}
