package FrontEnd.PascalWidgets.PascalToken;

import FrontEnd.Token;
import FrontEnd.TokenType.TokenType;

public abstract class PasConstToken extends Token {
    public PasConstToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
