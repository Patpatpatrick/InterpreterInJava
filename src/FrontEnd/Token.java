package FrontEnd;

import FrontEnd.TokenType.TokenType;

import java.io.IOException;

/**
 * An instance of this class represents a token in the source code file of some programming language.
 * the most important and the only method needed is CONSTRUCTOR.
 */
public abstract class Token {

    protected String content;
    protected TokenType type;

    public void setValue(Object value) {
        this.value=value;
    }

    //TODO I might need another field called "Value" to represent the value of a token of type number.
    protected Object value;
    protected int numOfLine;
    protected int offSet;

    public Token(String content,TokenType type,int numOfLine,int offSet) {
        this.content=content;
        this.type=type;
        this.numOfLine=numOfLine;
        this.offSet=offSet;
    }

    public String getContent() {
        return content;
    }


    public int getNumOfLine() {
        return numOfLine;
    }

    public int getOffSet() {
        return offSet;
    }

}

