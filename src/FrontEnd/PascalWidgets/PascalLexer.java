package FrontEnd.PascalWidgets;

import FrontEnd.*;
import FrontEnd.PascalWidgets.PascalToken.*;
import FrontEnd.PascalWidgets.PascalTokenType.*;
import FrontEnd.PascalWidgets.PascalTokenType.PascalConstType.*;
import FrontEnd.TokenType.EoFTokenType;
import FrontEnd.TokenType.EoLTokenType;
import FrontEnd.TokenType.TokenType;

import java.io.IOException;

import static FrontEnd.Source.EOF;
import static FrontEnd.Source.EOL;
import static java.lang.Float.MAX_EXPONENT;

public class PascalLexer extends Lexer {

    public PascalLexer(Source source) {
        super(source);
    }

    @Override
    public void extractToken() throws IOException {
        char currentChar = skipBlankReadAChar();
        int startPos = source.getCurrentOffset();
        int nlr = source.getNumOfLineRead();

        if (currentChar == EOF){
            currentToken = new EoFToken(Character.toString(EOF),new EoFTokenType(),nlr,startPos);
            succeedInExtraction = true;
            return;
        }
        if (currentChar == EOL) {
            source.cursorMoveForward();
            currentToken = new EoLToken(Character.toString(EOL),new EoLTokenType(),nlr,startPos);
            succeedInExtraction = true;
            return;
        }
        try {
            tryToExpandAsRWFollowedByBlank(currentChar,nlr,startPos);
            if(!succeedInExtraction)
                tryToExtractAValidIdentifier(nlr,startPos);
            if(!succeedInExtraction)
                tryToExtractAValidConst(nlr,startPos);
            if(!succeedInExtraction)
                tryToExtractSpecialCharacters(currentChar,nlr,startPos);
        } catch (ErrorTokenTypeException e) {
            TokenType type = new PascalError();
            currentToken = new PasErrorToken(content,type,nlr,startPos);
            succeedInExtraction = true;
        }
    }

    @Override
    protected void tryToExpandAsRWFollowedByBlank(char currentChar,int nlr,int startPos) throws IOException {
        if(!Character.isLetter(currentChar)) return;
        // if c is a letter, to try to treat it like a RW, we should read as much char
        // as we can until we meet a char that is not a-z, A-Z;
        content = readLegalRWOrID(currentChar,true,WordKind.RW);
        // we may fail to read a RW successfully then we try to interpret it as an identifier.
        if (contentIsRW() && (source.peekChar() == ' ' || source.peekChar() == EOL)){
            succeedInExtraction = true;
            TokenType type = PascalReservedWord.RWMap.get(content.toLowerCase());
            currentToken = new PasRWToken(content,type,nlr,startPos);
        }
    }

    @Override
    protected boolean contentIsRW() { return PascalReservedWord.RWMap.containsKey(content.toLowerCase()); }

    @Override
    protected void tryToExtractAValidIdentifier(int nlr,int startPos) throws IOException {
        char currentChar = source.readCharOnCurrOffset();
        // If we reach this place it means either content is not empty or currentChar is '_'
        if(content.equals("") && source.readCharOnCurrOffset() == '_'){
            // currentChar is '_'
            content = readLegalRWOrID(currentChar,true,WordKind.ID);
            // single _ must be followed by some other char to make a valid ID.
            if(content.length() > 1){
                succeedInExtraction = true;
                TokenType type = new PascalVar();
                currentToken = new PasVarToken(content,type,nlr,startPos);
            };
        }
        else if(!content.equals("")){
            // content is not empty
            succeedInExtraction = true;
            if((content.equals("true") || content.equals("false")) && (source.peekChar() == ' ' || source.peekChar() == EOL)){
                TokenType type = content.equals("true") ? new PascalTrue() : new PascalFalse();
                succeedInExtraction = true;
                currentToken = new PasBoolToken(content,type,nlr,startPos);
            }else{
                boolean continueCondition = Character.isLetterOrDigit(currentChar) || currentChar == '_';
                String readMore = readLegalRWOrID(currentChar,continueCondition,WordKind.ID);
                content = content + readMore;
                TokenType type = new PascalVar();
                succeedInExtraction = true;
                currentToken = new PasVarToken(content,type,nlr,startPos);
            }
        }
    }

    // TODO implement this !
    @Override
    protected void tryToExtractAValidConst(int nlr,int startPos) throws ErrorTokenTypeException, IOException {
        char currentChar = source.readCharOnCurrOffset();
        if(currentChar == '\'' || currentChar == '"'){
            // In this case we will read a string token
            //String a = '9'\"Hello, '\" int \"' t""'"''"\"" ;
            assert content.equals("");
            tryToExtractValidString(currentChar, nlr, startPos);
        }else if(Character.isDigit(currentChar) || currentChar == '.'){
            // In this case we will read a number token
            assert content.equals("");
            tryToExtractPositiveNumber(currentChar, nlr, startPos);
        }else if(currentChar == '-'){
            tryToExtractNegativeNum(nlr,startPos);
        }
    }

