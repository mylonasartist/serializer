package serializer;

import java.io.ByteArrayOutputStream;

public class ObjectSerializer {

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        serialize(obj, output);
        return output.toByteArray();
    }

    public static Object deserialize(byte[] contents) {
        return null;
    }

    static void serialize(Object obj, ByteArrayOutputStream output) {
    }
}
