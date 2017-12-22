package euler;

import com.google.common.math.LongMath;

public class Problem356 {
  public static void main(String[] args) {
    Runner.run(new Problem356()::solve);
  }

  public long solve() {
    long mod = LongMath.pow(10, 8);
    LongMod result = LongMod.zero(mod);
    for (int n = 1; n <= 30; n++) {
      LongMod e1 = LongMod.create(2, mod).pow(n);
      LongMod e2 = LongMod.zero(mod);
      LongMod e3 = LongMod.create(-n, mod);
      LongMod s1 = e1;
      LongMod s2 = s1.multiply(e1).subtract(e2.multiply(2));
      LongMod s3 = s2.multiply(e1).subtract(s1.multiply(e2)).add(e3.multiply(3));
      LongModMatrix2 mat = LongModMatrix2.create(new long[][]{{e1.n(), -e2.n(), e3.n()}, {1, 0, 0}, {0, 1, 0}}, mod);
      LongModMatrix2 snMat = mat.pow(987654321-3).multiply(LongModMatrix2.create(new long[][]{{s3.n()}, {s2.n()}, {s1.n()}}, mod));
      result = result.add(snMat.element(0, 0) - 1);
    }
    return result.n();
  }
}
