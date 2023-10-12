import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<GridAndWords> wholeFile = readFile(args[0]);
    }

    /**
     * The readFile function read a text file and returns an array list containing the queries from the file.
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
}