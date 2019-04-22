package FrontEnd;

import java.io.IOException;

/**
 *
 * An instance of this class represents a token in the source code file of some programming language.
 *  *Maybe* the most important and the only method needed is CONSTRUCTOR.
 */
public class Token {

    private String content;
    private TokenTag tag;
    private Object value;

    //represents which line it is
    private int numOfLine;
    private int offSet;
    private int tokenLength;

    public Token(String content,TokenTag tag,Object value,int numOfLine,int offSet,int tokenLength) {
        this.content=content;
        this.tag=tag;
        this.value=value;
        this.numOfLine=numOfLine;
        this.offSet=offSet;
        this.tokenLength=tokenLength;
    }

    public Token(Source source) throws IOException {
        numOfLine = source.getNumOfLineRead() - 1;
        offSet = source.getCurrentOffset();
        extract(source);
    }

    protected void extract(Source source) throws IOException {
        content= Character.toString(source.readOnCurrOffset());
        source.cursorMoveForward();
    }

    public String getContent() {
        return content;
    }

    public TokenTag getTag() {
        return tag;
    }

    public Object getValue() {
        return value;
    }

    public int getNumOfLine() {
        return numOfLine;
    }

    public int getOffSet() {
        return offSet;
    }

    public int getTokenLength() {
        return tokenLength;
    }
}

