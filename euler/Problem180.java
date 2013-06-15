package euler;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;

import java.math.BigInteger;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.math.LongMath;

public class Problem180 {

  public static void main(String[] args) {
    System.out.println(new Problem180().solve(35));
  }

  public BigInteger solve(int maxK) {
    Set<LongFrac> tripleSums = Sets.newHashSet();
    for (long x1 = 1; x1 < maxK; x1++) {
      for (long x2 = x1 + 1; x2 <= maxK; x2++) {
        if (LongMath.gcd(x1, x2) != 1) continue;
        System.out.println(String.format("%d %d", x1, x2));
        for (long y1 = 1; y1 < maxK; y1++) {
          for (long y2 = y1 + 1; y2 <= maxK; y2++) {
            if (LongMath.gcd(y1, y2) != 1) continue;
            for (long z1 = 1; z1 < maxK; z1++) {
              for (long z2 = z1 + 1; z2 <= maxK; z2++) {
                if (LongMath.gcd(z1, z2) != 1) continue;
                LongFrac x = new LongFrac(x1, x2);
                LongFrac y = new LongFrac(y1, y2);
                LongFrac z = new LongFrac(z1, z2);
                for (int n = -2; n <= 2; n++) {
                  if (x.pow(n).plus(y.pow(n)).equals(z.pow(n))) {
                    tripleSums.add(x.plus(y).plus(z));
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
    BigInteger n = BigInteger.ZERO;
    BigInteger d = BigInteger.ONE;
    for (LongFrac frac : tripleSums) {
      n = n.multiply(BigInteger.valueOf(frac.d)).add(BigInteger.valueOf(frac.n).multiply(d));
      d = d.multiply(BigInteger.valueOf(frac.d));
      BigInteger g = gcd(n, d);
      n = n.divide(g);
      d = d.divide(g);
    }
    System.out.println(String.format("%d %d", n, d));
    return n.add(d);
  }

  private BigInteger gcd(BigInteger a, BigInteger b) {
    return b.equals(BigInteger.ZERO) ? a : gcd(b, a.mod(b));
  }

  private static class LongFrac {
    private static final LongFrac ONE = new LongFrac(1, 1);

    private final long n, d;

    public LongFrac(long n, long d) {
      checkArgument(d != 0, "Invalid fraction: %d/%d", n, d);
      long g = LongMath.gcd(abs(n), abs(d)) * Long.signum(d);
      this.n = n / g;
      this.d = d / g;
    }

    public LongFrac plus(LongFrac that) {
      return new LongFrac(this.n * that.d + that.n * this.d, this.d * that.d);
    }

    public LongFrac pow(int k) {
      if (k > 0) {
        return new LongFrac(LongMath.pow(this.n, k), LongMath.pow(this.d, k));
      } else if (k < 0) {
        return new LongFrac(LongMath.pow(this.d, -k), LongMath.pow(this.n, -k));
      } else {
        return ONE;
      }
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(n, d);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof LongFrac) {
        LongFrac that = (LongFrac) obj;
        return this.n == that.n && this.d == that.d;
      }
      return false;
    }

    @Override
    public String toString() {
      return String.format("%d/%d", n, d);
    }
  }
}
