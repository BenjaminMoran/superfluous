package com.github.benjaminmoran.superfluous.hash;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {
  @Test
  void testAlgorithm() {
    var function = HashFunctions.sha256();

    var hasher = function.init();

    assertSame(hasher.getClass(), hasher.algorithm().init().getClass());
  }

  @Test
  void testUpdate() {
    var function = HashFunctions.sha256();
    var input = "Hello, world!".getBytes();

    var inputBefore = input.clone();
    var hasher = function.init();
    var hasherReturned = hasher.update(input);

    assertSame(hasher, hasherReturned);
    assertArrayEquals(inputBefore, input);
  }

  @Test
  void testUpdateRange() {
    var function = HashFunctions.sha256();
    var input = "Hello, world!".getBytes();
    var offset = 4;
    var length = 4;

    var inputBefore = input.clone();
    var hasher = function.init();
    var hasherReturned = hasher.update(input, offset, length);

    assertSame(hasher, hasherReturned);
    assertArrayEquals(inputBefore, input);

    // Equivalence with update(byte[])
    var slicedCopy = Arrays.copyOfRange(input, offset, offset + length);
    Digest digestOfSlice = function.init().update(slicedCopy).digest();
    assertEquals(digestOfSlice, hasher.digest());
  }

  /**
   * Tests the contract described in the third paragraph of the doc comment on the {@code Hasher}
   * class.
   */
  @Test
  void testDigest() {
    var function = HashFunctions.sha256();
    var input = "Hello, world!".getBytes();

    var hasher = function.init();
    hasher.update(input, 0, 5);
    hasher.update(input, 5, 8);

    assertEquals(function.hash(input), hasher.digest());
  }
}
