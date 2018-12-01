package serializer;

class IncompatibleClassVersionException extends SerializationException {
    IncompatibleClassVersionException(String message) {
        super(message);
    }
}
