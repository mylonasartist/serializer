package serializer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
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
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
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
            Object obj;
            Constructor constructor;
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException exc) {
                constructor = clazz.getDeclaredConstructors()[0];
            }
            constructor.setAccessible(true);
            Type[] paramTypes = constructor.getGenericParameterTypes();
            if (ArrayUtils.isNotEmpty(paramTypes)) {
                // The best effort to create instance of class without default constructor.
                Object[] params = Arrays.stream(paramTypes).map(type -> null).toArray();
                obj = constructor.newInstance(params);
            }
            else {
                obj = constructor.newInstance();
            }
            deserializeFields(obj, clazz, input);

            List<Class<?>> superclasses = ClassUtils.getAllSuperclasses(clazz);
            Iterator<Class<?>> classIterator = superclasses.iterator();

            Class<?> currentSuperclass;
            while (classIterator.hasNext() && (currentSuperclass = classIterator.next()) != Object.class ) {
                deserializeFields(obj, currentSuperclass, input);
            }
            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new SerializationException("Cannot create object of class: " + clazz.getName(), e);
        }
    }

    private void deserializeFields(Object obj, Class clazz, InputStream input) throws IOException, SerializationException {
        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                deserializeField(obj, clazz, input, fields[i]);
            }
        }
    }

    private void deserializeField(Object obj, Class clazz, InputStream input, Field field)
            throws IOException, SerializationException {
        String fieldName = field.getName();
        Object value = ValueSerializer.getInstance().deserialize(input);
        int modifiers = field.getModifiers();
        if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
            field.setAccessible(true);
            Field modifiersField = null;
            if (Modifier.isFinal(field.getModifiers())) {
                try {
                    modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new SerializationException("Cannot modify final field: " +
                            clazz.getName() + "." + fieldName);
                } finally {
                    // return modifier final
                    if (modifiersField != null) {
                        modifiersField.setAccessible(true);
                        try {
                            modifiersField.setInt(field, field.getModifiers() & 0x11111111);
                        } catch (IllegalAccessException e) {
                            // this can be ignored.
                        }
                    }
                }
            }
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                throw new SerializationException("Cannot set field " + fieldName +
                        " on object of class: " + clazz.getName(), e);
            }
        }
    }
}
