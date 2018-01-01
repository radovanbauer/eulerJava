package euler;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class Problem325 {

  public static void main(String[] args) {
    System.out.println(new Problem325().solve());
  }

  public long solve() {
    BigInteger n = BigInteger.valueOf(10_000_000_000_000_000L);
    BigInteger b = g(n.add(ONE));
    return b.subtract(n).multiply(ONE.add(BigInteger.valueOf(2).multiply(b)).add(b.multiply(b))
            .subtract(n.multiply(n))).divide(BigInteger.valueOf(2))
        .add(sfk(b).multiply(BigInteger.valueOf(2)))
        .add(sf(b).add(sf2(b)).divide(BigInteger.valueOf(2)))
        .mod(BigInteger.valueOf(7).pow(10)).longValueExact();
  }

  private final BigDecimal goldenRatio =
      BigDecimal.ONE.add(BigDecimal.valueOf(Math.sqrt(5))).divide(BigDecimal.valueOf(2));

  private BigInteger g(BigInteger n) {
    BigInteger x = new BigDecimal(n).divide(goldenRatio, MathContext.DECIMAL128).toBigInteger();
    while (x.multiply(x).add(n.multiply(x)).subtract(n.multiply(n)).compareTo(ZERO) <= 0) {
      x = x.add(ONE);
    }
    return x.subtract(ONE);
  }

  private static final BigInteger[] fib = new BigInteger[90];
  static {
    fib[0] = ZERO;
    fib[1] = ONE;
    for (int i = 2; i < fib.length; i++) {
      fib[i] = fib[i - 2].add(fib[i - 1]);
    }
  }

  private int highestFibIndex(BigInteger n) {
    for (int i = 0;; i++) {
      if (fib[i + 1].compareTo(n) >= 0) {
        return i;
      }
    }
  }

  private final Map<BigInteger, BigInteger> sfCache = new HashMap<>();

  private BigInteger sf(BigInteger n) {
    if (n.compareTo(ONE) <= 0) {
      return BigInteger.ZERO;
    }
    if (sfCache.containsKey(n)) {
      return sfCache.get(n);
    }
    int k = highestFibIndex(n);
    BigInteger res = sf(fib[k])
        .add(sf(n.subtract(fib[k])))
        .add(n.subtract(fib[k]).multiply(fib[k - 1]));
    sfCache.put(n, res);
    return res;
  }

  private final Map<BigInteger, BigInteger> sf2Cache = new HashMap<>();

  private BigInteger sf2(BigInteger n) {
    if (n.compareTo(ONE) <= 0) {
      return BigInteger.ZERO;
    }
    if (sf2Cache.containsKey(n)) {
      return sf2Cache.get(n);
    }
    int k = highestFibIndex(n);
    BigInteger res = sf2(fib[k])
        .add(sf2(n.subtract(fib[k])))
        .add(fib[k - 1].multiply(fib[k - 1]).multiply(n.subtract(fib[k])))
        .add(BigInteger.valueOf(2).multiply(fib[k - 1]).multiply(sf(n.subtract(fib[k]))));
    sf2Cache.put(n, res);
    return res;
  }

  private final Map<BigInteger, BigInteger> sfkCache = new HashMap<>();

  private BigInteger sfk(BigInteger n) {
    if (n.compareTo(ONE) <= 0) {
      return BigInteger.ZERO;
    }
    if (sfkCache.containsKey(n)) {
      return sfkCache.get(n);
    }
    int k = highestFibIndex(n);
    BigInteger res = sfk(fib[k])
        .add(sfk(n.subtract(fib[k])))
        .add(fib[k].multiply(sf(n.subtract(fib[k]))))
        .add(fib[k - 1].multiply(n.subtract(fib[k])).multiply(ONE.add(n).add(fib[k]))
            .divide(BigInteger.valueOf(2)));
    sfkCache.put(n, res);
    return res;
  }
}
