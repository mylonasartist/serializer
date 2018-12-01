package serializer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ClassSerializer implements ISerializer<Class> {
    private static final String VERSION_FIELD_NAME = "serialVersionUID";

    private static final ClassSerializer instance = new ClassSerializer();

    private static final Map<String, Class> primitiveTypesMap = new HashMap<>();

    static {
        primitiveTypesMap.put("byte", byte.class);
        primitiveTypesMap.put("short", short.class);
        primitiveTypesMap.put("char", char.class);
        primitiveTypesMap.put("int", int.class);
        primitiveTypesMap.put("long", long.class);
        primitiveTypesMap.put("float", float.class);
        primitiveTypesMap.put("double", double.class);
        primitiveTypesMap.put("boolean", boolean.class);
    }

    private ClassSerializer() {
    }

    static ClassSerializer getInstance() {
        return instance;
    }

    @Override
    public void serialize(Class clazz, OutputStream output) throws IOException {
        String className = clazz.getName();
        StringSerializer.getInstance().serialize(className, output);
        if (!primitiveTypesMap.containsKey(className)) {
            long classVersion = getClassVersion(clazz);
            new DataOutputStream(output).writeLong(classVersion);
        }
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
            Class clazz = primitiveTypesMap.get(className);
            if (clazz == null) {
                clazz = Class.forName(className);
            }
            if (!primitiveTypesMap.containsKey(className)) {
                long deserializedClassVersion = new DataInputStream(input).readLong();
                long availableClassVersion = getClassVersion(clazz);
                if (deserializedClassVersion != availableClassVersion) {
                    throw new IncompatibleClassVersionException("Incompatible versions for class: " + className +
                            ". Deserialized version: " + deserializedClassVersion +
                            ", available version: " + availableClassVersion);
                }
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Cannot create class by name: " + className, e);
        }
    }
}
