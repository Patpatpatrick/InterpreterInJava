package FrontEnd;

import Intermediate.AST;
import Intermediate.SymTab;
import Message.MessageBroadCaster;

import java.io.IOException;

/**
 * This class is responsible for requesting a token from Lexer
 * So it has a member of Lexer
 */

//TODO: should make the implementation of interface methods right in this abstract class!!

public abstract class Parser extends MessageBroadCaster{

    private SymTab table; // generated symbol table

    private AST root;

    private boolean succeed;

    //Parser instance will refer to Lexer to obtain token instance
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer=lexer;
        table = null;
        root = null;
    }

    public SymTab getTable() {
        return table;
    }

    public AST getRoot() {
        return root;
    }

    /**
     * leave the subclass to do the concrete implementation.
     * @throws Exception
     */
    public abstract void parse() throws Exception;

    private Token currentToken() throws IOException {
        return lexer.getCurrentToken();
    }

    public Token nextToken() throws IOException {
        return lexer.nextToken();
    }

    /**
     * language dependent, so make abstract.
     * @return # of errors in the source code
     */
    public abstract int getErrorCount();
}
