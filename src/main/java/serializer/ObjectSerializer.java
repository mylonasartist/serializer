package serializer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
        ClassSerializer classSerializer = ClassSerializer.getInstance();
        classSerializer.serialize(clazz, output);

        serializeFields(obj, clazz, output);

        List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(clazz);
        Iterator<Class<?>> classIterator = superclasses.iterator();

        Class<?> currentSuperclass;
        while (classIterator.hasNext() && (currentSuperclass = classIterator.next()) != Object.class ) {
            serializeFields(obj, currentSuperclass, output);
        }
    }

    private void serializeFields(Object obj, Class clazz, OutputStream output) throws IOException, SerializationException {
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
        ClassSerializer classSerializer = ClassSerializer.getInstance();
        Class clazz = classSerializer.deserialize(input);
        try {
            Object obj = clazz.getConstructor().newInstance();
            deserializeFields(obj, clazz, input);

            List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(clazz);
            Iterator<Class<?>> classIterator = superclasses.iterator();

            Class<?> currentSuperclass;
            while (classIterator.hasNext() && (currentSuperclass = classIterator.next()) != Object.class ) {
                deserializeFields(obj, currentSuperclass, input);
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new SerializationException("Cannot create object of class: " + clazz.getName(), e);
        }
    }

    private void deserializeFields(Object obj, Class clazz, InputStream input) throws IOException, SerializationException {
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
    }
}
