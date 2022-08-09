package com.github.benjaminmoran.superfluous.hash;

abstract class AbstractHasher implements Hasher {
  @Override
  public Digest digest() {
    return Digest.of(algorithm(), digestBytes());
  }

  protected abstract byte[] digestBytes();
}
