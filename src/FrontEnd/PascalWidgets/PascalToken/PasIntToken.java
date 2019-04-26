package FrontEnd.PascalWidgets.PascalToken;

import FrontEnd.TokenType.TokenType;

public class PasIntToken extends PasConstToken {
    public PasIntToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
