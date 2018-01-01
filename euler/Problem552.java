package euler;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public class Problem552 {

  public static void main(String[] args) {
    System.out.println(new Problem552().solve());
  }

  public long solve() {
    int max = 300_000;
    FactorizationSieve sieve = new FactorizationSieve(max);
    ImmutableList<Long> primes = sieve.getAllPrimes();
    BigInteger a = BigInteger.ZERO;
    BigInteger pMul = BigInteger.ONE;
    Set<Long> found = new HashSet<>();
    Set<Long> remaining = new HashSet<>(primes);
    for (int i = 0; i < primes.size(); i++) {
      System.out.println(i + "/" + primes.size());
      BigInteger p = BigInteger.valueOf(primes.get(i));
      a = a.multiply(p).multiply(BigMod.create(p, pMul).invert().n())
          .add(BigInteger.valueOf(i + 1).multiply(pMul).multiply(BigMod.create(pMul, p).invert().n()));
      pMul = pMul.multiply(p);
      a = a.mod(pMul);
      for (Iterator<Long> iter = remaining.iterator(); iter.hasNext();) {
        long r = iter.next();
        if (r > primes.get(i)) {
          if (a.mod(BigInteger.valueOf(r)).compareTo(BigInteger.ZERO) == 0) {
            found.add(r);
            iter.remove();
            System.out.println("Found " + r + ", remaining: " + remaining.size());
          }
        } else {
          iter.remove();
        }
      }
    }
    return found.stream().reduce(0L, Long::sum);
  }
}
