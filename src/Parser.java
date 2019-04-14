import java.util.ArrayList;

/**
 * This class is responsible for requesting a token from Scanner
 * So it has a member of Scanner
 */


public abstract class Parser {

    // this is what parser will pass to the backend at last.
    private ArrayList<Token> tokens;

    private AST root;

    //Parser instance will refer to Scanner to obtain token instance
    private final Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner=scanner;
        this.tokens = new ArrayList<>();
    }

    /**
     * leave the subclass to do the concrete implementation.
     * @throws Exception
     */
    public abstract void parse() throws Exception;

    private void addToken(Token token){
        tokens.add(token);
    }

    private Token getNextToken(){
        return scanner.getNextToken();
    }

    /**
     *
     * @return # of errors in the source code
     */
    public int getErrorCount(){
        return 0;
    }
}
