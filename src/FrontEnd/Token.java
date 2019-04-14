package FrontEnd;

/**
 *
 * An instance of this class represents a token in the source code file of some programming language.
 *  *Maybe* the most important and the only method needed is CONSTRUCTOR.
 */
public class Token {

    private String value;

    private TokenTag tag;

    //represents which line it is
    private int numOfLine;
    private int offSet;
    private int tokenLength;

    public Token(String value,TokenTag tag,int numOfLine,int offSet,int tokenLength) {
        this.value=value;
        this.tag=tag;
        this.numOfLine=numOfLine;
        this.offSet=offSet;
        this.tokenLength=tokenLength;
    }
}

