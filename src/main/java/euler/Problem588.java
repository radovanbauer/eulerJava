package euler;

import com.google.auto.value.AutoValue;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.LongMath;

import java.util.HashSet;
import java.util.regex.Pattern;

import static euler.Runner.run;

public class Problem588 {

  public static void main(String[] args) {
    run(new Problem588()::solve);
  }

  public long solve() {
    Poly2 p = Poly2.create(ImmutableList.of(0L, 1L, 2L, 3L, 4L));
    long sum = 0L;
    Poly2 q = p;
    for (int k = 1; k <= 18; k++) {
      long pow = LongMath.pow(10, k);
      String binary = Long.toString(pow, 2);
      Iterable<String> splits = Splitter.on(Pattern.compile("000*")).omitEmptyStrings().split(binary);
      long count = 1L;
      for (String split : splits) {
        long splitLong = Long.parseLong(split, 2);
        count *= p.pow(splitLong).coefs().size();
      }
      sum += count;
      System.out.println(pow + ": " + count);
    }
    return sum;
  }



  @AutoValue
  static abstract class Poly2 {
    abstract ImmutableSet<Long> coefs();

    static Poly2 create(Iterable<Long> coefs) {
      return new AutoValue_Problem588_Poly2(ImmutableSet.copyOf(coefs));
    }

    Poly2 multiply(Poly2 that) {
      HashSet<Long> result = new HashSet<>();
      for (long c1 : this.coefs()) {
        for (long c2 : that.coefs()) {
          long c = LongMath.checkedAdd(c1, c2);
          if (!result.contains(c)) {
            result.add(c);
          } else {
            result.remove(c);
          }
        }
      }
      return create(result);
    }

    Poly2 pow(long exp) {
      if (exp == 1) {
        return this;
      }
      Poly2 x = pow(exp / 2);
      if (exp % 2 == 0) {
        return x.multiply(x);
      } else {
        return x.multiply(x).multiply(this);
      }
    }
  }
}
