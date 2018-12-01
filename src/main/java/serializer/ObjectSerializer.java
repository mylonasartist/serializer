package serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ObjectSerializer implements ISerializer<Object> {

    private static final ObjectSerializer instance = new ObjectSerializer();

    private ObjectSerializer() {
    }

    static ObjectSerializer getInstance() {
        return instance;
    }

    @Override
    public void serialize(Object obj, OutputStream output)
            throws IOException, SerializationException {
        if (obj == null) {
            throw new SerializationException("Cannot serialize null reference");
        }
        Class clazz = obj.getClass();
        ClassSerializer.getInstance().serialize(clazz, output);

        Field[] fields = clazz.getDeclaredFields();

        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                StringSerializer.getInstance().serialize(fields[i].getName(), output);
                try {
                    Object fieldValue = fields[i].get(obj);
                    ValueSerializer.getInstance().serialize(fieldValue, output);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object deserialize(InputStream input) throws IOException, SerializationException {
        Class clazz = ClassSerializer.getInstance().deserialize(input);
        try {
            Object obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                Map<String, Field> fieldsMap = new HashMap<>(fields.length);
                Arrays.stream(fields).forEach(field -> fieldsMap.put(field.getName(), field));
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = StringSerializer.getInstance().deserialize(input);
                    Object value = ValueSerializer.getInstance().deserialize(input);
                    // TODO provide setting final fields.
                    Field field = fieldsMap.get(fieldName);
                    field.setAccessible(true);
                    try {
                        field.set(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new SerializationException("Cannot set field " + fieldName +
                                " on object of class: " + clazz.getName(), e);
                    }
                }
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SerializationException("Cannot create object of class: " + clazz.getName(), e);
        }
    }
}
