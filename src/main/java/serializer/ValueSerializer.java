package serializer;

import org.apache.commons.lang3.ClassUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ValueSerializer implements ISerializer<Object> {
    private static final ValueSerializer instance = new ValueSerializer();

    private static final Map<ValueType, ISerializer> valueSerializersMap = new HashMap<>();
    private static final Map<Class, ValueType> valueTypesMap = new HashMap<>();
    private static final Map<ValueType, Class> classToValueTypesMap = new HashMap<>();

    static {
        valueSerializersMap.put(ValueType.STRING, StringSerializer.getInstance());
        valueSerializersMap.put(ValueType.OBJECT, ObjectSerializer.getInstance());
        valueSerializersMap.put(ValueType.INTEGER, new IntegerSerializer());
        valueSerializersMap.put(ValueType.LONG, new LongSerializer());
        valueSerializersMap.put(ValueType.BYTE, new ByteSerializer());
        valueSerializersMap.put(ValueType.SHORT, new ShortSerializer());
        valueSerializersMap.put(ValueType.FLOAT, new FloatSerializer());
        valueSerializersMap.put(ValueType.DOUBLE, new DoubleSerializer());
        valueSerializersMap.put(ValueType.BOOLEAN, new BooleanSerializer());
        valueSerializersMap.put(ValueType.ARRAY, new ArraySerializer());
        valueSerializersMap.put(ValueType.COLLECTION, new CollectionSerializer());
        valueSerializersMap.put(ValueType.MAP, new MapSerializer());
        valueSerializersMap.put(ValueType.DATE, new DateSerializer());
        valueSerializersMap.put(ValueType.BIGDECIMAL, new BigDecimalSerializer());
        valueSerializersMap.put(ValueType.BIGINTEGER, new BigIntegerSerializer());

        valueTypesMap.put(String.class, ValueType.STRING);
        valueTypesMap.put(Long.class, ValueType.LONG);
        valueTypesMap.put(Integer.class, ValueType.INTEGER);
        valueTypesMap.put(Short.class, ValueType.SHORT);
        valueTypesMap.put(Byte.class, ValueType.BYTE);
        valueTypesMap.put(Float.class, ValueType.FLOAT);
        valueTypesMap.put(Double.class, ValueType.DOUBLE);
        valueTypesMap.put(Boolean.class, ValueType.BOOLEAN);
        valueTypesMap.put(Date.class, ValueType.DATE);
        valueTypesMap.put(BigDecimal.class, ValueType.BIGDECIMAL);
        valueTypesMap.put(BigInteger.class, ValueType.BIGINTEGER);

        valueTypesMap.forEach((aClass, valueType) -> classToValueTypesMap.put(valueType, aClass));
    }

    private ValueSerializer() {
    }

    static ValueSerializer getInstance() {
        return instance;
    }

    @Override
    public void serialize(Object value, OutputStream output)
            throws IOException, SerializationException {
        if (value != null) {
            // is not null
            output.write(0);
            ValueType valueType = defineValueType(value.getClass());
            StringSerializer.getInstance().serialize(valueType.name(), output);
            ISerializer valueSerializer = valueSerializersMap.get(valueType);
            if (valueSerializer == null)
            {
                throw new SerializationException("Cannot find serializer for value of class: " +
                        value.getClass().getName());
            }
            valueSerializer.serialize(value, output);
        } else {
            output.write(1);
        }
    }

    private ValueType defineValueType(Class valueClass) {
        ValueType valueType;
        if (valueClass.isArray()) {
            valueType = ValueType.ARRAY;
        } else if (ClassUtils.isAssignable(valueClass, Map.class)) {
            valueType = ValueType.MAP;
        } else if (ClassUtils.isAssignable(valueClass, Collection.class)) {
            valueType = ValueType.COLLECTION;
        } else if (valueTypesMap.containsKey(valueClass)) {
            valueType = valueTypesMap.get(valueClass);
        } else {
            valueType = ValueType.OBJECT;
        }
        return valueType;
    }

    @Override
    public Object deserialize(InputStream input) throws IOException, SerializationException {
        Object value = null;
        int isNull = input.read();
        if (isNull == 0) {
            String valueTypeName = StringSerializer.getInstance().deserialize(input);
            ValueType valueType = ValueType.valueOf(valueTypeName);
            ISerializer serializer = valueSerializersMap.get(valueType);
            value = serializer.deserialize(input);
        }
        return value;
    }

    private static class ByteSerializer implements ISerializer<Byte> {
        @Override
        public void serialize(Byte value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeByte(value);
        }

        @Override
        public Byte deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readByte();
        }
    }

    private static class ShortSerializer implements ISerializer<Short> {
        @Override
        public void serialize(Short value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeShort(value);
        }

        @Override
        public Short deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readShort();
        }
    }

    private static class IntegerSerializer implements ISerializer<Integer> {
        @Override
        public void serialize(Integer value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeInt(value);
        }

        @Override
        public Integer deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readInt();
        }
    }

    private static class LongSerializer implements ISerializer<Long> {
        @Override
        public void serialize(Long value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeLong(value);
        }

        @Override
        public Long deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readLong();
        }
    }

    private static class FloatSerializer implements ISerializer<Float> {
        @Override
        public void serialize(Float value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeFloat(value);
        }

        @Override
        public Float deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readFloat();
        }
    }

    private static class DoubleSerializer implements ISerializer<Double> {
        @Override
        public void serialize(Double value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeDouble(value);
        }

        @Override
        public Double deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readDouble();
        }
    }

    private static class BooleanSerializer implements ISerializer<Boolean> {
        @Override
        public void serialize(Boolean value, OutputStream output)
                throws IOException {
            new DataOutputStream(output).writeBoolean(value);
        }

        @Override
        public Boolean deserialize(InputStream input) throws IOException {
            return new DataInputStream(input).readBoolean();
        }
    }

    private static class ArraySerializer implements ISerializer<Object> {

        @Override
        public void serialize(Object array, OutputStream output) throws IOException, SerializationException {
            int length = Array.getLength(array);
            output.write(length);

            Class arrayType = array.getClass().getComponentType();
            ClassSerializer.getInstance().serialize(arrayType, output);

            ValueSerializer valueSerializer = ValueSerializer.getInstance();
            for (int i = 0; i < length; i++) {
                Object currentElement = Array.get(array, i);
                valueSerializer.serialize(currentElement, output);
            }
        }

        @Override
        public Object deserialize(InputStream input) throws IOException, SerializationException {
            int length = input.read();
            Class arrayType = ClassSerializer.getInstance().deserialize(input);
            Object array = Array.newInstance(arrayType, length);
            ValueSerializer valueSerializer = ValueSerializer.getInstance();
            for (int i = 0; i < length; i++) {
                Object element = valueSerializer.deserialize(input);
                Array.set(array, i, element);
            }
            return array;
        }
    }

    private static class CollectionSerializer implements ISerializer<Collection> {

        @Override
        public void serialize(Collection collection, OutputStream output) throws IOException, SerializationException {
            output.write(collection.size());
            ClassSerializer.getInstance().serialize(collection.getClass(), output);
            for (Object item: collection) {
                ValueSerializer.getInstance().serialize(item, output);
            }
        }

        @Override
        public Collection deserialize(InputStream input) throws IOException, SerializationException {
            int size = input.read();
            Class collectionClass = ClassSerializer.getInstance().deserialize(input);
            try {
                // TODO consider create class like InstantiationHelper for instances
                // TODO creation - finding ctors, best effort logic, exception handling, etc.
                Collection collection = (Collection) collectionClass.getConstructor().newInstance();
                for (int i = 0; i < size; i++) {
                    Object item = ValueSerializer.getInstance().deserialize(input);
                    collection.add(item);
                }
                return collection;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new SerializationException("Cannot create Collection instance of class: " + collectionClass.getName());
            }
        }
    }

    private static class MapSerializer implements ISerializer<Map> {

        @Override
        public void serialize(Map map, OutputStream output) throws IOException, SerializationException {
            output.write(map.size());
            ClassSerializer.getInstance().serialize(map.getClass(), output);
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            ValueSerializer valueSerializer = ValueSerializer.getInstance();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                valueSerializer.serialize(entry.getKey(), output);
                valueSerializer.serialize(entry.getValue(), output);
            }
        }

        @Override
        public Map deserialize(InputStream input) throws IOException, SerializationException {
            int size = input.read();
            Class mapClass = ClassSerializer.getInstance().deserialize(input);
            try {
                Map map = (Map) mapClass.getConstructor().newInstance();
                ValueSerializer valueSerializer = ValueSerializer.getInstance();
                for (int i = 0; i < size; i++) {
                    Object key = valueSerializer.deserialize(input);
                    Object value = valueSerializer.deserialize(input);
                    map.put(key, value);
                }
                return map;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new SerializationException("Cannot create Map instance of class: " + mapClass.getName());
            }
        }
    }

    private static class DateSerializer implements ISerializer<Date> {

        @Override
        public void serialize(Date date, OutputStream output) throws IOException, SerializationException {
            new DataOutputStream(output).writeLong(date.getTime());
        }

        @Override
        public Date deserialize(InputStream input) throws IOException, SerializationException {
            return new Date(new DataInputStream(input).readLong());
        }
    }

    private static class BigDecimalSerializer implements ISerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal value, OutputStream output) throws IOException, SerializationException {
            new DataOutputStream(output).writeUTF(value.toEngineeringString());
        }

        @Override
        public BigDecimal deserialize(InputStream input) throws IOException, SerializationException {
            return new BigDecimal(new DataInputStream(input).readUTF());
        }
    }

    private static class BigIntegerSerializer implements ISerializer<BigInteger> {

        @Override
        public void serialize(BigInteger value, OutputStream output) throws IOException, SerializationException {
            byte[] valueContents = value.toByteArray();
            ValueSerializer.getInstance().serialize(valueContents, output);
        }

        @Override
        public BigInteger deserialize(InputStream input) throws IOException, SerializationException {
            byte[] valueContents = (byte[]) ValueSerializer.getInstance().deserialize(input);
            return new BigInteger(valueContents);
        }
    }
}
