package euler;

public class Problem381 {

  public static void main(String[] args) {
    System.out.println(new Problem381().solve());
  }

  public long solve() {
    int n = 100_000_000 - 1;
    PrimeSieve sieve = PrimeSieve.create(n);
    long sum = 0;
    for (int p = 5; p <= n; p++) {
      if (sieve.isPrime(p)) {
        sum += s(p);
      }
    }
    return sum;
  }

  private long s(long p) {
    LongMod p1 = LongMod.create(-1, p);
    LongMod p2 = p1.divide(-1);
    LongMod p3 = p2.divide(-2);
    LongMod p4 = p3.divide(-3);
    LongMod p5 = p4.divide(-4);
    return p1.add(p2).add(p3).add(p4).add(p5).n();
  }
}
