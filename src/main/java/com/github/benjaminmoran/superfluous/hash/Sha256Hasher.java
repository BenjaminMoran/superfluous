package com.github.benjaminmoran.superfluous.hash;

import static com.github.benjaminmoran.superfluous.hash.Operations.choose;
import static com.github.benjaminmoran.superfluous.hash.Operations.majority;
import static java.lang.Integer.rotateRight;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Instances of this class can be obtained from {@link HashFunctions#sha256()}.
 */
class Sha256Hasher extends AbstractBlockHasher {
  static final HashFunction HASH_FUNCTION =
      HashFunction.of("SHA-256", 32, Sha256Hasher::new);
  private static final int WORDS_PER_BLOCK = 16;
  private static final int BYTES_PER_BLOCK = Integer.BYTES * WORDS_PER_BLOCK;

  /**
   * Pseudo-random constants derived from the square roots of the first 8 prime numbers.
   */
  private static final int[] INITIAL_STATE = {
      0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19,
  };

  /**
   * Pseudo-random constants derived from the cube roots of the first 64 prime numbers.
   */
  private static final int[] K = {
      0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
      0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
      0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
      0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
      0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
      0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
      0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
      0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2,
  };

  private final int[] state;
  private final int[] scheduleBuf;
  private final int[] deltaBuf;

  private Sha256Hasher() {
    super(BYTES_PER_BLOCK);
    state = INITIAL_STATE.clone();
    scheduleBuf = new int[K.length];
    deltaBuf = new int[state.length];
  }

  @Override
  public HashFunction algorithm() {
    return HashFunctions.sha256();
  }

  @Override
  protected void processBlock(ByteBuffer block) {
    int[] schedule = buildSchedule(block);
    int[] delta = computeDelta(schedule);

    for (int i = 0; i < state.length; i++) {
      state[i] += delta[i];
    }
  }

  private int[] buildSchedule(ByteBuffer block) {
    block.rewind();
    for (int t = 0; t < WORDS_PER_BLOCK; t++) {
      scheduleBuf[t] = block.getInt();
    }
    for (int t = WORDS_PER_BLOCK; t < scheduleBuf.length; t++) {
      scheduleBuf[t] =
          s1(scheduleBuf[t - 2])
              + scheduleBuf[t - 7]
              + s0(scheduleBuf[t - 15])
              + scheduleBuf[t - 16];
    }
    return scheduleBuf;
  }

  private int[] computeDelta(int[] schedule) {
    // Initialize deltaBuf by copying in state
    System.arraycopy(state, 0, deltaBuf, 0, state.length);

    for (int t = 0; t < schedule.length; t++) {
      int t1 = schedule[t] + K[t]
          + deltaBuf[7]
          + S1(deltaBuf[4])
          + choose(deltaBuf[4], deltaBuf[5], deltaBuf[6]);
      int t2 = S0(deltaBuf[0]) + majority(deltaBuf[0], deltaBuf[1], deltaBuf[2]);

      deltaBuf[7] = deltaBuf[6];
      deltaBuf[6] = deltaBuf[5];
      deltaBuf[5] = deltaBuf[4];
      deltaBuf[4] = deltaBuf[3] + t1;
      deltaBuf[3] = deltaBuf[2];
      deltaBuf[2] = deltaBuf[1];
      deltaBuf[1] = deltaBuf[0];
      deltaBuf[0] = t1 + t2;
    }
    return deltaBuf;
  }

  @Override
  protected byte[] digestBytes() {
    padAndProcess();
    return intArrayToBytes(state);
  }

  /**
   * Processes the partial-block input remaining in {@code blockBuf}.
   */
  private void padAndProcess() {
    long messageLengthInBits =
        Byte.SIZE * (BYTES_PER_BLOCK * blocksProcessed + blockBuf.position());
    blockBuf.put((byte) 0x80);
    if (blockBuf.remaining() < Long.BYTES) {
      fillZeroTo(blockBuf, BYTES_PER_BLOCK);
      processBlock(blockBuf);
      blockBuf.clear();
    }
    fillZeroTo(blockBuf, BYTES_PER_BLOCK - Long.BYTES);
    blockBuf.putLong(messageLengthInBits);
    processBlock(blockBuf);
  }

  private static void fillZeroTo(ByteBuffer buf, int to) {
    Arrays.fill(buf.array(), buf.position(), to, (byte) 0x00);
    buf.position(to);
  }

  private static byte[] intArrayToBytes(int[] array) {
    ByteBuffer bytes = ByteBuffer.allocate(array.length * Integer.BYTES);
    for (int i : array) {
      bytes.putInt(i);
    }
    return bytes.array();
  }

  private static int S0(int x) {
    return rotateRight(x, 2) ^ rotateRight(x, 13) ^ rotateRight(x, 22);
  }

  private static int S1(int x) {
    return rotateRight(x, 6) ^ rotateRight(x, 11) ^ rotateRight(x, 25);
  }

  private static int s0(int x) {
    return rotateRight(x, 7) ^ rotateRight(x, 18) ^ (x >>> 3);
  }

  private static int s1(int x) {
    return rotateRight(x, 17) ^ rotateRight(x, 19) ^ (x >>> 10);
  }


  @Override
  public String toString() {
    return "Sha256Hasher{"
        + "state=" + Arrays.toString(state)
        + ", blocksProcessed=" + blocksProcessed
        + '}';
  }
}
