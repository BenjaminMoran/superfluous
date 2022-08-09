package com.github.benjaminmoran.superfluous.hash;

/**
 * Bitwise and arithmetic operations commonly used in hashing algorithms.
 */
final class Operations {
  private Operations() {
  }

  static int choose(int x, int y, int z) {
    return (x & y) ^ (~x & z);
  }

  static int parity(int x, int y, int z) {
    return x ^ y ^ z;
  }

  static int majority(int x, int y, int z) {
    return (x & y) ^ (x & z) ^ (y & z);
  }
}
