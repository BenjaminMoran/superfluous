package com.github.benjaminmoran.superfluous.hash;

/**
 * Standard hash functions.
 * <p>
 * The methods in this class return {@link HashFunction} instances which implement popular
 * cryptographic hash functions.
 * <p>
 * Basic usage:
 * <pre>{@code
 * Digest digest = HashFunctions.sha256().hash("Hello, world!".getBytes());
 * String expectedDigest = "315f5bdb76d078c43b8ac0064e4a0164612b1fce77c869345bfc94c75894edd3";
 * assertEquals(expectedDigest, digest.hex());
 * assertTrue(digest.verify("Hello, world!".getBytes()));
 * assertFalse(digest.verify("another message".getBytes()));
 * }</pre>
 */
public final class HashFunctions {
  private HashFunctions() {
  }

  /**
   * {@return the SHA-256 hash function}
   * <p>
   * Defined in <a href="https://csrc.nist.gov/publications/detail/fips/180/4/final">FIPS 180-4,
   * <i>Secure Hash Standard (SHS)</i></a> by NIST, 2015 (originally defined in FIPS 180-2, 2002).
   * <p>
   * Digest size: 256 bits
   */
  public static HashFunction sha256() {
    return Sha256Hasher.HASH_FUNCTION;
  }
}
