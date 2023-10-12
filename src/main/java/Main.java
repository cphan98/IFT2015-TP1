import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ArrayList<Object> wholeFile = readFile(args[0]);
    }

    public static ArrayList<Object> readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int gridHeight;
            int gridWidth;
            ArrayList<Object> wholeFile = new ArrayList<>();

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
                wholeFile.add(query.createQuery(grid, wordsLine));
            }
        } catch (IOException e) {
            System.out.println("Unable to read fille " + fileName);
        }
        return null;
    }
}