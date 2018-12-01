package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.SimpleComplexState;
import types.SimpleState;
import types.Stateless;

public class ObjectSerializerTest {
    @Test
    public void testStatelessObjectSerialization() {
        Stateless obj = new Stateless();
        testObjectSerialization(obj);
    }

    private void testObjectSerialization(Object obj) {
        try {
            byte[] contents = MySerializer.serialize(obj);
            Object deserializedObj = MySerializer.deserialize(contents);
            Assert.assertEquals(obj, deserializedObj);
        } catch (SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSimpleStateObjectSerialization() {
        SimpleState obj = new SimpleState();
        testObjectSerialization(obj);
    }

    @Test
    public void testSimpleComplextStateObjectSerialization() {
        SimpleComplexState obj = new SimpleComplexState();
        testObjectSerialization(obj);
    }
}
