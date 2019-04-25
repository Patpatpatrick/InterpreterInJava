package FrontEnd;

import FrontEnd.TokenType.EoFTokenType;
import FrontEnd.TokenType.TokenType;

import java.io.IOException;

// This class is a kind of token, but it is language-independent

public class EoFToken extends Token {

    public EoFToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
