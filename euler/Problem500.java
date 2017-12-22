package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.common.math.LongMath;

public class Problem500 {

  public static void main(String[] args) {
    System.out.println(new Problem500().solve());
  }

  public long solve() {
    int max = 10_000_000;
    Primes primes = new Primes(max);
    List<PrimePower> primePowers = new ArrayList<>();
    for (int n = 2; n <= max; n++) {
      if (primes.isPrime(n)) {
        for (int exp = 1; LongMath.pow(n, exp) <= max; exp <<= 1) {
          primePowers.add(PrimePower.create(n, exp));
        }
      }
    }
    Collections.sort(primePowers);
    int mod = 500500507;
    return primePowers.stream().limit(500_500)
        .map(i -> LongMod.create(i.eval(), mod))
        .reduce(LongMod.create(1, mod), (i1, i2) -> i1.multiply(i2)).n();
  }

  @AutoValue
  static abstract class PrimePower implements Comparable<PrimePower> {
    public abstract long p();
    public abstract int exp();

    public long eval() {
      return LongMath.pow(p(), exp());
    }

    static PrimePower create(long p, int exp) {
      return new AutoValue_Problem500_PrimePower(p, exp);
    }

    @Override
    public int compareTo(PrimePower that) {
      return Long.compare(this.eval(), that.eval());
    }
  }


  private static class Primes {
    private final int max;
    private final boolean[] nonPrimes;

    public Primes(int max) {
      this.max = max;
      nonPrimes = new boolean[max + 1];
      for (int i = 2; i <= max; i++) {
        if (!nonPrimes[i]) {
          long j = 1L * i * i;
          while (j <= max) {
            if (!nonPrimes[(int) j]) {
              nonPrimes[(int) j] = true;
            }
            j += i;
          }
        }
      }
    }

    public boolean isPrime(int n) {
      checkArgument(n <= max);
      return !nonPrimes[n];
    }
  }
}
