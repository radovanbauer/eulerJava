package euler;

public class Problem409 {

  public static void main(String[] args) {
    Runner.run(new Problem409()::solve);
  }

  // 1, 6, 168, 30240, 19764360, 48159573840

  public long solve() {
    long mod = 1_000_000_007;
    int n = 10_000_000;
    long[] z = new long[n + 1];
    LongMod twoPowN = LongMod.create(2, mod).pow(n);
    LongMod pos = twoPowN.subtract(1);
    LongMod all = pos;
    pos = pos.subtract(1);
    all = all.multiply(pos);
    for (int i = 3; i <= n; i++) {
      z[i] = all.subtract(z[i - 1])
          .subtract(LongMod.create(i - 1, mod).multiply(z[i - 2]).multiply(twoPowN.subtract(i - 1))).n();
      pos = pos.subtract(1);
      all = all.multiply(pos);
    }
    return all.subtract(z[n]).n();
  }
}
