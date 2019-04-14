import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * An instance of this class represents a token in the source code file of some programming language.
 *
 */
public class Token {
    private String value;
    private TokenTag tag;

    private int numOfLine;
    private int offSet;



    public static void main(String[] args) {
        Token token = new Token();
    }

}

