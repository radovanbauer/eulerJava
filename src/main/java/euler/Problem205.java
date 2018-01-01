package euler;

import com.google.common.math.LongMath;

public class Problem205 {

  public static void main(String[] args) {
    System.out.println(new Problem205().solve());
  }

  public String solve() {
    long win = 0L;
    for (int p1 = 1; p1 <= 4; p1++)
      for (int p2 = 1; p2 <= 4; p2++)
        for (int p3 = 1; p3 <= 4; p3++)
          for (int p4 = 1; p4 <= 4; p4++)
            for (int p5 = 1; p5 <= 4; p5++)
              for (int p6 = 1; p6 <= 4; p6++)
                for (int p7 = 1; p7 <= 4; p7++)
                  for (int p8 = 1; p8 <= 4; p8++)
                    for (int p9 = 1; p9 <= 4; p9++)
                      for (int c1 = 1; c1 <= 6; c1++)
                        for (int c2 = 1; c2 <= 6; c2++)
                          for (int c3 = 1; c3 <= 6; c3++)
                            for (int c4 = 1; c4 <= 6; c4++)
                              for (int c5 = 1; c5 <= 6; c5++)
                                for (int c6 = 1; c6 <= 6; c6++)
                                  if (p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9 > c1 + c2 + c3 + c4 + c5 + c6)
                                    win++;
    long all = LongMath.pow(4, 9) * LongMath.pow(6, 6);
    return String.format("%.7f", 1D * win / all);
  }
}
