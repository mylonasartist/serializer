package serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MySerializer {
    private static final String VERSION = "1.0-SNAPSHOT";

    public static byte[] serialize(Object obj) throws SerializationException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            StringSerializer versionSerializer = StringSerializer.getInstance();
            versionSerializer.serialize(VERSION, output);
            ValueSerializer.getInstance().serialize(obj, output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static <T> T deserialize(byte[] contents) throws SerializationException {
        InputStream input = new ByteArrayInputStream(contents);
        try {
            StringSerializer versionSerializer = StringSerializer.getInstance();
            String version = versionSerializer.deserialize(input);
            if (!version.equals(VERSION))
            {
                throw new SerializationException("Serializer version not compatible with input object bytes");
            }
            T obj = (T) ValueSerializer.getInstance().deserialize(input);
            return obj;
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
