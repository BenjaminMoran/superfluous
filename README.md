# superfluous

\[[Javadocs]\]

Java implementations of popular cryptographic hash functions

## Usage example

```java
Digest digest = HashFunctions.sha256().hash("Hello, world!".getBytes());
String expectedDigest = "315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3";
assertEquals(expectedDigest, digest.hex());
assertTrue(digest.verify("Hello, world!".getBytes()));
assertFalse(digest.verify("another message".getBytes()));
```

For more information, see the [Javadocs].

## Hash functions

The definitions of these hash functions are linked in [`HashFunctions`].

[`HashFunctions`]: https://benjaminmoran.github.io/superfluous/com/github/benjaminmoran/superfluous/hash/HashFunctions.html

* [SHA-256](src/main/java/com/github/benjaminmoran/superfluous/hash/Sha256Hasher.java) (256 bits;
  NIST)

[Javadocs]: https://benjaminmoran.github.io/superfluous/