    @Override
    protected void tryToExtractValidString(char c,int nlr,int startPos) throws IOException, ErrorTokenTypeException {
        char startChar = c;
        StringBuilder textBuffer = new StringBuilder();
        boolean continueCondition = true;
        while(continueCondition) {
            textBuffer.append(c);
            c=source.readNextChar();
            if(c == EOF){
                throw new ErrorTokenTypeException();
            }else if(c == '\'' && startChar == '\''){
                if(source.peekChar() == '\''){
                    while ((c == '\'') && (source.peekChar() == '\'')) {
                        textBuffer.append("''");
                        source.cursorMoveForward();
                        c = source.readNextChar();
                    }
                    continueCondition = (c != '\'');
                }
                else{
                    continueCondition = false;
                }
            }
            else if(c == '"' && startChar == '"'){
                if(source.peekChar() == '"'){
                    while ((c == '"') && (source.peekChar() == '"')) {
                        textBuffer.append("\"\"");
                        source.cursorMoveForward();
                        c = source.readNextChar();
                    }
                    continueCondition = (c != '"');
                }
                else{
                    continueCondition = false;
                }
            }else{
                continueCondition = true;
            }
        }
        textBuffer.append(c);
        content = textBuffer.toString();
        succeedInExtraction = true;
        currentToken = new PasStringToken(textBuffer.toString(),new PascalString(),nlr,startPos);
        textBuffer.deleteCharAt(0);
        textBuffer.deleteCharAt(textBuffer.length()-1);
        currentToken.setValue(textBuffer.toString());
    }

    @Override
    protected void tryToExtractPositiveNumber(char startChar,int nlr,int startPos) throws ErrorTokenTypeException, IOException {
        // TODO need to handle range for number type, if a number has too many digits, it may be wrong.
        String wholeDigits = null;     // 小数点前的数字
        String fractionDigits = null;  // 小数点后的数字
        String exponentDigits = null;  // 标识指数的数字
        char exponentSign = '+';       // 指数标识 + 或-，默认为+
        boolean sawDotDot = false;     // 是否有 .. Token

        TokenType type = new PascalInteger();  // 默认为整数

        if(startChar == '.'){
            char nextChar = source.peekChar();
            if(nextChar == '.' || !Character.isDigit(nextChar)){
                return;
            }
            wholeDigits = "0";
        }else{
            wholeDigits = unsignedIntegerDigits();
        }

        // 接下来是否出现 .
        // 可能是个小数点 . 也可能是表示range .. 的第一个点
        StringBuilder textBuffer = new StringBuilder();
        char currentChar = source.readCharOnCurrOffset();
        if (currentChar == '.' && startChar != '.') {
            if (source.peekChar() == '.') {
                sawDotDot = true;  //表示 range的..，不再抽取，直接计算值
                // here we don't return because we need to compute the value of wholeDigits
            }
            else if(Character.isDigit(source.peekChar())){
                type = new PascalReal();  // 带点表示实数
                textBuffer.append(currentChar);
                source.cursorMoveForward();
                fractionDigits = unsignedIntegerDigits();
            }
        }

        // 是否有指数部分比如数可以为34.15E14
        currentChar = source.readCharOnCurrOffset();
        if (!sawDotDot && ((currentChar == 'E') || (currentChar == 'e'))) {
            type = new PascalReal();  // 带点表示实数
            textBuffer.append(currentChar);
            currentChar = source.readNextChar();

            // 指数有正负符号位？
            if ((currentChar == '+') || (currentChar == '-')) {
                textBuffer.append(currentChar);
                exponentSign = currentChar;
                source.cursorMoveForward();
            }

            // 抽取指数部分数字，34.15E14则抽取 14
            exponentDigits = unsignedIntegerDigits();
        }

        //计算整数值
        if (type instanceof PascalInteger) {
            int integerValue = computeIntegerValue(wholeDigits);
            succeedInExtraction = true;
            currentToken = new PasIntToken(wholeDigits,type,nlr,startPos);
            currentToken.setValue(integerValue);
        } else if (type instanceof PascalReal) {
            float floatValue = computeFloatValue(wholeDigits, fractionDigits,
                    exponentDigits, exponentSign);

//            if (type != ERROR) {
//                value = new Float(floatValue);
//            }
            succeedInExtraction = true;
            currentToken = new PasRealToken(wholeDigits+'.'+fractionDigits+'E'+exponentDigits,type,nlr,startPos);
            currentToken.setValue(floatValue);
        }

    }

