package com.github.benjaminmoran.superfluous.hash;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

/**
 * An immutable wrapper for a byte array, tagged with the hash function which created it.
 */
public final class Digest {
  private final HashFunction algorithm;
  private final byte[] bytes;

  private Digest(HashFunction algorithm, byte[] bytes) {
    this.algorithm = algorithm;
    this.bytes = bytes.clone();
  }

  /**
   * Tags a byte array with the hash function which created it.
   *
   * @param algorithm the hash function which output {@code bytes}
   * @param bytes     the bytes of the digest
   * @return a {@code Digest} containing the arguments
   */
  public static Digest of(HashFunction algorithm, byte[] bytes) {
    return new Digest(Objects.requireNonNull(algorithm), Objects.requireNonNull(bytes));
  }

  /**
   * {@return the hash function which created this digest}
   */
  public HashFunction algorithm() {
    return algorithm;
  }

  /**
   * {@return the bytes of this digest}
   */
  public byte[] bytes() {
    return bytes.clone();
  }

  /**
   * {@return a hexadecimal representation of this digest}
   */
  public String hex() {
    return HexFormat.of().formatHex(bytes());
  }

  /**
   * Tests whether a message matches this hash digest.
   * <p>
   * Returns true if and only if {@code algorithm()} hashes {@code input} to the same bytes as are
   * contained in this digest object.
   *
   * @param input the message to verify
   * @return true if {@code input} matches; false otherwise
   */
  public boolean verifyMessage(byte[] input) {
    Digest otherDigest = algorithm().hash(Objects.requireNonNull(input));
    return Arrays.equals(bytes(), otherDigest.bytes());
  }

  /**
   * Compares this object to the specified object. The result is true if and only if the argument is
   * a non-null {@code Digest} containing the same bytes. The algorithm field is not compared.
   *
   * @param obj the object to compare with this digest
   * @return true if {@code obj} is equivalent to this digest; false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Digest other = (Digest) obj;
    return Arrays.equals(bytes, other.bytes);
  }

  /**
   * {@return the four most significant bytes of this digest as a big-endian {@code int}}
   */
  @Override
  public int hashCode() {
    return (ByteBuffer.wrap(bytes)).getInt();
  }

  /**
   * {@return a hexadecimal representation of this digest}
   */
  @Override
  public String toString() {
    return hex();
  }
}
