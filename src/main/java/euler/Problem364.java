package euler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class Problem364 {
  public static void main(String[] args) {
    Runner.run(new Problem364()::solve);
  }

  // 2*w2 + w1 + b = in
  // w2 + w1 = b + 1
  // w1 = b + 1 - w2
  // 2*w2 + b + 1 - w2 + b = in
  // w2 + 2*b + 1 = in
  // w2 = in - 2*b - 1
  // b = (in - w2 - 1)/2
  //
  // w1 = (in - 3*w2 + 1)/2
  //
  // c(w1+w2,w2) * (b+2)! * (w2+extraW)! * 2^w2 * (w1+w2)!

  public long solve() {
    int n = 1_000_000;
    long mod = 100_000_007;
    return innerCount(n - 2, 0, mod)
        .add(innerCount(n - 3, 1, mod).multiply(2))
        .add(innerCount(n - 4, 2, mod)).n();
  }

  private LongMod innerCount(int in, int extraW, long mod) {
    checkArgument(in >= 0);
    LongMod res = LongMod.zero(mod);

    LongMod[] fact = new LongMod[in + 2];
    LongMod[] ifact = new LongMod[in + 2];
    fact[0] = LongMod.create(1, mod);
    for (int i = 1; i < fact.length; i++) {
      fact[i] = fact[i - 1].multiply(i);
    }
    ifact[ifact.length - 1] = fact[ifact.length - 1].invert();
    for (int i = ifact.length - 2; i >= 0; i--) {
      ifact[i] = ifact[i + 1].multiply(i + 1);
    }

    int firstW2 = (in % 2 == 0) ? 1 : 0;
    for (int w2 = firstW2; in - w2 - 1 >= 0 && in - 3*w2 + 1 >= 0; w2 += 2) {
      int b = (in - w2 - 1) / 2;
      checkState(w2 >= 0);
      int w1 = (in - 3*w2 + 1) / 2;
      checkState(w1 >= 0);

      // c(w1+w2,w2) * (b+2)! * (w2+extraW)! * 2^w2 * (w1+w2)!
      LongMod x = fact[w1 + w2].multiply(ifact[w2]).multiply(ifact[w1])
          .multiply(fact[b + 2])
          .multiply(fact[w2 + extraW])
          .multiply(LongMod.create(2, mod).pow(w2))
          .multiply(fact[w1 + w2]);
//      System.out.printf("in=%d w1=%d w2=%d b=%d -> %d\n", in, w1, w2, b, r.n());
      res = res.add(x);
    }
    return res;
  }
//
//  private LongMod innerCount2(long in, long extraW, long mod) {
//    checkArgument(in >= 0);
//    LongMod res = LongMod.zero(mod);
//    long firstW2 = (in % 2 == 0) ? 1 : 0;
//    long firstW1 = (in - 3*firstW2 + 1) / 2;
//    long firstB = (in - firstW2 - 1) / 2;
//    LongMod term = (firstW2 == 0 ? LongMod.create(1, mod) : LongMod.create(firstW1 + firstW2, mod))
//        .multiply(factorial(firstB + 2, mod))
//        .multiply(factorial(firstW2 + extraW, mod))
//        .multiply(1L << firstW2)
//        .multiply(factorial(firstW1 + firstW2, mod));
//    for (long w2 = firstW2; in - w2 - 1 >= 0 && in - 3*w2 + 1 >= 0; w2 += 2) {
//      long b = (in - w2 - 1) / 2;
//      checkState(w2 >= 0);
//      long w1 = (in - 3*w2 + 1) / 2;
//      checkState(w1 >= 0);
//
////      System.out.printf("in=%d w1=%d w2=%d b=%d -> %d\n", in, w1, w2, b, term.n());
//      res = res.add(term);
//
//      term = term.multiply(w1).multiply(w1-1).multiply(w1 - 2).multiply(w2 + extraW + 1).multiply(w2 + extraW + 2).multiply(4)
//          .divide(LongMod.create(w1+w2, mod).multiply(w2+1).multiply(w2+2).multiply(b + 2).multiply(w1 + w2));
//    }
//    return res;
//  }
//
//  private LongMod factorial(long n, long mod) {
//    LongMod res = LongMod.create(1, mod);
//    while (n > 0) {
//      res = res.multiply(n--);
//    }
//    return res;
//  }
}
