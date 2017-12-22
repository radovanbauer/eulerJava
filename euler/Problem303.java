package euler;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import com.google.common.primitives.Ints;

import com.google.common.base.Stopwatch;

public class Problem303 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem303().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public BigInteger solve() {
    BigInteger sum = BigInteger.ZERO;
    for (int n = 1; n <= 10_000; n++) {
      BigInteger x = solve(n);
      sum = sum.add(x.divide(BigInteger.valueOf(n)));
    }
    return sum;
  }

  private BigInteger solve(int n) {
    if (n == 1) {
      return BigInteger.ONE;
    }
    if (n == 2) {
      return BigInteger.valueOf(2);
    }
    Deque<Integer> queue = new ArrayDeque<>();
    int[] parentEdge = new int[n];
    int[] parent = new int[n];
    Arrays.fill(parent, -1);
    Arrays.fill(parentEdge, -1);
    queue.add(0);
    while (!queue.isEmpty()) {
      int cur = queue.removeFirst();
      for (int d = 0; d <= 2; d++) {
        if (cur == 0 && d == 0) {
          continue;
        }
        int next = Ints.checkedCast((cur * 10 + d) % n);
        if (parent[next] == -1) {
          parent[next] = cur;
          parentEdge[next] = d;
          if (next == 0) {
            BigInteger res = BigInteger.valueOf(d);
            int x = cur;
            BigInteger tenPow = BigInteger.ONE;
            while (x != 0) {
              tenPow = tenPow.multiply(BigInteger.TEN);
              res = res.add(tenPow.multiply(BigInteger.valueOf(parentEdge[x])));
              x = parent[x];
            }
            return res;
          }
          queue.add(next);
        }
      }
    }
    throw new IllegalStateException();
  }
}
