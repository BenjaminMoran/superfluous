package com.github.benjaminmoran.superfluous.hash;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;

/**
 * Tests for known SHA-256 input-output pairs.
 */
class Sha256HasherTest {
  private static void testHash(String input, String expectedDigest) {
    testHash(input.getBytes(StandardCharsets.UTF_8), expectedDigest);
  }

  private static void testHash(byte[] input, String expectedDigest) {
    assertEquals(expectedDigest, HashFunctions.sha256().hash(input).hex());
  }

  private static void testRepeatedByte(byte inputByte, int inputLength, String expectedDigest) {
    var hasher = HashFunctions.sha256().init();
    var buf = new byte[64];
    Arrays.fill(buf, inputByte);
    int bufIterations = inputLength / buf.length;
    int remainder = inputLength % buf.length;
    for (int i = 0; i < bufIterations; i++) {
      hasher.update(buf);
    }
    hasher.update(buf, 0, remainder);
    assertEquals(expectedDigest, hasher.digest().hex());
  }

  /*
   * The tests prefixed nist are from
   * https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA256.pdf and
   * https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
   */

  @Test
  void testNistAbc() {
    testHash(
        "abc",
        "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
    );
  }

  @Test
  void testNistTwoBlocks() {
    testHash(
        "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
        "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1"
    );
  }

  @Test
  void testNistLength1() {
    testRepeatedByte(
        (byte) 0xbd, 1,
        "68325720aabd7c82f30f554b313d0570c95accbb7dc4b5aae11204c08ffe732b"
    );
  }

  @Test
  void testNistLength4() {
    testHash(
        HexFormat.of().parseHex("c98c8e55"),
        "7abc22c0ae5af26ce93dbb94433a0e0b2e119d014f8e7f65bd56c61ccccd9504"
    );
  }

  @Test
  void testNistZeros55() {
    testRepeatedByte(
        (byte) 0, 55,
        "02779466cdec163811d078815c633f21901413081449002f24aa3e80f0b88ef7"
    );
  }

  @Test
  void testNistZeros56() {
    testRepeatedByte(
        (byte) 0, 56,
        "d4817aa5497628e7c77e6b606107042bbba3130888c5f47a375e6179be789fbb"
    );
  }

  @Test
  void testNistZeros57() {
    testRepeatedByte(
        (byte) 0, 57,
        "65a16cb7861335d5ace3c60718b5052e44660726da4cd13bb745381b235a1785"
    );
  }

  @Test
  void testNistZeros64() {
    testRepeatedByte(
        (byte) 0, 64,
        "f5a5fd42d16a20302798ef6ed309979b43003d2320d9f0e8ea9831a92759fb4b"
    );
  }

  @Test
  void testNistZeros1000() {
    testRepeatedByte(
        (byte) 0, 1000,
        "541b3e9daa09b20bf85fa273e5cbd3e80185aa4ec298e765db87742b70138a53"
    );
  }

  @Test
  void testNistLength1000() {
    testRepeatedByte(
        (byte) 0x41, 1000,
        "c2e686823489ced2017f6059b8b239318b6364f6dcd835d0a519105a1eadd6e4"
    );
  }

  @Test
  void testNistLength1005() {
    testRepeatedByte(
        (byte) 0x55, 1005,
        "f4d62ddec0f3dd90ea1380fa16a5ff8dc4c54b21740650f24afc4120903552b0"
    );
  }

  @Test
  void testNistZeros1000000() {
    testRepeatedByte(
        (byte) 0, 1000000,
        "d29751f2649b32ff572b5e0a9f541ea660a50f94ff0beedfb0b692b924cc8025"
    );
  }

  // @Test
  // void testNistLength536870912() {
  //   testRepeatedByte(
  //       (byte) 0x5a, 536870912,
  //       "15a1868c12cc53951e182344277447cd0979536badcc512ad24c67e9b2d4f3dd"
  //   );
  // }
  //
  // @Test
  // void testNistZeros1090519040() {
  //   testRepeatedByte(
  //       (byte) 0, 1090519040,
  //       "461c19a93bd4344f9215f5ec64357090342bc66b15a148317d276e31cbc20b53"
  //   );
  // }
  //
  // @Test
  // void testNistLength1610612798() {
  //   testRepeatedByte(
  //       (byte) 0x42, 1610612798,
  //       "c23ce8a7895f4b21ec0daf37920ac0a262a220045a03eb2dfed48ef9b05aabea"
  //   );
  // }

  /*
   * Expected digests for these tests were computed with an online SHA-256 utility.
   */

  @Test
  void testEmpty() {
    testHash(
        "",
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    );
  }
}
