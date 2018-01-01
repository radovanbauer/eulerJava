package euler;

import com.google.common.math.LongMath;

import java.util.stream.LongStream;

public class Problem570 {
  public static void main(String[] args) {
    Runner.run(new Problem570()::solve);
  }

  // 1,0,0,2
  // 1,0,2,2
  // 1,2,2,2
  // 3,2,2,2
  // 3,2,2,4
  // 3,2,4,4
  // 3,4,4,4
  //
  // f1(n+1) = 3*f1(n) + 1*f2(n)
  // f2(n+1) = 2*f1(n) + 2*f2(n)
  // f3(n+1) = f2(n) + 3*f3(n)
  // f4(n+1) = f1(n) + 2*f2(n) + 3*f3(n)
  // f5(n+1) = 6*f4(n) + 3*f5(n) + f6(n)
  // f6(n+1) = 2*f5(n) + 2*f6(n)
  // f7(n+1) = f6(n) + 3*f7(n)
  //
  // f1(n) = 4^(n-1) + 2
  // f2(n) = 4^(n-1) - 4
  // f3(n) = 4^(n-1) - 2*3^(n-1) + 2
  // f4(n) = 3/2*4^(n-1) - 2*3^(n-1)
  // f5(n) = 3/2*4^(n-1)*n - 8*4^(n-1) + 6*3^(n-1) + 2
  // f6(n) = 3/2*4^(n-1)*n - 11*4^(n-1) + 12*3^(n-1) - 4
  // f7(n) = 3/2*4^(n-1)*n + 4*3^(n-1)*n - 17*4^(n-1) + 10*3^(n-1) + 2
  //
  // A(n) = 6*(2*4^(n-2) - 3^(n-2))
  // B(n) = 6*(3*4^(n-2)*n + 2*3^(n-2)*n - 23*4^(n-2) + 13*3^(n-2))
  //
  // G(n) = 6*GCD(2*4^(n-2) - 3^(n-2), 3*4^(n-2)*n + 2*3^(n-2)*n - 23*4^(n-2) + 13*3^(n-2))
  //      = 6*GCD(2*4^(n-2) - 3^(n-2), 3*4^(n - 2)*n + 2*3^(n - 2)*n + 3*4^(n - 2))
  //      = 6*GCD(2*4^(n-2) - 3^(n-2), 4^(n-2)*(7*n + 3))
  //      = 6*GCD(2*4^(n-2) - 3^(n-2), 7*n + 3)
  //      = 6*GCD((2*4^(n-2) - 3^(n-2)) % (7*n + 3), 7n + 3)


  public long solve() {
    return LongStream.rangeClosed(3, 10_000_000)
        .map(n -> 6 * LongMath.gcd(evalA(n, 7 * n + 3).n(), 7 * n + 3)).sum();
  }

  private LongMod evalA(long n, long mod) {
    // 2*4^(n-2) - 3^(n-2)
    return LongMod.create(4, mod).pow(n - 2).multiply(2).subtract(LongMod.create(3, mod).pow(n - 2));
  }
}
