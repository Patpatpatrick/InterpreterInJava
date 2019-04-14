/**
 *
 * It's a Lexer.
 *
 * An instance of class Scanner contains a member Source represents the code,
 * an index that points to the next char to be read as token
 * an member of type Token to stands for the current scanned token.
 *
 */

public abstract class Scanner {

    private Source source;

    private Token currentToken;

    private int currentPos = 0;

    private int posOfNextChar = 0;


    public Token extractToken(){
        currentToken = source.extractToken();
        return currentToken;
    }



}
