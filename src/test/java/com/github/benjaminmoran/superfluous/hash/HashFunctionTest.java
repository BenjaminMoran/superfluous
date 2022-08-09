package com.github.benjaminmoran.superfluous.hash;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashFunctionTest {
  @Test
  void testName() {
    var function = HashFunctions.sha256();

    assertEquals("SHA-256", function.name());
  }

  @Test
  void testDigestLength() {
    var function = HashFunctions.sha256();

    assertEquals(32, function.digestLength());
  }

  @Test
  void testHash() {
    var function = HashFunctions.sha256();
    var input = "Hello, world!".getBytes();

    var inputBefore = input.clone();
    var digest = function.hash(input);

    assertEquals(function, digest.algorithm());
    assertEquals(function.digestLength(), digest.bytes().length);
    assertArrayEquals(inputBefore, input); // input not modified
    assertEquals(digest, function.hash(input)); // consistent
  }

  @Test
  void testHashRange() {
    var function = HashFunctions.sha256();
    var input = "Hello, world!".getBytes();
    var offset = 4;
    var length = 4;

    var inputBefore = input.clone();
    var digest = function.hash(input, offset, length);

    assertEquals(function, digest.algorithm());
    assertEquals(function.digestLength(), digest.bytes().length);
    assertArrayEquals(inputBefore, input); // input not modified
    assertEquals(digest, function.hash(input, offset, length)); // consistent

    // Equivalent to hash(byte[]) with sliced array
    var slicedCopy = Arrays.copyOfRange(input, offset, offset + length);
    assertEquals(digest, function.hash(slicedCopy));
  }

  @Test
  void testInit() {
    var function = HashFunctions.sha256();

    var hasher = function.init();

    assertEquals(function, hasher.algorithm());
  }

  @Test
  void testImmutable() {
    var function = HashFunctions.sha256();
    var input1 = "abc".getBytes();
    var input2 = "bcd".getBytes();

    function.hash(input1);

    // Later hashes are unaffected by earlier inputs
    assertEquals(
        "a6b0f90d2ac2b8d1f250c687301aef132049e9016df936680e81fa7bc7d81d70",
        function.hash(input2).hex()
    );
  }
}
