package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.FLOOR;

import com.google.common.math.LongMath;

public class Problem210 {

  public static void main(String[] args) {
    System.out.println(new Problem210().solve(1000000000L));
  }

  public long solve(long r) {
    checkArgument(r % 8 == 0);
    return r * r * 3 / 2 + inCircle(r * r / 32) - r / 4 + 1;
  }

  private long inCircle(long rSquare) {
    long rCeiling = LongMath.sqrt(rSquare, CEILING);
    long rFloor = LongMath.sqrt(rSquare, FLOOR);
    long cnt = 4 * rCeiling - 3;
    for (int i = 1; i <= rFloor; i++) {
      cnt += 4 * (LongMath.sqrt(rSquare - 1L * i * i, CEILING) - 1);
    }
    return cnt;
  }
}
