package serializer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringSerializerTest {

    private ISerializer<String> serializer = StringSerializer.getInstance();

    @Test
    public void testSerialize() {
        String value = "Hello, World!";
        ByteArrayOutputStream output = null;
        InputStream input = null;
        try {
            output = new ByteArrayOutputStream();
            serializer.serialize(value, output);
            output.write("This is a lot of garbage to test the correct data is read".getBytes());
            input = new ByteArrayInputStream(output.toByteArray());
            String deserializedValue = serializer.deserialize(input);
            Assert.assertEquals(value, deserializedValue);
        } catch (IOException | SerializationException e) {
            Assert.fail(e.getMessage());
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
    }
}
