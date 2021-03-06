# serializer

Java 8 or later.

Unit tests confirm the correct job :)

Example of usage:

`Object obj = ...;`

`byte[] contents = MySerializer.serialize(obj);`

`Object deserializedObj = MySerializer.deserialize(contents);`


**Grammar:**

Value -> isNull:int [type:ValueType Object | Array | Collection | Map | String | Byte | Short | Integer | Long | Float | Double | Boolean | BigDecimal | BigInteger | Date]

ValueType -> OBJECT | ARRAY | COLLECTION | MAP | STRING | BYTE | SHORT | INTEGER | LONG | FLOAT | DOUBLE | BOOLEAN | BIGDECIMAL | BIGINTEGER | DATE

Object -> Class [ Field[] ] [ Field[] foreach superclass ... until currentSuperclass == java.lang.Object ]

Class -> name:String version:long

Field -> Value

Array -> length:int Class [ Value[] ]

Collection -> size:int Class [ Value[] ]

Map -> size:int Class [ MapEntry[] ]

MapEntry -> Value Value

**TODO:**
- Provide serialization of multidimensional arrays.

**!!! Warning:** the implementation uses recursion - StackOverflowError possible on too deep nesting
    as well as on circular references which includes the case of link between instances of outer and inner classes.

**!!! Limitations:**
- Fields nesting depth should be small enough for not to cause StackOverflowError.
- There should be no circular references (includes the case of link between instances of outer and inner classes).
- Classes of instances must have either default constructor or allow NULL params to constructor.
- Not supported instance (fields, elements in collections, maps, etc.) types:
  - Dynamic proxies, e.g. java.lang.reflect.Proxy.
  - With runtime byte code modification.
- Multidimensional arrays are not yet supported - see the TODO.
