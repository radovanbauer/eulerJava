package euler;

public class Problem160 {

  private static final int MAX = 100000;

  public static void main(String[] args) {
    System.out.println(new Problem160().solve(1000000000000L));
  }

  public long solve(long n) {
    long[] count = new long[MAX + 1];
    for (long a = 1; a <= n; a *= 2) {
      for (long b = 1; a * b <= n; b *= 5) {
        long base = a * b;
        long div = n / base / MAX;
        long rem = n / base % MAX;
        for (int i = 0; i < MAX; i++) {
          if (i % 2 == 0 || i % 5 == 0) {
            continue;
          }
          count[i] += div;
          if (i <= rem) {
            count[i]++;
          }
        }
      }
    }
    long count2 = 0L;
    long count5 = 0L;
    for (long a = 2; a <= n; a *= 2) {
      count2 += n / a;
    }
    for (long b = 5; b <= n; b *= 5) {
      count5 += n / b;
    }
    long prod = 1L;
    for (int i = 0; i < MAX; i++) {
      prod = (prod * modPow(i, count[i], MAX)) % MAX;
    }
    prod = (prod * modPow(2, count2 - count5, MAX)) % MAX;
    return prod;
  }

  private long modPow(long base, long exp, long mod) {
    if (exp == 0) {
      return 1;
    } else if (exp % 2 == 1) {
      return (base * modPow(base, exp - 1, mod)) % mod;
    } else {
      long x = modPow(base, exp / 2, mod);
      return (x * x) % mod;
    }
  }
}
