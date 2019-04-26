package FrontEnd;

import FrontEnd.TokenType.TokenType;

public class EoLToken extends Token {
    public EoLToken(String content,TokenType type,int numOfLine,int offSet) {
        super(content,type,numOfLine,offSet);
    }
}
