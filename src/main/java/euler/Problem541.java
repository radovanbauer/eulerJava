package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Iterables;

public class Problem541 {

  public static void main(String[] args) {
    System.out.println(new Problem541().solve());
  }

  public long solve() {
    int N = 10;
    int s = 10;
    int p = 137;
    BigFraction[] b = new BigFraction[N];
    for (int n = 1; n <= N; n++) {
      b[n - 1] = h(p * n).subtract(h(n).divide(p));
    }
    BigFraction[][] m = new BigFraction[N][N];
    for (int i = 1; i <= N; i++) {
      for (int j = 1; j <= N; j++) {
        m[i - 1][j - 1] = BigFraction.create(BigInteger.valueOf(i).pow(2 * j));
      }
    }
    BigFraction[] sol = linearSolve(m, b);
    PAdic[] cp = new PAdic[N];
    for (int i = 1; i <= N; i++) {
      cp[i - 1] = PAdic.create(p, sol[i - 1], s);
    }
    List<HNumber> g = new ArrayList<>();
    for (int n = 1; n <= p - 1; n++) {
      HNumber num = HNumber.create(n, PAdic.create(p, h(n), s));
      if (num.h().ord() >= 1) {
        g.add(num);
      }
    }
    while (true) {
      List<HNumber> newG = new ArrayList<>();
      for (HNumber num : g) {
        PAdic curh = num.h().shift(-1, s);
        for (int i = 1; i <= N; i++) {
          curh = curh.add(
              cp[i - 1].multiply(
                  PAdic.create(p, BigFraction.create(BigInteger.valueOf(num.n()).pow(2 * i)), s), s), s);
        }
        for (int k = 0; k <= p - 1; k++) {
          long newN = num.n() * p + k;
          if (curh.ord() >= 1) {
            newG.add(HNumber.create(newN, curh));
          }
          curh = PAdic.create(p, curh.asBigFraction().add(BigFraction.create(1, newN + 1)), s);
        }
      }
      if (newG.isEmpty()) {
        return Iterables.getLast(g).n() * p + (p - 1);
      }
      g = newG;
    }
  }

  @AutoValue
  static abstract class HNumber {
    abstract long n();
    abstract PAdic h();
    static HNumber create(long n, PAdic h) {
      return new AutoValue_Problem541_HNumber(n, h);
    }
  }

  private BigFraction h(long n) {
    BigFraction res = BigFraction.ZERO;
    for (int i = 1; i <= n; i++) {
      res = res.add(BigFraction.create(1, i));
    }
    return res;
  }

  private BigFraction[] linearSolve(BigFraction[][] m, BigFraction[] b) {
    BigFraction[][] _m = new BigFraction[m.length][];
    for (int i = 0; i < m.length; i++) {
      _m[i] = m[i].clone();
    }
    BigFraction[] _b = b.clone();
    for (int column = 0; column < m.length; column++) {
      int row = column;
      int nonZeroRow = -1;
      for (int r = column; r < m.length; r++) {
        if (!_m[r][column].equals(BigFraction.ZERO)) {
          nonZeroRow = r;
          break;
        }
      }
      checkArgument(nonZeroRow != -1, "No solution or multiple solutions");
      if (nonZeroRow > row) {
        for (int c = column; c < m.length; c++) {
          _m[row][c] = _m[row][c].add(_m[nonZeroRow][c]);
        }
        _b[row] = _b[row].add(_b[nonZeroRow]);
      }
      for (int r = row + 1; r < m.length; r++) {
        if (!_m[r][column].equals(BigFraction.ZERO)) {
          BigFraction ratio = _m[r][column].divide(_m[row][column]);
          for (int c = column; c < m.length; c++) {
            _m[r][c] = _m[r][c].subtract(_m[row][c].multiply(ratio));
          }
          _b[r] = _b[r].subtract(_b[row].multiply(ratio));
        }
      }
    }
    BigFraction[] res = new BigFraction[m.length];
    for (int i = m.length - 1; i >= 0; i--) {
      res[i] = _b[i];
      for (int j = m.length - 1; j > i; j--) {
        res[i] = res[i].subtract(_m[i][j].multiply(res[j]));
      }
      res[i] = res[i].divide(_m[i][i]);
    }
    return res;
  }
}
