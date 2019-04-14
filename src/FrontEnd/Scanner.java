package FrontEnd;

/**
 *
 * It's a Lexer.
 *
 * An instance of class Scanner contains a member Source represents the source code,
 * The scanner will organize the characters grabbed from source base on the language to form a token ready for use.
 * So we would expect the subclass to call readOnCurrOffset on source.
 *
 *
 */

public abstract class Scanner {

    private Source source;

    private Token currentToken;



    // currentToken should be assigned in this method override in the subclass.
    public abstract Token extractToken();




}
