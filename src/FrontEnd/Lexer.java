package FrontEnd;

import Message.MessageBroadCaster;

import java.io.IOException;


/**
 * It's a Lexer.
 *
 * An instance of class Lexer contains a member Source represents the source code,
 * The scanner will organize the characters grabbed from source base on the language to form a token ready for use.
 * So we would expect the subclass to call readOnCurrOffset on source.
 */

public abstract class Lexer extends MessageBroadCaster {

    protected Source source;

    // Lexer owns the current Token member
    private Token currentToken;

    public Lexer(Source source) {
        this.source=source;
    }

    // currentToken should be assigned in this method override in the subclass.
    // this method overridden in subtype will primarily call token constructor
    public abstract Token extractToken() throws IOException;

    protected abstract boolean isRW(String content) throws IOException;

    protected abstract boolean isAValidIdentifier(String content);

    protected boolean isAValidConst(String content){

        return true;
    }

    protected abstract boolean isSC(String content);

    public char readOnCurrOffset() throws IOException {
        return source.readOnCurrOffset();
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token nextToken() throws IOException {
        currentToken = extractToken();
        return currentToken;
    }

    protected String readMeaningfulChars() throws IOException, ErrorTokenTypeException {
        char c = skipBlankReadAChar();
        if(c == Source.EOL){
            source.cursorMoveForward();
            return "";
        }else if(c == Source.EOF){
            return "";
        }
        else {
            return readNormalContentByThisChar(c);
        }
    }

    protected abstract String readNormalContentByThisChar(char c) throws IOException, ErrorTokenTypeException;

    protected abstract String readLegalWordOrNumber(char c, boolean continueCondition, String InitialFlag) throws IOException;

    protected abstract String readLegalSC(char c) throws Exception, ErrorTokenTypeException;

    protected char skipBlankReadAChar() throws IOException {
        char c = source.readOnCurrOffset();
        while(c== ' '){
            source.cursorMoveForward();
            c = source.readOnCurrOffset();
            if(c == Source.EOL || c == Source.EOF)
                break;
        }
        return c;
    }

}
