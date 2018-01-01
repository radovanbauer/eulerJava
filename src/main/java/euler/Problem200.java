package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigInteger;
import java.util.List;
import java.util.TreeSet;

public class Problem200 {

  public static void main(String[] args) {
    System.out.println(new Problem200().solve());
  }

  public long solve() {
    int[] primes = primes(1000000);
    TreeSet<Sqube> squbes = Sets.newTreeSet();
    squbes.add(new Sqube(primes, 0, 1));
    squbes.add(new Sqube(primes, 1, 0));
    int cnt = 0;
    while (true) {
      Sqube sqube = squbes.first();
      squbes.remove(sqube);
      squbes.addAll(sqube.next());
      if (contains200(sqube.val) && isPrimeProof(sqube.val)) {
        cnt++;
        if (cnt == 200) {
          return sqube.val;
        }
      }
    }
  }

  private boolean contains200(long sqube) {
    return String.valueOf(sqube).contains("200");
  }

  private boolean isPrimeProof(long sqube) {
    char[] chars = String.valueOf(sqube).toCharArray();
    for (int i = 0; i < chars.length; i++) {
      for (int d = i == 0 ? 1 : 0; d <= 9; d++) {
        char[] newChars = chars.clone();
        newChars[i] = (char) ('0' + d);
        if (new BigInteger(new String(newChars)).isProbablePrime(10)) {
          return false;
        }
      }
    }
    return true;
  }

  private int[] primes(int n) {
    boolean[] sieve = new boolean[n];
    List<Integer> res = Lists.newArrayList();
    for (int d = 2; d < n; d++) {
      if (!sieve[d]) {
        res.add(d);
        long k = 1L * d * d;
        while (k < n) {
          sieve[(int) k] = true;
          k += d;
        }
      }
    }
    return Ints.toArray(res);
  }

  private static class Sqube implements Comparable<Sqube> {
    private final int[] primes;
    private final int pidx, qidx;
    private final long val;

    public Sqube(int[] primes, int pidx, int qidx) {
      this.primes = primes;
      this.pidx = pidx;
      this.qidx = qidx;
      this.val = 1L * primes[pidx] * primes[pidx]
          * primes[qidx] * primes[qidx] * primes[qidx];
    }

    public ImmutableList<Sqube> next() {
      return ImmutableList.of(
          new Sqube(primes, pidx + 1, qidx),
          new Sqube(primes, pidx, qidx + 1));
    }

    @Override
    public int compareTo(Sqube that) {
      return Longs.compare(this.val, that.val);
    }
  }
}
