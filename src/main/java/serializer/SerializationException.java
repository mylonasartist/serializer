package serializer;

public class SerializationException extends Exception {
    SerializationException(String message) {
        super(message);
    }

    SerializationException(Throwable cause) {
        super(cause);
    }

    SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
