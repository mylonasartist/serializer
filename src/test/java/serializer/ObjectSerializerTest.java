package serializer;

import org.junit.Assert;
import org.junit.Test;
import types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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

    @Test
    public void testFinalFieldSerialization() {
        WithFinalField obj = new WithFinalField("honest JWT token: hhhh.eeeeee.ttttt");
        testObjectSerialization(obj);
    }

    @Test
    public void testDateSerialization() {
        Date obj = new Date();
        testObjectSerialization(obj);
    }

    @Test
    public void testBigDecimal() {
        BigDecimal obj = new BigDecimal("382934.892839284");
        testObjectSerialization(obj);
    }

    @Test
    public void testBigInteger() {
        BigInteger obj = new BigInteger("12391293921");
        testObjectSerialization(obj);
    }

    @Test
    public void testPrivateConstructor() {
        PrivateConstructor obj = PrivateConstructor.createInstance();
        obj.n = 150;
        testObjectSerialization(obj);
    }

    @Test
    public void testNoDefaultConstructor() {
        NoDefaultConstructor obj = new NoDefaultConstructor(15);
        testObjectSerialization(obj);
    }

    @Test
    public void testStringSerialization() {
        String obj = "Hello, World!";
        testObjectSerialization(obj);
    }
}
