package FrontEnd.PascalWidgets.PascalToken;

import FrontEnd.TokenType.TokenType;

public class PasStringToken extends PasConstToken {
    public PasStringToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
