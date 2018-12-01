# serializer

Java 8 or later.

Example of usage:

`Object obj = ...;`

`byte[] contents = MySerializer.serialize(obj);`

`Object deserializedObj = MySerializer.deserialize(contents);`

Grammar:

Object -> Class [ Field[] ]

Class -> name:String version:long

String -> lengthBytes:int value(UTF-8)

Field -> name:String Value // we serialize name of the field because Javadoc says: "The elements in the returned array are not sorted and are not in any particular order"

Value -> isNull:int [type:V_TYPE Object | Array | String | Byte | Short | Int | Long | ... | BigDecimal | BigInteger | Date]

V_TYPE -> OBJECT | ARRAY | STRING | BYTE | SHORT | ... | BIGDECIMAL | BIGINTEGER | DATE

Array -> length:int type:A_TYPE [ Value[] ]

A_TYPE -> PRIMITIVE | OBJECT

**TODO:**
- Take into account inherited state - i.e. fields from superclass.
  The grammar for Object will be like:
  Object -> Class [ Field[] ] [ Class Field[] ... until Class == java.lang.Object]
- Provide setting final fields on Object.

**!!! Warning:** the implementation uses recursion - StackOverflowError possible on too deep nesting
    as well as on circular references.

**!!! Limitations:**
- Fields nesting depth should be small enough for not to cause StackOverflowError.
- There should be no circular references.
- Classes of instances must have default constructor.
- Not supported instance (fields, elements in collections, maps, etc.) types:
  - Dynamic proxies, e.g. java.lang.reflect.Proxy.
  - With runtime byte code modification.
- Inherited state not yet serialized - see the TODO.
