package FrontEnd.PascalTDWidgets;

import FrontEnd.LastToken;
import FrontEnd.Lexer;
import FrontEnd.Source;
import FrontEnd.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static FrontEnd.Source.EOF;

public class PascalLexer extends Lexer {

    List<Token> list = new ArrayList<>();

    public PascalLexer(Source source) {
        super(source);
    }

    @Override
    public Token extractToken() throws IOException {
        Token token;
        char currentChar = getChar();
        if (currentChar == EOF) {
            token = new LastToken(super.source);
        }
        else{
            token = new Token(super.source);
        }
        list.add(token);
        return token;
    }
}
