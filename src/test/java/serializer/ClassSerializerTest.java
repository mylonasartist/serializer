package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.Stateless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ClassSerializerTest {
    @Test
    public void testClassSerialization() {
        ISerializer<Class> serializer = ClassSerializer.getInstance();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            serializer.serialize(Stateless.class, output);
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            Class clazz = serializer.deserialize(input);
            Assert.assertEquals(Stateless.class, clazz);
        } catch (IOException | SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testIncompatibleClassVersionException() {
        ISerializer<Class> serializer = ClassSerializer.getInstance();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            serializer.serialize(Stateless.class, output);
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            Stateless.serialVersionUID += 1;
            Class clazz = serializer.deserialize(input);
            Assert.fail("Should have been IncompatibleClassVersionException raised.");
        } catch (IncompatibleClassVersionException expectedException) {
            // this exception is expected.
        } catch (IOException | SerializationException e) {
            Assert.fail(e.getMessage());
        }
    }
}
