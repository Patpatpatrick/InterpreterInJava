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
    //private int numOfLineReading;


    // in constructor numOfLineRead is -1 means no char in this line has been read at all
    // this is the horizontal coordination
    private int toReadCharOnThisPos;

    public Source(String path) {
        readFromFileSystem(path);
        this.numOfLineRead= 0;
        //this.numOfLineReading = -1;
        this.toReadCharOnThisPos= -1;
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
        char ret = '0';
        System.out.println("====999===");
        // the first time that we read the file, so current offset is 0 dist away from the start of this line
        if (toReadCharOnThisPos == -1) {
            readWholeLine();
            if(currentWholeLine == null){
                ret = EOF;
                System.out.println("return value is " + ret);
                return ret;
            }
            ret=readNextChar();
            //System.out.println("return value is " + ret);
        }
        else if (toReadCharOnThisPos == currentWholeLine.length()) {
            System.out.println("return eol");
            ret = EOL;
        }
        else{
            System.out.println("=======");
            System.out.println(toReadCharOnThisPos);
            System.out.println(currentWholeLine.length());
            System.out.println("=======");
            ret=currentWholeLine.charAt(toReadCharOnThisPos);
            //System.out.println("return value is " + ret);
        }
        return ret;
    }

    public void cursorMoveForward() {
        if(currentWholeLine !=null)
            toReadCharOnThisPos = (toReadCharOnThisPos + 2) % (currentWholeLine.length() + 2) - 1;
    }
    public char readNextChar() throws IOException {
        cursorMoveForward();
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
        // assert toReadCharOnThisPos == 0;
        currentWholeLine= reader.readLine();
        numOfLineRead++;
        System.out.println("now whole line is " + currentWholeLine);
    }

    /**
     * Closes the stream and releases any system resources associated with it.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        reader.close();
    }

    public int getNumOfLineRead() {
        return numOfLineRead;
    }

    public int getCurrentOffset() {
        return toReadCharOnThisPos;
    }
}
