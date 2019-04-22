package FrontEnd;

import Message.MessageBroadCaster;
import Message.MessageType;

import java.io.IOException;

import static FrontEnd.Source.EOF;

/**
 *
 * It's a Lexer.
 *
 * An instance of class Lexer contains a member Source represents the source code,
 * The scanner will organize the characters grabbed from source base on the language to form a token ready for use.
 * So we would expect the subclass to call readOnCurrOffset on source.
 *
 *
 */

public abstract class Lexer extends MessageBroadCaster {

    protected Source source;

    // Lexer owns the current Token member
    private Token currentToken;

    public Lexer(Source source) {
        this.source=source;
    }

    // currentToken should be assigned in this method override in the subclass.
    // this method overriden in subtype will primarily call token constructor
    public abstract Token extractToken() throws IOException;

    public char getChar() throws IOException {
        char ret = source.readOnCurrOffset();
//        if(ret != EOF);
//            source.cursorMoveForward();
        return ret;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token nextToken() throws IOException {
        currentToken = extractToken();
        return currentToken;
    }
}
