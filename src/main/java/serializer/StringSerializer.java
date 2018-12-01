package serializer;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

class StringSerializer implements ISerializer<String> {

    private static final StringSerializer instance = new StringSerializer();

    private StringSerializer() {
    }

    static StringSerializer getInstance() {
        return instance;
    }

    public void serialize(String value, OutputStream output) throws IOException {
        new DataOutputStream(output).writeUTF(value);
    }

    public String deserialize(InputStream input) throws IOException {
        return new DataInputStream(input).readUTF();
    }
}
