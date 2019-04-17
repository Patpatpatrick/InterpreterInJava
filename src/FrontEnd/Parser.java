package FrontEnd;

import Intermediate.AST;
import Intermediate.SymTab;
import Message.MessageBroadCaster;

import java.io.IOException;

/**
 * This class is responsible for requesting a token from Scanner
 * So it has a member of Scanner
 */

//TODO: should make the implementation of interface methods right in this abstract class!!

public abstract class Parser extends MessageBroadCaster{

    private SymTab table; // generated symbol table

    private AST root;

    private boolean succeed;

    //Parser instance will refer to Scanner to obtain token instance
    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner=scanner;
        table = null;
        root = null;
    }

    /**
     * leave the subclass to do the concrete implementation.
     * @throws Exception
     */
    public abstract void parse() throws Exception;

    private Token currentToken() throws IOException {
        return scanner.currentToken();
    }

    private Token nextToken() throws IOException {
        return scanner.nextToken();
    }

    /**
     * language dependent, so make abstract.
     * @return # of errors in the source code
     */
    public abstract int getErrorCount();
}
