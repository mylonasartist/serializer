package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.*;

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
        obj.n = 900;
        testObjectSerialization(obj);
    }

    @Test
    public void testComplextStateObjectSerialization() {
        ComplexState obj = new ComplexState();
        obj.boo = false;
        obj.obj.n = 1001;
        testObjectSerialization(obj);
    }

    @Test
    public void testInheritanceStateSerialization() {
        Subclass obj = new Subclass();
        obj.complexObjSuper.message = "HHHHOOOOOoooooo";
        testObjectSerialization(obj);
    }
}
