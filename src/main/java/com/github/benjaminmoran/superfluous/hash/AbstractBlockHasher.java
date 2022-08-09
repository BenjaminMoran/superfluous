package com.github.benjaminmoran.superfluous.hash;

import java.nio.ByteBuffer;
import java.util.Objects;

abstract class AbstractBlockHasher extends AbstractHasher {
  protected final ByteBuffer blockBuf;
  protected long blocksProcessed;

  protected AbstractBlockHasher(int blockSize) {
    blockBuf = ByteBuffer.allocate(blockSize);
    blocksProcessed = 0L;
  }

  protected abstract void processBlock(ByteBuffer block);

  @Override
  public Hasher update(byte[] input, int offset, int length) {
    Objects.requireNonNull(input);
    Objects.checkFromIndexSize(offset, length, input.length);
    int inputOffset = offset;
    int end = offset + length;
    while (inputOffset < end) {
      int copyLength = Math.min(end - inputOffset, blockBuf.remaining());
      blockBuf.put(input, inputOffset, copyLength);
      inputOffset += copyLength;
      if (!blockBuf.hasRemaining()) {
        processBlock(blockBuf);
        blockBuf.clear();
        blocksProcessed++;
      }
    }
    return this;
  }
}
