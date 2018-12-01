package serializer;

import java.io.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

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

        valueTypesMap.put(String.class, ValueType.STRING);
        valueTypesMap.put(Long.class, ValueType.LONG);
        valueTypesMap.put(Integer.class, ValueType.INTEGER);
        valueTypesMap.put(Short.class, ValueType.SHORT);
        valueTypesMap.put(Byte.class, ValueType.BYTE);
        valueTypesMap.put(Float.class, ValueType.FLOAT);
        valueTypesMap.put(Double.class, ValueType.DOUBLE);
        valueTypesMap.put(Boolean.class, ValueType.BOOLEAN);

        // TODO add more primitive and wrapper types to valueTypesMap

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
}
