package com.github.benjaminmoran.superfluous.hash;

import java.util.function.Supplier;

class HashFunctionImpl implements HashFunction {
  private final String name;
  private final int digestLength;
  private final Supplier<? extends Hasher> factory;

  HashFunctionImpl(String name, int digestLength, Supplier<? extends Hasher> factory) {
    this.name = name;
    this.digestLength = digestLength;
    this.factory = factory;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Hasher init() {
    return factory.get();
  }

  @Override
  public int digestLength() {
    return digestLength;
  }

  @Override
  public String toString() {
    return name();
  }
}
