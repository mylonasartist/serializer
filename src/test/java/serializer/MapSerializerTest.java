package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.ComplexState;
import types.SimpleState;
import types.WithMap;

import java.util.HashMap;
import java.util.Map;

public class MapSerializerTest {

    @Test
    public void testHashMapSerialization() {
        Map<Object, Object> obj = createMap();
        testSerialization(obj);
    }

    private void testSerialization(Object obj) {
        try {
            byte[] contents = MySerializer.serialize(obj);
            Object deserializedObj = MySerializer.deserialize(contents);
            Assert.assertEquals(obj, deserializedObj);
        } catch (SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private Map<Object, Object> createMap() {
        Map<Object, Object> obj = new HashMap<>();
        ComplexState key = new ComplexState();
        key.message = "Hi Hi Hi Hi";
        obj.put(key, new SimpleState());
        obj.put(Integer.valueOf(10), Float.valueOf(1000.2222f));
        return obj;
    }

    @Test
    public void testObjectWithMapSerialization() {
        WithMap obj = new WithMap();
        obj.mapField = createMap();
        testSerialization(obj);
    }
}
