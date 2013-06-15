package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.math.RoundingMode.FLOOR;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;

public class Problem192 {

  public static void main(String[] args) {
    System.out.println(new Problem192().solve(100000, 1000000000000L));
  }

  public long solve(int n, long maxDenom) {
    long sum = 0L;
    for (int i = 2; i <= n; i++) {
      if (!isSquare(i)) {
        sum += bestApprox(i, maxDenom).d.longValue();
      }
    }
    return sum;
  }

  private BIFrac bestApprox(int n, long maxDenom) {
    Iterator<Integer> sqrtCFIter = sqrtCF(n);
    List<Integer> cf = Lists.newArrayList(sqrtCFIter.next());
    BIFrac last = BIFrac.ZERO;
    while (true) {
      int next = sqrtCFIter.next();
      cf.add(0);
      for (int d = (next + 1) / 2; d <= next; d++) {
        cf.set(cf.size() - 1, d);
        BIFrac approx = evalCF(cf);
        if (next % 2 == 0 && d == next / 2 && !isBetterApprox(n, approx, last)) {
          continue;
        }
        if (approx.d.longValue() > maxDenom) {
          return last;
        }
        checkState(isBetterApprox(n, approx, last));
        last = approx;
      }
    }
  }

  private boolean isBetterApprox(int n, BIFrac a, BIFrac b) {
    int cmp = a.compareTo(b);
    if (cmp > 0) {
      return new BIFrac(4 * n, 1).compareTo(a.plus(b).square()) > 0;
    } else if (cmp < 0) {
      return new BIFrac(4 * n, 1).compareTo(a.plus(b).square()) < 0;
    } else {
      return false;
    }
  }

  private BIFrac evalCF(List<Integer> cf) {
    BIFrac res = new BIFrac(cf.get(cf.size() - 1), 1L);
    for (int i = cf.size() - 2; i >= 0; i--) {
      res = res.invert().plus(new BIFrac(cf.get(i), 1L));
    }
    return res;
  }

  private Iterator<Integer> sqrtCF(final int n) {
    final int s = IntMath.sqrt(n, FLOOR);
    return new AbstractIterator<Integer>() {
      private int m = 0;
      private int d = 1;
      private int a = s;

      @Override
      protected Integer computeNext() {
        int res = a;
        m = d * a - m;
        d = (n - m * m) / d;
        a = (s + m) / d;
        return res;
      }
    };
  }

  private boolean isSquare(int n) {
    int s = IntMath.sqrt(n, FLOOR);
    return s * s == n;
  }

  private static class BIFrac implements Comparable<BIFrac> {
    private static final BIFrac ZERO = new BIFrac(0, 1);

    private final BigInteger n, d;

    public BIFrac(long n, long d) {
      this(BigInteger.valueOf(n), BigInteger.valueOf(d));
    }

    public BIFrac(BigInteger n, BigInteger d) {
      checkArgument(d.compareTo(BigInteger.ZERO) != 0, "Invalid fraction: %d/%d", n, d);
      BigInteger g = gcd(n.abs(), d.abs());
      if (d.compareTo(BigInteger.ZERO) < 0) {
        g.negate();
      }
      this.n = n.divide(g);
      this.d = d.divide(g);
    }

    private BigInteger gcd(BigInteger a, BigInteger b) {
      return b.equals(BigInteger.ZERO) ? a : gcd(b, a.mod(b));
    }

    public BIFrac plus(BIFrac that) {
      return new BIFrac(
          this.n.multiply(that.d).add(that.n.multiply(this.d)),
          this.d.multiply(that.d));
    }

    public BIFrac invert() {
      return new BIFrac(d, n);
    }

    public BIFrac square() {
      return new BIFrac(n.multiply(n), d.multiply(d));
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(n, d);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof BIFrac) {
        BIFrac that = (BIFrac) obj;
        return this.n == that.n && this.d == that.d;
      }
      return false;
    }

    @Override
    public int compareTo(BIFrac that) {
      return this.n.multiply(that.d).compareTo(that.n.multiply(this.d));
    }

    @Override
    public String toString() {
      return String.format("%d/%d", n, d);
    }
  }
}
