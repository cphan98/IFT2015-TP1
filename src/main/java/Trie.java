import java.util.*;

/**
 * Trie is a class for a standard Trie with character nodes.
 *
 * Based on François Major's course notes 3.1 Arbres généraux, slide 17
 */
public class Trie {
    // inner class TrieNode
    public static class TrieNode {
        // attributes
        public Map<int[], TrieNode> children; // map for children nodes
        public boolean isWord; // boolean for marking word nodes; removes the prefix restriction

        // constructor
        public TrieNode() {
            children = new HashMap<>(); // empty HashMap for children
            this.isWord = false; // a node is not storing a word by default
        }

        // pretty print
        @Override
        public String toString() {
            return "Node< " + this.children.size() + " children, is a word? " + this.isWord + " >";
        }
    }

    // attributes
    private final TrieNode root = new TrieNode(); // root of the Trie

    // getters
    public TrieNode getRoot() {
        return root;
    }

    // constructor
    public Trie(String[] words) {
        for (String word : words) this.insert(word);
    }

    /**
     * The insert method inserts a word in the trie.
     *
     * @param word  a word to be inserted in the trie
     */
    public void insert(String word) {
        TrieNode node = root; // take the root

        for (char c : word.toCharArray()) {
            // if there is no node with char c in the children, create it
            if (node.children.get(c) == null) node.children.put(c, new TrieNode());

            node = node.children.get(c); // move node to process the next char
        }
        node.isWord = true; // last node accessed is for the last char of the word
    }

    public Trie(String word, char[][] grid, HashMap<Character, ArrayList<int[]>> charAndPositions) {
        TrieNode node = root;

        // add positions of 1st char to Trie
        char firstChar = word.charAt(0);
        ArrayList<int[]> firstCharPositions = charAndPositions.get(firstChar);
        for (int[] position : firstCharPositions) {
            node.children.put(position, new TrieNode());
        }

        for (Map.Entry<int[], TrieNode> set : node.children.entrySet()) {
            int currentWordIndex = 0;
            char currentWordChar = firstChar;
            int[] currentGridPosition = set.getKey();
            int nextWordIndex = currentWordIndex + 1;
            char nextWordChar = word.charAt(nextWordIndex);

            // adjacent columns and rows
            int left = currentGridPosition[1] - 1;
            int right = currentGridPosition[1] + 1;
            int top = currentGridPosition[0] - 1;
            int bottom = currentGridPosition[0] + 1;

            // find location of currentGirdPositon in grid
            String locationInGrid;
            // possible locations in grid
            // top left corner
            if (left == -1 && right == 1 && top == -1 && bottom == 1) locationInGrid = "topLeftCorner";
            // left extremity
            if (left == -1 && right == 1 && top >= 0 && bottom < grid.length) locationInGrid = "leftExtremity";
            // bottom left corner
            if (left == -1 && right == 1 && top >= 0 && bottom == grid.length) locationInGrid = "bottomLeftCorner";
            // top extremity
            if (left >= 0 && right < grid[0].length && top == -1 && bottom < grid.length) locationInGrid = "topExtremity";
            // bottom extremity
            if (left >= 0 && right < grid[0].length && top >= 0 && bottom == grid.length) locationInGrid = "bottomExtremity";
            // top right corner
            if (left >= 0 && right == grid[0].length && top == -1 && bottom < grid.length) locationInGrid = "topRightCorner";
            // right extremity
            if (left >= 0 && right == grid[0].length && top >= 0 && bottom < grid.length) locationInGrid = "rightExtremity";
            // bottom right corner
            if (left >= 0 && right == grid[0].length && top >= 0 && bottom == grid.length) locationInGrid = "bottomRightCorner";
            // middle
            if (left >= 0 && right < grid[0].length && top >= 0 && bottom < grid.length) locationInGrid = "middle";


        }
    }
}
