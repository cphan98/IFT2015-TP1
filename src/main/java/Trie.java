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
        public Map<Character, TrieNode> children; // map for children nodes
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

    /**
     * findFirstChar uses breadth-first search to find the first character of a word in a trie. Only the first level is
     * visited.
     *
     * @param firstChar a character
     * @return          a TrieNode; the node that contains firstChar
     */
    public TrieNode findFirstChar(char firstChar) {
        Queue<TrieNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TrieNode currentNode = queue.remove();
            if (currentNode.children.containsKey(firstChar)) {
                return currentNode;
            }
        }
        return null;
    }
}
