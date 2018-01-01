package euler;

import java.math.BigInteger;

public class Problem467 {

  public static void main(String[] args) {
    System.out.println(new Problem467().solve());
  }

  public long solve() {
    int n = 10_000;
    FactorizationSieve sieve = new FactorizationSieve(n * 100);
    int[] p = new int[n + 1];
    int[] c = new int[n + 1];
    int pidx = n;
    for (int pi = 2; pidx > 0; pi++) {
      if (sieve.isPrime(pi)) {
        p[pidx--] = digitalRoot(pi);
      }
    }
    int cidx = n;
    for (int ci = 2; cidx > 0; ci++) {
      if (!sieve.isPrime(ci)) {
        c[cidx--] = digitalRoot(ci);
      }
    }
    int[][] min = new int[n + 1][n + 1];
    Direction[][] dir = new Direction[n + 1][n + 1];
    min[0][0] = 0;
    for (int pi = 0; pi <= n; pi++) {
      for (int ci = 0; ci <= n; ci++) {
        if (pi > 0 && ci > 0 && p[pi] == c[ci]) {
          min[pi][ci] = min[pi - 1][ci - 1] + 1;
          dir[pi][ci] = Direction.PC;
        } else {
          if (pi > 0) {
            min[pi][ci] = min[pi - 1][ci] + 1;
            dir[pi][ci] = Direction.P;
          }
          if (ci > 0) {
            int val = min[pi][ci - 1] + 1;
            if (pi == 0 || (pi > 0 && (val < min[pi][ci] || (val == min[pi][ci] && c[ci] < p[pi])))) {
              min[pi][ci] = val;
              dir[pi][ci] = Direction.C;
            }
          }
        }
      }
    }
    char[] f = new char[min[n][n]];
    int fi = 0;
    int pi = n;
    int ci = n;
    while (pi > 0 || ci > 0) {
      switch (dir[pi][ci]) {
      case PC:
        f[fi++] = (char) ('0' + p[pi]);
        pi--;
        ci--;
        break;
      case P:
        f[fi++] = (char) ('0' + p[pi]);
        pi--;
        break;
      case C:
        f[fi++] = (char) ('0' + c[ci]);
        ci--;
        break;
      }
    }
    return new BigInteger(new String(f)).mod(BigInteger.valueOf(1_000_000_007)).longValueExact();
  }

  enum Direction {
    P, C, PC
  }

  private int digitalRoot(int n) {
    if (n < 10) {
      return n;
    } else {
      int sum = 0;
      while (n > 0) {
        sum += n % 10;
        n /= 10;
      }
      return digitalRoot(sum);
    }
  }
}
