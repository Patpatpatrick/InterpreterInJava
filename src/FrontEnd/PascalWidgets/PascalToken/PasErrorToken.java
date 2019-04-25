package FrontEnd.PascalWidgets.PascalToken;

import FrontEnd.Token;
import FrontEnd.TokenType.TokenType;

public class PasErrorToken extends Token {
    public PasErrorToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
