package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.AbstractIterator;

import com.google.common.base.Stopwatch;

public class Problem418 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem418().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public BigInteger solve() {
    Number n = Number.create(
        new int[] {39, 19, 9, 6, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1},
        new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43});
    double min = Double.MAX_VALUE;
    BigInteger minSum = null;
    for (Number a : n.divisors()) {
      if (!(a.log() <= n.log() / 3 && a.log() > (n.log() - 2 * min) / 3)) {
        continue;
      }
      for (Number c : n.divide(a).divisors()) {
        Number b = n.divide(a).divide(c);
        if (a.log() <= b.log() && b.log() <= c.log()) {
          double ratio = c.log() - a.log();
          if (ratio >= 0 && ratio < min) {
            min = ratio;
            minSum = a.bigIntegerValue().add(b.bigIntegerValue()).add(c.bigIntegerValue());
          }
        }
      }
    };
    return minSum;
  }

  static class Number {
    private final int[] exp;
    private final int[] p;
    private final double[] logp;
    private final double log;

    static Number create(int[] exp, int[] primes) {
      double[] logp = new double[primes.length];
      for (int i = 0; i < primes.length; i++) {
        logp[i] = Math.log(primes[i]);
      }
      return new Number(exp, primes, logp);
    }

    Number(int[] exp, int[] p, double[] logp) {
      double log = 0;
      checkArgument(exp.length == p.length && exp.length == logp.length);
      for (int i = 0; i < exp.length; i++) {
        checkArgument(exp[i] >= 0);
        checkArgument(p[i] >= 2);
        log += exp[i] * logp[i];
      }
      this.exp = exp;
      this.p = p;
      this.logp = logp;
      this.log = log;
    }

    double log() {
      return log;
    }

    Number divide(Number that) {
      int[] newExp = new int[exp.length];
      for (int i = 0; i < exp.length; i++) {
        newExp[i] = this.exp[i] - that.exp[i];
      }
      return new Number(newExp, p, logp);
    }

    public BigInteger bigIntegerValue() {
      BigInteger res = BigInteger.ONE;
      for (int i = 0; i < p.length; i++) {
        for (int j = 0; j < exp[i]; j++) {
          res = res.multiply(BigInteger.valueOf(p[i]));
        }
      }
      return res;
    }

    @Override
    public String toString() {
      return bigIntegerValue().toString();
    }

    Iterable<Number> divisors() {
      return new Iterable<Number>() {
        @Override
        public Iterator<Number> iterator() {
          return new AbstractIterator<Number>() {
            final int[] cur = exp.clone();
            boolean finished = false;

            @Override
            protected Number computeNext() {
              if (finished) {
                return endOfData();
              }
              Number div = new Number(cur.clone(), p, logp);
              int idx = 0;
              while (idx < cur.length && cur[idx] == 0) {
                cur[idx] = exp[idx];
                idx++;
              }
              if (idx >= cur.length) {
                finished = true;
              } else {
                cur[idx]--;
              }
              return div;
            }
          };
        }
      };
    }
  }
}
