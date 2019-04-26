package FrontEnd;

import Message.MessageBroadCaster;

import java.io.IOException;


/**
 * It's a Lexer.
 *
 * An instance of class Lexer contains a member Source represents the source code,
 * The scanner will organize the characters grabbed from source base on the language to form a token ready for use.
 * So we would expect the subclass to call readCharOnCurrOffset on source.
 */

public abstract class Lexer extends MessageBroadCaster {

    protected Source source;

    protected Token currentToken;

    protected String content;

    protected boolean succeedInExtraction;

    public Lexer(Source source) {
        this.source=source;
        content = "";
        succeedInExtraction = false;
        //Succeeding in generating an error token also counts.
    }

    public Token getCurrentToken() { return currentToken; }


    public Token nextToken() throws IOException {
        extractToken();
        // reinitialize the content to be empty;
        content = "";
        succeedInExtraction = false;
        return currentToken;
    }

    public abstract void extractToken() throws IOException;

    protected abstract void tryToExpandAsRWFollowedByBlank(char currentChar,int nlr,int pos) throws IOException;
    protected abstract boolean contentIsRW() throws IOException;
    protected abstract void tryToExtractAValidIdentifier(int nlr,int pos) throws IOException;

    protected abstract void tryToExtractAValidConst(int nlr,int pos) throws Exception, ErrorTokenTypeException;
    protected abstract void tryToExtractValidString(char startChar,int nlr,int pos) throws IOException, ErrorTokenTypeException;
    protected abstract void tryToExtractPositiveNumber(char startChar,int nlr,int pos) throws Exception, ErrorTokenTypeException;
    protected abstract void tryToExtractNegativeNum(int nlr,int pos);

    protected abstract void tryToExtractSpecialCharacters(char currentChar,int nlr,int pos) throws IOException, ErrorTokenTypeException;
    protected abstract boolean contentIsSC() throws IOException;

    protected abstract String unsignedIntegerDigits() throws IOException;


    //this method read something under some rule.
    protected abstract String readLegalRWOrID(char c,boolean continueCondition,WordKind initialAttempt) throws IOException;
    protected abstract String readLegalSC(char c) throws Exception, ErrorTokenTypeException;

    protected char skipBlankReadAChar() throws IOException {
        char c = source.readCharOnCurrOffset();
        while(c == ' '){
            source.cursorMoveForward();
            c = source.readCharOnCurrOffset();
            if(c == Source.EOL || c == Source.EOF)
                break;
        }
        return c;
    }

}
