package FrontEnd.PascalWidgets.PascalToken;

import FrontEnd.Token;
import FrontEnd.TokenType.TokenType;

public class PasVarToken extends Token {
    public PasVarToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
