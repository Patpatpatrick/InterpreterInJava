import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is responsible for reading a file which contains the source code of some programming language
 * this class is not responsible for returning a token, token is made by scanner subclass that corresponding to a specific
 * language.
 *
 */

public class Source {

    private BufferedReader reader;

    // where you will be finding char to return
    private String currentWholeLine;

    // in constructor currentLineRead is -1 means no line has been read at all
    // this is the vertical coordination
    private int currentLineRead;


    // in constructor currentLineRead is -1 means no char in this line has been read at all
    // this is the horizontal coordination
    private int currentOffset;

    public Source(String path) {
        readFromFileSystem(path);
        this.currentLineRead= -1;
        this.currentOffset= -1;
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


    private char readOnCurrOffset() throws IOException {
        // the first time that we read the file
        if(currentOffset == -1 && currentLineRead == -1){

        }
        return (char) reader.read();
    }

    private void readWholeLine() throws IOException {
        assert currentOffset == -1;
        currentWholeLine= reader.readLine();
        if(currentWholeLine != null){
            currentLineRead++;
        }
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
