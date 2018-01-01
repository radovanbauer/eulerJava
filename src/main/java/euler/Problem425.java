package euler;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.collect.Lists;

public class Problem425 {

  public static void main(String[] args) {
    System.out.println(new Problem425().solve(10000000));
  }

  public long solve(int n) {
    boolean[] primes = primes(n + 1);
    boolean[] relative = new boolean[n + 1];
    boolean[] vis = new boolean[n + 1];
    long sum = 0L;
    PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
    queue.add(2);
    relative[2] = true;
    vis[2] = true;
    for (int p = 2; p <= n; p++) {
      if (primes[p]) {
        while (!queue.isEmpty() && queue.peek() <= p) {
          int q = queue.poll();
          relative[q] = true;
          for (int conn : connected(q)) {
            if (conn <= n && primes[conn] && !vis[conn]) {
              vis[conn] = true;
              queue.add(conn);
            }
          }
        }
        if (!relative[p]) {
          sum += p;
        }
      }
    }
    return sum;
  }

  private List<Integer> connected(int n) {
    char[] digits = String.valueOf(n).toCharArray();
    List<Integer> res = Lists.newArrayList();
    if (digits.length > 1 && digits[1] > '0') {
      res.add(Integer.valueOf(new String(digits, 1, digits.length - 1)));
    }
    for (int d = 1; d <= 9; d++) {
      char[] newDigits = new char[digits.length + 1];
      newDigits[0] = (char) ('0' + d);
      System.arraycopy(digits, 0, newDigits, 1, digits.length);
      res.add(Integer.valueOf(new String(newDigits)));
    }
    for (int i = 0; i < digits.length; i++) {
      for (int d = (i == 0) ? 1 : 0; d <= 9; d++) {
        if (digits[i] != d) {
          char[] newDigits = digits.clone();
          newDigits[i] = (char) ('0' + d);
          res.add(Integer.valueOf(new String(newDigits)));
        }
      }
    }
    return res;
  }

  private boolean[] primes(int n) {
    boolean[] sieve = new boolean[n];
    Arrays.fill(sieve, true);
    sieve[0] = false;
    sieve[1] = false;
    sieve[2] = true;
    for (int d = 2; d < n; d++) {
      if (sieve[d]) {
        for (long k = ((long) d) * d; k < n; k += d) {
          sieve[(int) k] = false;
        }
      }
    }
    return sieve;
  }
}
