import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<GridAndWords> wholeFile = readFile(args[0]);
        for (GridAndWords gridAndWords : wholeFile) {
            System.out.println("\u001b[34m"+gridAndWords.toString()+"\u001b[0m"); //color text in blue
        }
    }

    /**
     * The function readFile returns an array list containing the grid and words of each query given in a text file.
     *
     * @param fileName  the name of the text file to be read
     * @return          an array list containing the grid and words of each query
     */
    public static ArrayList<GridAndWords> readFile(String fileName) { // reads the file provided with a buffer line by line
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

                for (int i = 0; i < gridHeight; i++) {
                    line = br.readLine();

                    for (int j = 0; j < gridWidth; j++) {
                        grid[i][j] = line.split(" ")[j].charAt(0);
                    }
                }

                String[] wordsLine = br.readLine().split(" ");
                GridAndWords gridAndWords = new GridAndWords(grid, wordsLine);
                wholeFile.add(gridAndWords);
            }
            return wholeFile;
        } catch(IOException e) {
            System.out.println("Unable to read file " + fileName);
        }
        return null;
    }
}