package FrontEnd;

import Intermediate.AST;
import Message.MessageBroadCaster;

/**
 * This class is responsible for requesting a token from Scanner
 * So it has a member of Scanner
 *
 *
 *
 */

//TODO: should make the implementation of interface methods right in this abstract class!!

public abstract class Parser {

    private static MessageBroadCaster messageBroadCaster;

    private AST root;

    private boolean succeed;

    //Parser instance will refer to Scanner to obtain token instance
    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner=scanner;
    }

    /**
     * leave the subclass to do the concrete implementation.
     * @throws Exception
     */
    public abstract void parse() throws Exception;

    private Token getToken(){
        return scanner.extractToken();
    }

    /**
     *
     * @return # of errors in the source code
     */
    public int getErrorCount(){
        return 0;
    }
}
