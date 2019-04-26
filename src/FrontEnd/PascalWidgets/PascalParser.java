package FrontEnd.PascalWidgets;

import FrontEnd.EoFToken;
import FrontEnd.Parser;
import FrontEnd.Lexer;
import FrontEnd.Token;

import Message.Message;

import java.util.ArrayList;
import java.util.List;

import static Message.MessageType.PARSER_SUMMARY;

public class PascalParser extends Parser {

    public List<Token> plist = new ArrayList<>();
    public PascalParser(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void parse() throws Exception {
        Token token;
        long startTime = System.currentTimeMillis();
        while (!((token = nextToken()) instanceof EoFToken)) {
            plist.add(token);
        }
        float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
        setMessage(
                new Message(PARSER_SUMMARY,
                        new Number[] {token.getNumOfLine(), getErrorCount(),elapsedTime}
                        )
        );
        sendMessage();
    }

    @Override
    public int getErrorCount() {
        return 0;
    }
}
