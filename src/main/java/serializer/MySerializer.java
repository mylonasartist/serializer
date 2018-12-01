package serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MySerializer {
    public static byte[] serialize(Object obj) throws SerializationException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ValueSerializer.getInstance().serialize(obj, output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static <T> T deserialize(byte[] contents) throws SerializationException {
        try {
            T obj = (T) ValueSerializer.getInstance().deserialize(new ByteArrayInputStream(contents));
            return obj;
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
