package BackEnd;

import Intermediate.AST;
import Message.MessageBroadCaster;

public abstract class SourceCodePerformer {
    private MessageBroadCaster messageBroadCaster;

    private AST ast;

    public abstract void perform(AST ast);
}
