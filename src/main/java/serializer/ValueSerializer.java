package serializer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ValueSerializer implements ISerializer<Object> {
    private static final ValueSerializer instance = new ValueSerializer();

    private static final Map<V_TYPE, ISerializer> valueSerializersMap = new HashMap<>();
    private static final Map<Class, V_TYPE> valueTypesMap = new HashMap<>();

    static {
        valueSerializersMap.put(V_TYPE.STRING, StringSerializer.getInstance());
        valueSerializersMap.put(V_TYPE.OBJECT, ObjectSerializer.getInstance());
        valueSerializersMap.put(V_TYPE.INTEGER, new IntegerSerializer());
        valueSerializersMap.put(V_TYPE.LONG, new LongSerializer());

        valueTypesMap.put(String.class, V_TYPE.STRING);
        valueTypesMap.put(Integer.class, V_TYPE.INTEGER);
        valueTypesMap.put(Long.class, V_TYPE.LONG);
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
            V_TYPE valueType = defineValueType(value);
            StringSerializer.getInstance().serialize(valueType.name(), output);
            ISerializer valueSerializer = valueSerializersMap.get(valueType);
            valueSerializer.serialize(value, output);
        } else {
            output.write(1);
        }
    }

    private V_TYPE defineValueType(Object value) {
        V_TYPE valueType;
        if (value.getClass().isArray()) {
            valueType = V_TYPE.ARRAY;
        } else if (valueTypesMap.containsKey(value.getClass())) {
            valueType = valueTypesMap.get(value.getClass());
        } else {
            valueType = V_TYPE.OBJECT;
        }
        return valueType;
    }

    @Override
    public Object deserialize(InputStream input) throws IOException, SerializationException {
        Object value = null;
        int isNull = input.read();
        if (isNull == 0) {
            String valueTypeName = StringSerializer.getInstance().deserialize(input);
            V_TYPE valueType = V_TYPE.valueOf(valueTypeName);
            ISerializer serializer = valueSerializersMap.get(valueType);
            value = serializer.deserialize(input);
        }
        return value;
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
}
