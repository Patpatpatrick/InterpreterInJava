package FrontEnd.PascalTDWidgets;

import FrontEnd.TokenTag;

import java.util.HashSet;
import java.util.Hashtable;

public enum PascalTokenDict implements TokenTag {
    // 关键字
    AND, ARRAY, BEGIN, CASE, CONST, DIV, DO, DOWNTO, ELSE, END,
    FILE, FOR, FUNCTION, GOTO, IF, IN, LABEL, MOD, NIL, NOT,
    OF, OR, PACKED, PROCEDURE, PROGRAM, RECORD, REPEAT, SET,
    THEN, TO, TYPE, UNTIL, VAR, WHILE, WITH,

    // 特殊符号
    PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"), COLON_EQUALS(":="),
    DOT("."), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"),
    EQUALS("="), NOT_EQUALS("<>"), LESS_THAN("<"), LESS_EQUALS("<="),
    GREATER_EQUALS(">="), GREATER_THAN(">"), LEFT_PAREN("("), RIGHT_PAREN(")"),
    LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    UP_ARROW("^"), DOT_DOT(".."),

    IDENTIFIER, INTEGER, REAL, STRING,
    ERROR, END_OF_FILE;

    private static final int FIRST_RESERVED_INDEX = AND.ordinal();
    private static final int LAST_RESERVED_INDEX  = WITH.ordinal();

    private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
    private static final int LAST_SPECIAL_INDEX  = DOT_DOT.ordinal();

    private String text;  // 字面文本

    PascalTokenDict()
    {
        this.text = this.toString().toLowerCase();
    }

    PascalTokenDict(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    //所有关键字(都是小写）集合
    public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
    static {
        PascalTokenDict values[] = PascalTokenDict.values();
        for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) {
            RESERVED_WORDS.add(values[i].getText().toLowerCase());
        }
    }

    //特殊符号哈希表，以符号字面文本作为键表示Token类型
    public static Hashtable<String, PascalTokenDict> SPECIAL_SYMBOLS =
            new Hashtable<String, PascalTokenDict>();
    static {
        PascalTokenDict values[] = PascalTokenDict.values();
        for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
            SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
        }
    }

}
