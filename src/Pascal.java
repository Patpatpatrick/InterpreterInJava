import BackEnd.BackEndCoreWidget;
import BackEnd.Interpreter;
import FrontEnd.Parser;
import FrontEnd.PascalWidgets.PascalLexer;
import FrontEnd.PascalWidgets.PascalParser;
import FrontEnd.PascalWidgets.PascalTokenType.PascalConst;
import FrontEnd.PascalWidgets.PascalTokenType.PascalSpecialChar;
import FrontEnd.Source;
import FrontEnd.TokenType.TokenType;
import Intermediate.AST;
import Intermediate.SymTab;
import Message.Message;
import Message.MessageObserver;
import Message.MessageType;


public class Pascal {
    private Parser parser;
    private Source source;
    private AST iCode;
    private SymTab symTab;
    private BackEndCoreWidget interpreter;

    /**
     * 编译或者解释源程序
     *
     * @param filePath
     *            源文件路径
     */
    public Pascal(String filePath) {
        try {
            source = new Source(filePath);
            SourceMessageListener a = new SourceMessageListener();
            source.addMessageListener(a);


            parser = new PascalParser(new PascalLexer(source));

            ParserMessageListener b = new ParserMessageListener();
            parser.addMessageListener(b);

            interpreter= new Interpreter();
            BackendMessageListener c = new BackendMessageListener();
            interpreter.addMessageListener(c);

            parser.parse();

            //always close the buffer when done.
            source.close();
            iCode = parser.getRoot();
            symTab = parser.getTable();
            interpreter.performAST(iCode, symTab);
        } catch (Exception ex) {
            System.out.println("***** Parser Error Occurs! *****");
            ex.printStackTrace();
        }
    }

    private static final String FLAGS = "[-ix]";
    private static final String USAGE = "使用方式: Pascal execute|compile " + FLAGS
            + " <源文件路径>";

    /**
     * 入口程序，参考Pascal构造函数的参数接受过程。<br>
     * 例如：compile -i hello.pas
     */
    public static void main(String args[]) {
        try {
            String path = "/Users/mac/Workspaces/Interpreter/hello.pas";
            new Pascal(path);
        } catch (Exception ex) {
            System.out.println(USAGE);
        }
    }

    private static final String SOURCE_LINE_FORMAT = "%03d %s";

    /**
     * 源（也就是源文件)的监听器，用于监听源文件的读取情况，如果注册了监听器，源每产生一条消息比如<br>
     * 读取了一行等，将会调用相应的监听器处理。典型的Observe模式
     */
    private class SourceMessageListener implements MessageObserver {

        @Override
        public void updateOnMessage(Message message) {
            MessageType type = message.getType();
            Object body[] = (Object[]) message.getBody();

            switch (type) {
                // 源读取了一行
                case SOURCE_LINE: {
                    int lineNumber = (Integer) body[0];
                    String lineText = (String) body[1];

                    System.out.println(String.format(SOURCE_LINE_FORMAT,
                            lineNumber, lineText));
                    break;
                }
            }
        }
    }

    private static final String PARSER_SUMMARY_FORMAT = "源文件共有\t%d行。"
            + "\n有\t%d个语法错误." + "\n解析共耗费\t%.2f秒.\n";

    private static final String TOKEN_FORMAT = ">>> %-15s %03d行 %2d列, 内容为\"%s\"";
    private static final String VALUE_FORMAT = ">>>                 值等于%s";
    private static final int PREFIX_WIDTH = 5;

    /**
     * Parser的监听器，监听来自Parser解析过程中产生的消息，还是Observe模式
     */
    private class ParserMessageListener implements MessageObserver {

        @Override
        public void updateOnMessage(Message message) {
            MessageType type = message.getType();

            switch (type) {
                case TOKEN: {
                    Object body[] = (Object[]) message.getBody();
                    int line = (Integer) body[0];
                    int position = (Integer) body[1];
                    TokenType tokenType = (TokenType) body[2];
                    String tokenText = (String) body[3];
                    Object tokenValue = body[4];

                    System.out.println(String.format(TOKEN_FORMAT, tokenType, line,
                            position, tokenText));
                    if (tokenValue != null) {
                        // TODO stub change back
                        if (tokenType == PascalSpecialChar.DOT_DOT) {
                            tokenValue = "\"" + tokenValue + "\"";
                        }

                        System.out.println(String.format(VALUE_FORMAT, tokenValue));
                    }

                    break;
                }

                case SYNTAX_ERROR: {
                    Object body[] = (Object[]) message.getBody();
                    int lineNumber = (Integer) body[0];
                    int position = (Integer) body[1];
                    String tokenText = (String) body[2];
                    String errorMessage = (String) body[3];

                    int spaceCount = PREFIX_WIDTH + position;
                    StringBuilder flagBuffer = new StringBuilder();

                    //下一行跳到token出现的位置
                    for (int i = 1; i < spaceCount; ++i) {
                        flagBuffer.append(' ');
                    }

                    //尖括号指出问题token
                    flagBuffer.append("^\n*** ").append(errorMessage);

                    if (tokenText != null) {
                        flagBuffer.append(" [在 \"").append(tokenText)
                                .append("\" 处]");
                    }

                    System.out.println(flagBuffer.toString());
                    break;
                }
                case PARSER_SUMMARY: {
                    Number body[] = (Number[]) message.getBody();
                    int statementCount = (Integer) body[0];
                    int syntaxErrors = (Integer) body[1];
                    float elapsedTime = (Float) body[2];
                    System.out.println("\n----------代码解析统计信--------------");
                    System.out.printf(PARSER_SUMMARY_FORMAT, statementCount,
                            syntaxErrors, elapsedTime);
                    break;
                }
            }
        }
    }

    private static final String INTERPRETER_SUMMARY_FORMAT = "共执行\t%d 条语句。"
            + "\n运行中发生了\t%d 个错误。" + "\n执行共耗费\t%.2f 秒。\n";

    private static final String COMPILER_SUMMARY_FORMAT = "共生成\t\t%d 条指令"
            + "\n代码生成共耗费\t%.2f秒\n";

    private class BackendMessageListener implements MessageObserver {

        @Override
        public void updateOnMessage(Message message) {
            MessageType type = message.getType();

            switch (type) {

                case INTERPRETER_SUMMARY: {
                    Number body[] = (Number[]) message.getBody();
                    int executionCount = (Integer) body[0];
                    int runtimeErrors = (Integer) body[1];
                    float elapsedTime = (Float) body[2];
                    System.out.println("\n----------解释统计信息------------");
                    System.out.printf(INTERPRETER_SUMMARY_FORMAT, executionCount,
                            runtimeErrors, elapsedTime);
                    break;
                }

                case COMPILER_SUMMARY: {
                    Number body[] = (Number[]) message.getBody();
                    int instructionCount = (Integer) body[0];
                    float elapsedTime = (Float) body[1];
                    System.out.println("\n----------编译统计信--------------");
                    System.out.printf(COMPILER_SUMMARY_FORMAT, instructionCount,
                            elapsedTime);
                    break;
                }
            }

        }
    }
}
