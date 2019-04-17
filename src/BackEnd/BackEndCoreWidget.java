package BackEnd;

import Intermediate.AST;
import Message.MessageBroadCaster;

public abstract class BackEndCoreWidget {
    protected MessageBroadCaster messageBroadCaster;

    protected AST ast;

    public abstract void perform(AST ast);
}
