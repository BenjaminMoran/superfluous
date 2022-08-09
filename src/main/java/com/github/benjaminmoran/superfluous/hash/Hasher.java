package com.github.benjaminmoran.superfluous.hash;

import java.util.Objects;

/**
 * The mutable intermediate state of a hash algorithm.
 * <p>
 * Instances should be created with {@link HashFunction#init()}.
 * <p>
 * Suppose a hasher is created in its initial state, then the {@link #update(byte[]) update()}
 * methods are called any number of times. A subsequent invocation of {@link #digest()} returns the
 * same digest as {@code algorithm().hash(message)}, where {@code message} is the concatenation of
 * the inputs to {@code update()} in the order received.
 * <p>
 * The behavior of every method is undefined after {@code digest()} has been called once.
 * Implementations may throw an exception in this case.
 */
public interface Hasher {
  /**
   * Returns a hash function which generates hashers of the same type as this one.
   *
   * @return the hash function implemented by this hasher
   */
  HashFunction algorithm();

  /**
   * Appends {@code input} to the message to be hashed.
   * <p>
   * Changes the state of this hasher as if {@code input} were appended to the message received thus
   * far.
   * <p>
   * The input is not modified. This method can be chained.
   *
   * @param input bytes to append to the message
   * @return this hasher
   */
  default Hasher update(byte[] input) {
    return update(Objects.requireNonNull(input), 0, input.length);
  }

  /**
   * Appends a sub-array of {@code input} to the message to be hashed.
   * <p>
   * Changes the state of this hasher as if a sub-array of {@code input} were appended to the
   * message received thus far. The data is read from {@code input}, starting at {@code offset} and
   * extending for {@code length} bytes.
   * <p>
   * The input is not modified. This method can be chained.
   *
   * @param input  byte array containing the data to append to the message
   * @param offset the offset of the data in {@code input}
   * @param length the number of bytes in the data
   * @return this hasher
   */
  Hasher update(byte[] input, int offset, int length);

  /**
   * {@return the digest of the input received}
   * <p>
   * Returns the digest of the concatenation of the message fragments received so far.
   * <p>
   * Behavior is undefined if this method is called twice.
   */
  Digest digest();
}
