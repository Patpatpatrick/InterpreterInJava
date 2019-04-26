package FrontEnd.PascalWidgets.PascalTokenType;

import FrontEnd.TokenType.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum PascalSpecialChar implements TokenType {

    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    COLON_EQUALS(":="),
    DOT("."),
    COMMA(","),
    SEMICOLON(";"),
    COLON(":"),
    QUOTE("'"),
    DOUBLE_QUOTE("\""),
    EQUALS("="),
    NOT_EQUALS("<>"),
    LESS_THAN("<"),
    LESS_EQUALS("<="),
    GREATER_EQUALS(">="),
    GREATER_THAN(">"),
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    UP_ARROW("^"),
    DOT_DOT("..");

    private String content;

    PascalSpecialChar()
    {
        this.content= this.toString().toLowerCase();
    }

    PascalSpecialChar(String content)
    {
        this.content=content;
    }

    public String getContent()
    {
        return content;
    }

    public static Map<String,PascalSpecialChar> SCMap = new HashMap<>();
    static {
        for (PascalSpecialChar psc : PascalSpecialChar.values()) {
            SCMap.put(psc.getContent(), psc);
        }
    }
}
