package serializer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

class StringSerializer implements ISerializer<String> {

    // TODO make sure UTF-8 is Charset always available.
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private static final StringSerializer instance = new StringSerializer();

    private StringSerializer() {
    }

    static StringSerializer getInstance() {
        return instance;
    }

    public void serialize(String value, OutputStream output) throws IOException {
        byte[] contents = value.getBytes(CHARSET);
        output.write(contents.length);
        output.write(contents);
    }

    public String deserialize(InputStream input) throws IOException {
        int length = input.read();
        byte[] buffer = new byte[length];
        IOUtils.read(input, buffer);
        return new String(buffer, CHARSET);
    }
}
