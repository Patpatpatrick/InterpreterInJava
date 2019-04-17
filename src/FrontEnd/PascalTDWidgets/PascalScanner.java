package FrontEnd.PascalTDWidgets;

import FrontEnd.LastToken;
import FrontEnd.Scanner;
import FrontEnd.Token;

import java.io.IOException;

import static FrontEnd.Source.EOF;

public class PascalScanner extends Scanner {
    @Override
    public Token extractToken() throws IOException {
        Token token;
        char currentChar = getChar();
        if (currentChar == EOF) {
            token = new LastToken();
        }
        else{
            token = new Token();
        }
        return token;
    }
}
