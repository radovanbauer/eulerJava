package euler;

import static com.google.common.base.Preconditions.checkState;

public class Problem567 {
  public static void main(String[] args) {
    Runner.run(new Problem567()::solve);
  }

  // JA1(n) = sum(1/k * c(n,k) * 1/2^k, {k, 1, n})

  public String solve() {
    long n = 123456789;
    double sum = 0;
    double fact = 1D;
    for (long i = 1; i <= n; i++) {
      if (i % 2 == 1) {
        fact *= (2*((i+1)/2) - 1) / ((i+1) / 2 * 2D);
      }
      double jb = jb(i);
      double ja = i <= 100 ? ja(i, fact) : jb;
      sum += ja + jb;
    }
    return String.format("%.8f", sum);
  }

  private double jb(long n) {
    double binom = 1;
    double jb = 1D/n;
    for (long k = 1; k <= n / 2; k++) {
      binom = binom * (n - k + 1) / k;
      double delta;
      if (k < n - k) {
        delta = 1D / (k * binom) + 1D / ((n - k) * binom);
      } else {
        checkState(k == n - k);
        delta = 1D / (k * binom);
      }
      if (delta < 1e-20) {
        break;
      }
      jb += delta;
    }
    return jb;
  }

  private double ja(long n, double initFact) {
    double ja = 1D/(n * Math.pow(2, n));
    long mid = (n + 1) / 2;
    double fact = initFact;
    for (long k = mid; k <= n - 1; k++) {
      if (k > mid) {
        fact = fact * (n - k + 1) / k;
      }
      double delta;
      if (k > n - k) {
        delta = fact / k + fact / (n - k);
      } else {
        checkState(k == n - k);
        delta = fact / k;
      }
      if (delta < 1e-20) {
        break;
      }
      ja += delta;
    }
    return ja;
  }
}
