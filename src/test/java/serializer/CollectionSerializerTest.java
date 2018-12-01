package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.ComplexState;

import java.util.*;

public class CollectionSerializerTest {
    @Test
    public void testSerializeArrayList() {
        ArrayList<Object> list = new ArrayList<>(Arrays.asList("Hi", "there", new ComplexState()));
        testCollectionSerialization(list);
    }

    private void testCollectionSerialization(Collection<Object> collection) {
        try {
            byte[] contents = MySerializer.serialize(collection);
            Collection<Object> deserializedCollection = MySerializer.deserialize(contents);
            Assert.assertEquals(collection, deserializedCollection);
        } catch (SerializationException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSerializeHashSet() {
        Set<Object> obj = new HashSet<>();
        obj.addAll(Arrays.asList("Hi", "there", new ComplexState()));
        testCollectionSerialization(obj);
    }
}
