package euler;

public class Problem506 {

  public static void main(String[] args) {
    System.out.println(new Problem506().solve());
  }

  public long solve() {
    long n = 100_000_000_000_000L;
    long mod = 123454321;
    LongMod sum = LongMod.zero(mod);
    long[] seq = new long[] {
        0, 1, 2, 3, 4,
        32, 123, 43, 2123, 432,
        1234, 32123, 43212, 34321, 23432};
    long[] suffix = new long[] {
        123432, 234321, 343212, 432123, 321234,
        123432, 432123, 212343, 432123, 123432,
        321234, 432123, 343212, 234321, 123432};
    for (int r = 0; r < 15; r++) {
      if (n >= r) {
        LongModMatrix mat = LongModMatrix
            .create(new long[][] {{1, 1, 0}, {0, 1_000_000, suffix[r]}, {0, 0, 1}}, mod)
            .pow((n - r) / 15 + 1)
            .multiply(LongModMatrix.create(new long[][] {{0}, {seq[r]}, {1}}, mod));
        sum = sum.add(mat.element(0, 0));
      }
    }
    return sum.n();
  }
}
