package com.github.benjaminmoran.superfluous.hash;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A deterministic mapping from messages to digests.
 * <p>
 * Messages are variable-length bit strings. Digests are bit strings of a fixed length dependent on
 * the particular function. This library only supports messages and digests of lengths divisible by
 * 8, represented by byte arrays. A byte array represents the concatenation, in order, of the
 * <em>big-endian</em> binary expansions of its bytes.
 * <p>
 * Implementors of this interface should be stateless.
 */
public interface HashFunction {
  /**
   * Creates a {@code HashFunction} instance from a {@link Hasher} constructor.
   *
   * @param name         a string identifying the hash function
   * @param digestLength the length of digests created (must be positive)
   * @param factory      creates {@code Hasher} instances in their initial state
   * @return a {@code HashFunction} which uses {@code Hasher}s returned by {@code factory}
   */
  static HashFunction of(String name, int digestLength, Supplier<Hasher> factory) {
    Objects.requireNonNull(name);
    if (digestLength <= 0) {
      throw new IllegalArgumentException("Digest length must be positive");
    }
    Objects.requireNonNull(factory);
    return new HashFunctionImpl(name, digestLength, factory);
  }

  /**
   * {@return a string identifying this hash function}
   */
  String name();

  /**
   * {@return the length <em>in bytes</em> of the digests output by this hash function}
   */
  int digestLength();

  /**
   * Applies this hash function to the given message and returns the digest computed.
   * <p>
   * This method is <i>consistent</i>: repeated invocations with the same input will yield the same
   * output.
   * <p>
   * The input is not modified. The digest returned has length {@link #digestLength()} and its
   * {@link Digest#algorithm()} is this hash function.
   *
   * @param input the bytes of the message to hash
   * @return the digest of the message
   */
  default Digest hash(byte[] input) {
    return hash(Objects.requireNonNull(input), 0, input.length);
  }

  /**
   * Applies this hash function to a sub-array of the input and returns the digest computed.
   * <p>
   * The message is read from {@code input}, starting at {@code offset} and extending for
   * {@code length} bytes.
   * <p>
   * This method is <i>consistent</i>: repeated invocations with the same input will yield the same
   * output.
   * <p>
   * The input is not modified. The digest returned has length {@link #digestLength()} and its
   * {@link Digest#algorithm()} is this hash function.
   *
   * @param input  the byte array containing the message to hash
   * @param offset the offset of the message in {@code input}
   * @param length the number of bytes in the message
   * @return the digest of the message
   */
  default Digest hash(byte[] input, int offset, int length) {
    Objects.requireNonNull(input);
    Objects.checkFromIndexSize(offset, length, input.length);
    return init().update(input, offset, length).digest();
  }

  /**
   * Allows incremental hashing by creating a stateful {@code Hasher} which can be updated with
   * parts of the message at a time.
   * <p>
   * The hasher returned has this hash function as its {@link Hasher#algorithm()}.
   *
   * @return a {@code Hasher} instance for this algorithm, in its initial state
   */
  Hasher init();
}
