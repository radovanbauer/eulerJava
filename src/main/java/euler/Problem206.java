package euler;

import static java.math.RoundingMode.FLOOR;

import com.google.common.math.LongMath;

public class Problem206 {

  public static void main(String[] args) {
    System.out.println(new Problem206().solve());
  }

  public long solve() {
    long base = 1020304050607080900L;
    for (int d1 = 0; d1 <= 9; d1++)
      for (int d2 = 0; d2 <= 9; d2++)
        for (int d3 = 0; d3 <= 9; d3++)
          for (int d4 = 0; d4 <= 9; d4++)
            for (int d5 = 0; d5 <= 9; d5++)
              for (int d6 = 0; d6 <= 9; d6++)
                for (int d7 = 0; d7 <= 9; d7++)
                  for (int d8 = 0; d8 <= 9; d8++)
                    for (int d9 = 0; d9 <= 9; d9++) {
                      long num = base
                          + d1 * 10L
                          + d2 * 1000L
                          + d3 * 100000L
                          + d4 * 10000000L
                          + d5 * 1000000000L
                          + d6 * 100000000000L
                          + d7 * 10000000000000L
                          + d8 * 1000000000000000L
                          + d9 * 100000000000000000L;
                      long sqrtFloor = LongMath.sqrt(num, FLOOR);
                      if (sqrtFloor * sqrtFloor == num) {
                        return sqrtFloor;
                      }
                    }
    throw new IllegalStateException();
  }
}
