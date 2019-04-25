package FrontEnd.PascalWidgets.PascalTokenType;

import FrontEnd.TokenType.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum PascalReservedWord implements TokenType {

    AND,
    ARRAY,
    BEGIN,
    CASE,
    CONST,
    DIV,
    DO,
    DOWNTO,
    ELSE,
    END,
    FILE,
    FOR,
    FUNCTION,
    GOTO,
    IF,
    IN,
    LABEL,
    MOD,
    NIL,
    NOT,
    OF,
    OR,
    PACKED,
    PROCEDURE,
    PROGRAM,
    RECORD,
    REPEAT,
    SET,
    THEN,
    TO,
    TYPE,
    UNTIL,
    VAR,
    WHILE,
    WITH;

    private String content;

    PascalReservedWord()
    {
        this.content= this.toString().toLowerCase();
    }

    PascalReservedWord(String content)
    {
        this.content=content;
    }

    public String getContent()
    {
        return content;
    }

    /**
     * Java programming language enum types are much more powerful than their counterparts in other languages.
     * The enum declaration defines a class (called an enum type).
     * The enum class body can include methods and other fields.
     * The compiler automatically adds some special methods when it creates an enum.
     * For example, they have a static values method that returns an array containing all of the
     * values of the enum in the order they are declared.
     * This method is commonly used in combination with the for-each construct to iterate over the values of an enum type.
     */
    public static Map<String,PascalReservedWord> RWMap = new HashMap<>();
    static {
        for (PascalReservedWord prw : PascalReservedWord.values()) {
            RWMap.put(prw.getContent().toLowerCase(),prw);
        }
    }

}
