package euler;

import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.util.ArrayDeque;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.math.LongMath.checkedAdd;
import static com.google.common.math.LongMath.checkedMultiply;

public class Problem472 {
  public static void main(String[] args) {
    Runner.run(new Problem472()::solve);
  }

  public long solve() {
    BigInteger sum = BigInteger.valueOf(18);
    long seqNum = 1;
    long idx = 6;
    long max = LongMath.pow(10, 12);
    ArrayDeque<Seq> seqs = new ArrayDeque<>();
    while (idx < max) {
      if (seqs.isEmpty()) {
        for (long run = 1, runLength = 1; run <= seqNum; run++, runLength *= 2) {
          long from = 2;
          long to = runLength * 2;
          seqs.add(new Seq(from, runLength, 2));
          if (run < seqNum) {
            seqs.add(new Seq(2 * to + 2, 1, 0));
            seqs.add(new Seq(to, runLength - 1, -2));
          }
        }

        {
          long from = (1L << (seqNum - 1)) + 2;
          seqs.add(new Seq(3 * from - 3, 1, 0));
          seqs.add(new Seq(from, from - 2, -1));
          seqs.add(new Seq(8, 1, 0));
        }

        seqNum++;
      }

      Seq seq = seqs.pollFirst();
      long elems = Math.min(max - idx, seq.count());
      sum = sum.add(seq.sumFirst(elems));
      idx += elems;
    }
    return sum.mod(BigInteger.valueOf(10).pow(8)).longValueExact();
  }

  private static class Seq {
    private final long from, count, inc;

    Seq(long from, long count, long inc) {
      this.from = from;
      this.count = count;
      this.inc = inc;
    }

    long count() {
      return count;
    }

    BigInteger sumFirst(long n) {
      checkArgument(n <= count);
      long to = checkedAdd(from, checkedMultiply(n - 1, inc));
      return BigInteger.valueOf(from + to).multiply(BigInteger.valueOf(n)).divide(BigInteger.valueOf(2));
    }
  }
}
