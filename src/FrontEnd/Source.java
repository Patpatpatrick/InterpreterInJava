package FrontEnd;

import Message.Message;
import Message.MessageBroadCaster;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is responsible for reading a file which contains the source code of some programming language
 * this class is not responsible for returning a token, token is made by scanner subclass that corresponding to a specific
 * language.
 *
 */

public class Source extends MessageBroadCaster{

    public static final char EOL = '\n';
    public static final char EOF = (char) 0;

    private BufferedReader reader;

    // where you will be finding char to return
    private String currentWholeLine;

    // in constructor numOfLineRead is -1 means no line has been read at all
    // this is the vertical coordination
    private int numOfLineRead;


    // in constructor numOfLineRead is -1 means no char in this line has been read at all
    // this is the horizontal coordination
    private int toReadCharOnThisPos;

    public Source(String path) {
        readFromFileSystem(path);
        this.numOfLineRead= 0;
        this.toReadCharOnThisPos= 0;
    }

    /**
     * @param path is the path of the file in the file system
     * @throws IOException
     */
    public void readFromFileSystem(String path) {
        // reader = new BufferedReader(new FileReader(path));
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public char readOnCurrOffset() throws IOException {
        char ret;
        // the first time that we read the file, so current offset is 0 dist away from the start of this line
        if (toReadCharOnThisPos == 0) {
            readWholeLine();
            if(currentWholeLine != null){
                numOfLineRead++;
                ret=currentWholeLine.charAt(toReadCharOnThisPos);
            }else{
                ret = EOF;
            }
        }
        else if (toReadCharOnThisPos == currentWholeLine.length()) {
            ret = EOL;
        }
        else{
            ret=currentWholeLine.charAt(toReadCharOnThisPos);
        }
        return ret;
    }

    public char readNextChar() throws IOException {
        if(toReadCharOnThisPos == currentWholeLine.length()){
            toReadCharOnThisPos = 0;
        }
        else{
            toReadCharOnThisPos++;
        }
        return readOnCurrOffset();
    }

    /**
     * peek next char but not consume the current char。
     * @return
     * @throws Exception
     */
    public char peekChar()
            throws Exception
    {
        readOnCurrOffset();
        if (currentWholeLine == null) {
            return EOF;
        }

        int nextPos = toReadCharOnThisPos + 1;
        return nextPos < currentWholeLine.length() ? currentWholeLine.charAt(nextPos) : EOL;
    }

    private void readWholeLine() throws IOException {
        assert toReadCharOnThisPos == 0;
        currentWholeLine= reader.readLine();
    }

    /**
     * Closes the stream and releases any system resources associated with it.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        reader.close();
    }


}
