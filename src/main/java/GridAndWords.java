import java.util.ArrayList;
import java.util.Objects;

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

    // create an array list of a query's grid and words
    public ArrayList<Object> createQuery(char[][] grid, String[] words) {
        ArrayList<Object> query = new ArrayList<>();

        query.add(grid);
        query.add(words);

        return query;
    }
}
