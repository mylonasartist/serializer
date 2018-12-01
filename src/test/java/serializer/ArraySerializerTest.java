package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.WithArray;

public class ArraySerializerTest {

    @Test
    public void testArraySerialization() {
        try {
            WithArray obj = new WithArray();
            byte[] contents = MySerializer.serialize(obj);
            WithArray deserialized = MySerializer.deserialize(contents);
            Assert.assertEquals(obj, deserialized);
        } catch (SerializationException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
