package com.github.benjaminmoran.superfluous.hash;

import java.util.HexFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigestTest {
  private static final String digestHex =
      "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
  private static final byte[] digestBytes = HexFormat.of().parseHex(digestHex);
  /**
   * A manually-constructed SHA-256 digest of the empty string
   */
  private static final Digest digest = Digest.of(HashFunctions.sha256(), digestBytes);

  @Test
  void testAlgorithm() {
    assertEquals(HashFunctions.sha256(), digest.algorithm());
  }

  @Test
  void testBytes() {
    assertArrayEquals(digestBytes, digest.bytes());
  }

  @Test
  void testHex() {
    assertEquals(digestHex, digest.hex());
  }

  @Test
  void testVerifyMessage() {
    assertTrue(digest.verifyMessage(new byte[0]));
    assertFalse(digest.verifyMessage(new byte[1]));
  }

  @Test
  void testEquals() {
    var otherDigest = Digest.of(HashFunctions.sha256(), digestBytes.clone());
    assertEquals(digest, otherDigest);
  }

  @Test
  void testNotEqualsBytes() {
    var otherDigest = Digest.of(HashFunctions.sha256(), new byte[digestBytes.length]);
    assertNotEquals(digest, otherDigest);
  }

  @Test
  void testHashCode() {
    assertEquals(0xe3b0c442, digest.hashCode());
  }

  @Test
  void testToString() {
    assertEquals(digestHex, digest.toString());
  }

  @Test
  void testImmutable() {
    var bytesBefore = digest.bytes();
    digest.bytes()[0] ^= 0xff;
    var bytesAfter = digest.bytes();
    assertArrayEquals(bytesBefore, bytesAfter);
  }
}
