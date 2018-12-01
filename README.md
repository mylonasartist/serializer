# serializer

Java 8 or later.

Example of usage:

`Object obj = ...;`

`byte[] contents = MySerializer.serialize(obj);`

`Object deserializedObj = MySerializer.deserialize(contents);`


**Grammar:**

Object -> Class [ Field[] ] [ Field[] foreach superclass ... until currentSuperclass == java.lang.Object ]

Class -> name:String version:long

String -> lengthBytes:int value(UTF-8)

Field -> name:String Value // we serialize name of the field because Javadoc says for Class.getDeclaredFields(): "The elements in the returned array are not sorted and are not in any particular order"

Value -> isNull:int [type:ValueType Object | Array | String | Byte | Short | Integer | Long | ... | BigDecimal | BigInteger | Date]

ValueType -> OBJECT | ARRAY | STRING | BYTE | SHORT | INTEGER | LONG | FLOAT | DOUBLE | BOOLEAN | BIGDECIMAL | BIGINTEGER | DATE

Array -> length:int Class [ Value[] ]
 

**TODO:**
- Resolve all TODOs
- Provide setting of final fields on Object.
- Provide support of multidimensional arrays.

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
- Multidimensional arrays are not yet supported - see the TODO.
