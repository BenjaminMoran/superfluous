package com.github.benjaminmoran.superfluous.hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HashFunctionsTest {
  @Test
  void testDocUsageExample() {
    Digest digest = HashFunctions.sha256().hash("Hello, world!".getBytes());
    String expectedDigest = "315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3";
    assertEquals(expectedDigest, digest.hex());
    assertTrue(digest.verifyMessage("Hello, world!".getBytes()));
    assertFalse(digest.verifyMessage("another message".getBytes()));
  }

  @Test
  void testSha256() {
    var function = HashFunctions.sha256();

    assertEquals("SHA-256", function.name());
    assertEquals(256, Byte.SIZE * function.digestLength());
    var hasher = function.init();
    assertInstanceOf(Sha256Hasher.class, hasher);
    assertEquals(HashFunctions.sha256(), hasher.algorithm());
  }
}
