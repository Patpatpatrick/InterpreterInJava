package BackEnd;

import Intermediate.AST;
import Message.Message;
import Message.MessageType;

/**
 * this is our interpreter
 */
public class Interpreter extends BackEndCoreWidget {

    @Override
    public void perform(AST ast) {
        long startTime = System.currentTimeMillis();
        float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
        int executionCount = 0;
        int runtimeErrors = 0;
        messageBroadCaster.setMessage(
            new Message(
                    MessageType.INTERPRETER_SUMMARY,
                    new Number[] {executionCount,runtimeErrors,elapsedTime}
            )
        );
        messageBroadCaster.sendMessage();
    }
}
