package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.WithArray;

import java.util.Arrays;

public class ArraySerializerTest {

    @Test
    public void testArraySerialization() {
        try {
            WithArray obj = new WithArray();
            byte[] contents = MySerializer.serialize(obj);
            WithArray deserialized = MySerializer.deserialize(contents);
            Assert.assertEquals(obj, deserialized);
        } catch (SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testPrimitiveArraySerialization() {
        int[] arr = new int[] {1, 2, 3};
        try {
            byte[] contents = MySerializer.serialize(arr);
            int[] deserializedObj = MySerializer.deserialize(contents);
            Assert.assertTrue(Arrays.equals(arr, deserializedObj));
        } catch (SerializationException e) {
            Assert.fail(e.getMessage());
        }

    }
}
