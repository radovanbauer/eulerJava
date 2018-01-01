package euler;

import com.google.common.base.Preconditions;

public class Problem473 {

  public static void main(String[] args) {
    System.out.println(new Problem473().solve());
  }

  public long solve() {
    long max = 1000;
    PhiPows phiPows = new PhiPows(44);
    long sum = 1;
    long maxI = 1L << ((int) (Math.log(max) / Math.log((1 + Math.sqrt(5)) / 2) + 0.5) + 1);
    for (long i = 0; i < maxI; i = next(i)) {
      if (i % 2 == 0) {
        SqrtNumber n = new PhigitalPalindrome(i, phiPows).sqrtNumber();
//        if (n.b().equals(LongFraction.ZERO) && n.a().n().compareTo(null) <= max && n.a().d() == 1L) {
//          sum += n.a().n();
//          System.out.println(n);
//        }
      }
    }
    return sum;
  }

  private long next(long n) {
    if (n == 0) {
      return 1;
    }
    switch (Long.numberOfTrailingZeros(n)) {
    case 0:
    case 1:
      return next(n >> 1) << 1;
    default:
      return n | 1;
    }
  }

  private static class PhigitalPalindrome {
    private final long num;
    private final PhiPows phiPows;

    public PhigitalPalindrome(long num, PhiPows phiPows) {
      this.num = num;
      this.phiPows = phiPows;
    }

    public SqrtNumber sqrtNumber() {
      SqrtNumber res = SqrtNumber.zero(5);
      for (int i = 0; i < 64; i++) {
        if ((num & (1L<<i)) != 0) {
          res = res.add(phiPows.phiPow(i)).add(phiPows.phiPow(-i - 1));
        }
      }
      return res;
    }
  }

  private static class PhiPows {
    private final int max;
    private final SqrtNumber[] phiPows;

    public PhiPows(int max) {
      this.max = max;
      SqrtNumber phi =
          SqrtNumber.create(BigFraction.create(1, 2), BigFraction.create(1, 2), BigFraction.create(5));
      phiPows = new SqrtNumber[2 * max + 1];
      phiPows[0] = phi.pow(-max);
      for (int i = -max + 1; i <= max; i++) {
        phiPows[i + max] = phiPows[i + max - 1].multiply(phi);
      }
    }

    public SqrtNumber phiPow(int exp) {
      Preconditions.checkArgument(exp <= max, "exp too big: %d", exp);
      return phiPows[exp + max];
    }
  }
}
