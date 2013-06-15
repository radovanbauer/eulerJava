package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.lang.Math.log;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

public class Problem183 {

  public static void main(String[] args) {
    System.out.println(new Problem183().solve(5, 10000));
  }

  public long solve(int a, int b) {
    long sum = 0L;
    for (int n = a; n <= b; n++) {
      int guess1 = Math.max(1, (int) Math.floor(n / Math.E));
      int guess2 = guess1 + 1;
      int k = guess1 * (log(n) - log(guess1)) > guess2 * (log(n) - log(guess2)) ? guess1 : guess2;
      if (isNonTerminating(n, k)) {
        sum += n;
      } else {
        sum -= n;
      }
    }
    return sum;
  }

  private boolean isNonTerminating(int n, int k) {
    int rem = n % k;
    boolean[] rems = new boolean[k];
    rems[rem] = true;
    while (rem != 0) {
      rem = (rem * 10) % k;
      if (rems[rem]) {
        return true;
      }
      rems[rem] = true;
    }
    return false;
  }
}
