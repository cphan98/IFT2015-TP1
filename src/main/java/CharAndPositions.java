import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CharAndPositions {
    // attributes
    private char letter;
    private Integer[][] positions;

    // constructor
    public CharAndPositions(char letter, Integer[][] positions) {
        this.letter = letter;
        this.positions = positions;
    }

    // getters

    public char getLetter() {
        return letter;
    }
    public Integer[][] getPositions() {
        return positions;
    }

    // setters

    public void setLetter(char letter) {
        this.letter = letter;
    }
    public void setPositions(Integer[][] positions) {
        this.positions = positions;
    }

    // pretty print
    @Override
    public String toString() {
        return "{" + letter + "=" + Arrays.toString(positions) + "}";
    }
}
