package BackEnd;

import Intermediate.AST;
import Intermediate.SymTab;
import Message.MessageBroadCaster;

// Extracting a higher hierarchy so that we can extend this into, say, compiler in the future in addition to interpreter.
public abstract class BackEndCoreWidget extends MessageBroadCaster {

    protected AST ast;

    protected SymTab symTab;

    public abstract void performAST(AST ast,SymTab symTab);


}
