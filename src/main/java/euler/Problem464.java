package euler;

import com.google.auto.value.AutoValue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.math.IntMath.checkedMultiply;
import static com.google.common.math.IntMath.checkedSubtract;

public class Problem464 {
  public static void main(String[] args) {
    Runner.run(new Problem464()::solve);
  }

  public long solve() {
    int max = 20_000_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    int[] N = new int[max + 1];
    int[] P = new int[max + 1];
    QNode qTree = new QNode(checkedMultiply(-100, max), checkedMultiply(100, max), checkedMultiply(-100, max), checkedMultiply(100, max));
    long count = 0;
    qTree.add(0, 0);
    System.out.println("count=" + qTree.count(-1, 0, -1, 0));
    for (int n = 1; n <= max; n++) {
      if ((n & 0xFFFF) == 0) {
        System.out.println(n);
      }
      N[n] = N[n - 1];
      P[n] = P[n - 1];
      Factorization factorization = sieve.factorization(n);
      if (isSquareFree(factorization)) {
        if (factorization.getPrimes().size() % 2 == 0) {
          P[n]++;
        } else {
          N[n]++;
        }
      }
      int x1 = checkedSubtract(checkedMultiply(99, N[n]), checkedMultiply(100, P[n]));
      int x2 = checkedSubtract(checkedMultiply(100, N[n]), checkedMultiply(99, P[n]));
      checkState(x1 <= x2);
      long count2 = qTree.count(x1, qTree.maxx, qTree.miny, x2);
      count += count2;
      qTree.add(x1, x2);
//      System.out.println("Adding " + x1 + "/" + x2);
    }
//    for (int a = 1; a <= max; a++) {
//      for (int b = a; b <= max; b++) {
//        if (isGood(N[b] - N[a - 1], P[b] - P[a - 1])) {
//          System.out.printf("a=%d b=%d\n", a, b);
//        }
//      }
//    }
    return count;
  }

  @AutoValue
  static abstract class Pair {
    abstract int a();
    abstract int b();

    static Pair create(int a, int b) {
      return new AutoValue_Problem464_Pair(a, b);
    }
  }

  private boolean isGood(long N, long P) {
    return 99 * N <= 100 * P && 99 * P <= 100 * N;
  }

  private boolean isSquareFree(Factorization factorization) {
    for (long prime : factorization.getPrimes()) {
      if (factorization.getExponent(prime) >= 2) {
        return false;
      }
    }
    return true;
  }

  private static class QNode {
    private final int minx, maxx, miny, maxy, midx, midy;
    private int count;
    private QNode x1y1, x1y2, x2y1, x2y2;

    QNode(int minx, int maxx, int miny, int maxy) {
      checkArgument(minx < maxx && miny < maxy);
      this.minx = minx;
      this.maxx = maxx;
      this.midx = (minx + maxx) / 2;
      this.miny = miny;
      this.maxy = maxy;
      this.midy = (miny + maxy) / 2;
    }

    void add(int x, int y) {
      checkArgument(x >= minx && x < maxx && y >= miny && y < maxy);
      this.count++;
      if (minx == maxx - 1 && miny == maxy - 1) {
        return;
      }
      if (x < midx && y < midy) {
        if (x1y1 == null) {
          x1y1 = new QNode(minx, midx, miny, midy);
        }
        x1y1.add(x, y);
      } else if (x < midx) {
        if (x1y2 == null) {
          x1y2 = new QNode(minx, midx, midy, maxy);
        }
        x1y2.add(x, y);
      } else if (x >= midx && y < midy) {
        if (x2y1 == null) {
          x2y1 = new QNode(midx, maxx, miny, midy);
        }
        x2y1.add(x, y);
      } else {
        if (x2y2 == null) {
          x2y2 = new QNode(midx, maxx, midy, maxy);
        }
        x2y2.add(x, y);
      }
    }

    int count(int x1, int x2, int y1, int y2) {
//      System.out.printf("minx=%d maxx=%d miny=%d maxy=%d\n", minx, maxx, miny, maxy);
      if (x1 <= minx && x2 >= maxx - 1 && y1 <= miny && y2 >= maxy - 1) {
        return count;
      }
      int res = 0;
      if (x1y1 != null && x2 >= minx && y2 >= miny && x1 < midx && y1 < midy) {
        res += x1y1.count(x1, x2, y1, y2);
      }
      if (x1y2 != null && x2 >= minx && y2 >= midy && x1 < midx && y1 < maxy) {
        res += x1y2.count(x1, x2, y1, y2);
      }
      if (x2y1 != null && x2 >= midx && y2 >= miny && x1 < maxx && y1 < midy) {
        res += x2y1.count(x1, x2, y1, y2);
      }
      if (x2y2 != null && x2 >= midx && y2 >= midy && x1 < maxx && y1 < maxy) {
        res += x2y2.count(x1, x2, y1, y2);
      }
      return res;
    }
  }
}