    private int computeIntegerValue(String digits) throws ErrorTokenTypeException {
        if (digits == null) {
            return 0;
        }

        int integerValue = 0;
        int prevValue = -1;    // 如果有溢出发生，-1为正溢出的最大值
        int index = 0;

        //根据十进制数每位权值计算其值，比如123 = 100*1+20*2+3 => 10(10*(1)+2)+3，广义表
        while ((index < digits.length()) && (integerValue >= prevValue)) {
            prevValue = integerValue;
            integerValue = 10*integerValue +
                    Character.getNumericValue(digits.charAt(index++));
        }

        // 正常无溢出
        if (integerValue >= prevValue) {
            return integerValue;
        }

        else {
            throw new ErrorTokenTypeException();
        }
    }

    private float computeFloatValue(String wholeDigits, String fractionDigits,
                                    String exponentDigits, char exponentSign) throws ErrorTokenTypeException {
        double floatValue = 0.0;
        int exponentValue = computeIntegerValue(exponentDigits);
        String digits = wholeDigits;

        if (exponentSign == '-') {
            exponentValue = -exponentValue;
        }

        //如果有小数，则指数降幂，而小数变整，比如 10.1E2，表示10.1*10^2=1010相当于101*10^1即101E1
        if (fractionDigits != null) {
            exponentValue -= fractionDigits.length();
            digits += fractionDigits;
        }

        // 超位
        if (Math.abs(exponentValue + wholeDigits.length()) > MAX_EXPONENT) {
            throw new ErrorTokenTypeException();
        }

        int index = 0;
        while (index < digits.length()) {
            floatValue = 10*floatValue +
                    Character.getNumericValue(digits.charAt(index++));
        }
        if (exponentValue != 0) {
            floatValue *= Math.pow(10, exponentValue);
        }

        return (float) floatValue;
    }

    @Override
    protected String unsignedIntegerDigits() throws IOException {
        StringBuilder textBuffer = new StringBuilder();
        char currentChar = source.readCharOnCurrOffset();

        // 如果抽取数字，则至少有一个数字位，否则有问题。
        if (!Character.isDigit(currentChar)) {
            return null;
        }

        //遍历数字
        StringBuilder digits = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            textBuffer.append(currentChar);
            digits.append(currentChar);
            currentChar = source.readNextChar();  // consume digit
        }

        return digits.toString();
    }

    @Override
    protected void tryToExtractNegativeNum(int nlr,int pos) {

    }

    @Override
    protected void tryToExtractSpecialCharacters(char currentChar,int nlr,int startPos) throws IOException, ErrorTokenTypeException {
        content = readLegalSC(currentChar);
        if(contentIsSC()){
           TokenType tokenType = PascalSpecialChar.SCMap.get(content.toLowerCase());
            succeedInExtraction = true;
            currentToken = new PasSCToken(content,tokenType,nlr,startPos);
        }
    }

    @Override
    protected boolean contentIsSC() { return PascalSpecialChar.SCMap.containsKey(content.toLowerCase()); }

    @Override
    protected String readLegalRWOrID(char c ,boolean continueCondition,WordKind initialAttempt) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (continueCondition) {
            sb.append(c);
            c=source.readNextChar();
            if (c == Source.EOL || c == Source.EOF)
                break;
            switch (initialAttempt) {
                case RW:
                    continueCondition=Character.isLetter(c);
                    break;
                case ID:
                    continueCondition = Character.isLetter(c) || c == '_' || Character.isDigit(c);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        return sb.toString();
    }


    @Override
    protected String readLegalSC(char c) throws IOException, ErrorTokenTypeException {
        source.cursorMoveForward();
        switch (c) {

            // single char SC is easy to deal with
            case '+':  case '-':  case '*':  case '/':  case ',':  case ';':  case '\'': case '"':
            case '=':  case '(':  case ')':  case '[':  case ']':  case '{':  case '}':  case '^': {
                return Character.toString(c);
            }


            case ':': {
                if(source.readCharOnCurrOffset() == '='){
                    source.cursorMoveForward();
                    return ":=";
                }
                else
                    return ":";
            }

            case '<': {
                if(source.readCharOnCurrOffset() == '='){
                    source.cursorMoveForward();
                    return "<=";
                }
                if (source.readCharOnCurrOffset() == '>'){
                    source.cursorMoveForward();
                    return "<>";
                }
                return "<";
            }

            case '>': {
                if(source.readCharOnCurrOffset() == '='){
                    source.cursorMoveForward();
                    return ">=";
                }
                return ">";
            }

            case '.': {
                if(source.readCharOnCurrOffset() == '.')
                    return "..";
                return ".";
            }

            default: {
                System.out.println("we found a SC that is not in our map!");
                throw new ErrorTokenTypeException();
            }
        }
    }

}
