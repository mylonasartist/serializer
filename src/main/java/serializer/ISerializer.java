package serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

interface ISerializer<T> {
    void serialize(T value, OutputStream output) throws IOException, SerializationException;

    T deserialize(InputStream input) throws IOException, SerializationException;
}
