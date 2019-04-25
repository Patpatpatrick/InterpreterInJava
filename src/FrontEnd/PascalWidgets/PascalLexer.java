package FrontEnd.PascalWidgets;

import FrontEnd.*;
import FrontEnd.PascalWidgets.PascalToken.*;
import FrontEnd.PascalWidgets.PascalTokenType.*;
import FrontEnd.TokenType.EoFTokenType;
import FrontEnd.TokenType.TokenType;

import java.io.IOException;

import static FrontEnd.Source.EOF;

public class PascalLexer extends Lexer {

    public PascalLexer(Source source) {
        super(source);
    }

    @Override
    public Token extractToken() throws IOException {
        String content = "";
        char currentChar = readOnCurrOffset();
        int startPos = source.getCurrentOffset();
        int nlr = source.getNumOfLineRead();
        TokenType type;

        if (currentChar == EOF)
            return new EoFToken(Character.toString(EOF),new EoFTokenType(),nlr,startPos);
        try{
            content = readMeaningfulChars();
        }catch (ErrorTokenTypeException e){
            type = new PascalError();
            return new PasErrorToken(content,type,nlr,startPos);
        }
        if(isRW(content)){
            type = PascalReservedWord.RWMap.get(content);
            return new PasRWToken(content,type,nlr,startPos);
        }else if(isSC(content)){
            type = PascalSpecialChar.SCMap.get(content);
            return new PasSCToken(content,type,nlr,startPos);
        }else if(isAValidIdentifier(content)){
            type = new PascalVar();
            return new PasVarToken(content,type,nlr,startPos);
        }else if(isAValidConst(content)){
            type = PascalConst.FALSE;
            return new PasConstToken(content,type,nlr,startPos);
        }else {
            // TODO might be problematic
            type = PascalSpecialChar.SCMap.get(content);
            return new PasSCToken(content,type,nlr,startPos);
        }
    }

    @Override
    protected boolean isRW(String content) { return PascalReservedWord.RWMap.containsKey(content); }

    @Override
    protected boolean isAValidIdentifier(String content) {
        if(isRW(content)) return false;

        return false;
    }

    @Override
    protected boolean isSC(String content) {
        return PascalSpecialChar.SCMap.containsKey(content);
    }

    // This function **should** definitely be language-dependent!
    @Override
    protected String readNormalContentByThisChar(char c) throws IOException, ErrorTokenTypeException {

        if(Character.isLetter(c) || c == '_'){
            // if c is a letter or a underscore,
            // we should read as much as we can until we meet a char that is not a-z A-Z _;
            boolean continueCondition = Character.isLetter(c) || c == '_';
            return readLegalWordOrNumber(c,continueCondition,"W");
        }else if(Character.isDigit(c) || c == '.'){
            // if c is a digit / dicimal, we should read digit&dicimal / digit.
            boolean continueCondition = Character.isDigit(c) || c == '.';
            return readLegalWordOrNumber(c,continueCondition,"N");
        }else{
            // in such case we need to try to read a SC meaning ful in Pascal
            return readLegalSC(c);
        }

    }

    @Override
    protected String readLegalWordOrNumber(char c , boolean continueCondition, String initialFlag) throws IOException {
        StringBuilder sb = new StringBuilder();
        int dotCount = 0;
        while(continueCondition){
            sb.append(c);
            c = source.readNextChar();
            if(c == Source.EOL || c == Source.EOF)
                break;
            if(initialFlag.equals("W")){
                continueCondition = Character.isLetter(c) || c == '_' || Character.isDigit(c);
            }else if(initialFlag.equals("N")){
                continueCondition = Character.isDigit(c) || c == '.';
                if(c == '.'){
                    dotCount++;
                    if(dotCount > 1)
                        break;
                }
            }else{
                throw new RuntimeException();
            }
        }
        return sb.toString();
    }


    @Override
    protected String readLegalSC(char c) throws IOException, ErrorTokenTypeException {
        switch (c) {

            // single char SC is easy to deal with
            case '+':  case '-':  case '*':  case '/':  case ',':
            case ';':  case '\'': case '=':  case '(':  case ')':
            case '[':  case ']':  case '{':  case '}':  case '^': {
                return Character.toString(c);
            }


            case ':': {
                if(source.peekChar() == '='){
                    return ":=";
                }
                else
                    return ":";
            }

            case '<': {
                if(source.peekChar() == '=')
                    return "<=";
                if (source.peekChar() == '>')
                    return "<>";
                return "<";
            }

            case '>': {
                if(source.peekChar() == '=')
                    return ">=";
                return ">";
            }

            // . 或 ..
            case '.': {
                if(source.peekChar() == '.')
                    return "..";
                return ".";
            }

            default: {
                source.cursorMoveForward();
                throw new ErrorTokenTypeException();
            }
        }
    }

}
