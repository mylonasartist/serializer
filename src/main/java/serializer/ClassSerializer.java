package serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

class ClassSerializer implements ISerializer<Class> {
    private static final String VERSION_FIELD_NAME = "serialVersionUID";

    private static final ClassSerializer instance = new ClassSerializer();

    private ClassSerializer() {
    }

    static ClassSerializer getInstance() {
        return instance;
    }

    @Override
    public void serialize(Class clazz, OutputStream output) throws IOException {
        String className = clazz.getName();
        StringSerializer.getInstance().serialize(className, output);
        long classVersion = getClassVersion(clazz);
        new DataOutputStream(output).writeLong(classVersion);
    }

    private long getClassVersion(Class clazz) {
        long classVersion = 0;
        try {
            Field versionField = clazz.getDeclaredField(VERSION_FIELD_NAME);
            versionField.setAccessible(true);
            Object fieldValue = versionField.get(clazz);
            if (fieldValue instanceof Number) {
                classVersion = ((Number) fieldValue).longValue();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // this can be ignored.
        }
        return classVersion;
    }

    @Override
    public Class deserialize(InputStream input) throws IOException, SerializationException {
        String className = StringSerializer.getInstance().deserialize(input);
        try {
            Class clazz = Class.forName(className);
            long deserializedClassVersion = new DataInputStream(input).readLong();
            long availableClassVersion = getClassVersion(clazz);
            if (deserializedClassVersion != availableClassVersion) {
                throw new IncompatibleClassVersionException("Incompatible versions for class: " + className +
                        ". Deserialized version: " + deserializedClassVersion +
                        ", available version: " + availableClassVersion);
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Cannot create class by name: " + className, e);
        }
    }
}
