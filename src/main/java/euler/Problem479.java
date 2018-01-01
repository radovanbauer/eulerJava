package euler;

import java.util.stream.LongStream;


public class Problem479 {

  public static void main(String[] args) {
    System.out.println(new Problem479().solve());
  }

  public long solve() {
    int max = 1_000_000;
    long mod = 1_000_000_007;
    return LongStream.rangeClosed(1, max)
        .mapToObj(k -> sum(k, max, mod))
        .reduce(LongMod.create(0, mod), (i1, i2) -> i1.add(i2)).n();
  }

  private LongMod sum(long k, long max, long mod) {
    LongMod one = LongMod.create(1, mod);
    LongMod ksq = LongMod.create(k, mod).pow(2);
    return ksq.subtract(one).multiply(one.subtract(ksq).pow(max).subtract(one)).divide(ksq);
  }
}
