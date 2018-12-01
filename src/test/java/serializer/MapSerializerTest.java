package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.ComplexState;
import types.SimpleState;

import java.util.HashMap;
import java.util.Map;

public class MapSerializerTest {

    @Test
    public void testHashMapSerialization() {
        Map<Object, Object> obj = new HashMap<>();
        ComplexState key = new ComplexState();
        key.message = "Hi Hi Hi Hi";
        obj.put(key, new SimpleState());
        obj.put(Integer.valueOf(10), Float.valueOf(1000.2222f));
        try {
            byte[] contents = MySerializer.serialize(obj);
            Map deserializedObj = MySerializer.deserialize(contents);
            Assert.assertEquals(obj, deserializedObj);
        } catch (SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }
}
