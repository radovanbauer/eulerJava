package euler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;
import com.google.common.primitives.Doubles;

public class Problem461 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem461().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    int n = 10000;
    int max = n * 3 / 2;
    double[] ePow = new double[max + 1];
    int[] a = new int[(max + 1) * (max + 1)];
    int[] b = new int[(max + 1) * (max + 1)];
    double[] ePowSum = new double[(max + 1) * (max + 1)];
    int idx = 0;
    for (int i = 0; i <= max; i++) {
      ePow[i] = Math.pow(Math.E, 1D * i / n);
    }
    for (int i = 0; i <= max; i++) {
      for (int j = i; j <= max; j++) {
        a[idx] = i;
        b[idx] = j;
        ePowSum[idx] = ePow[i] + ePow[j];
        idx++;
      }
    }
    quickSort(0, a.length,
        (i, j) -> Doubles.compare(ePowSum[i], ePowSum[j]),
        (i, j) -> { swap(ePowSum, i, j); swap(a, i, j); swap(b, i, j); });
    double min = Double.POSITIVE_INFINITY;
    int minIdx1 = -1, minIdx2 = -1;
    int idx1 = -1, idx2 = a.length - 1;
    while (idx1 + 1 < a.length && idx2 >= 0) {
      idx1++;
      while (idx1 + 1 < a.length && f(ePowSum, idx1 + 1, idx2) < f(ePowSum, idx1, idx2)) {
        idx1++;
      }
      while (idx2 - 1 >= 0 && f(ePowSum, idx1, idx2 - 1) < f(ePowSum, idx1, idx2)) {
        idx2--;
      }
      if (f(ePowSum, idx1, idx2) < min) {
        min = f(ePowSum, idx1, idx2);
        minIdx1 = idx1;
        minIdx2 = idx2;
      }
    }
    return LongMath.pow(a[minIdx1], 2) + LongMath.pow(b[minIdx1], 2)
        + LongMath.pow(a[minIdx2], 2) + LongMath.pow(b[minIdx2], 2);
  }

  private void swap(double[] arr, int a, int b) {
    double tmp = arr[a];
    arr[a] = arr[b];
    arr[b] = tmp;
  }

  private void swap(int[] arr, int a, int b) {
    int tmp = arr[a];
    arr[a] = arr[b];
    arr[b] = tmp;
  }

  private double f(double[] ePowSum, int idx1, int idx2) {
    return Math.abs(Math.PI + 4 - ePowSum[idx1] - ePowSum[idx2]);
  }

  private static interface CompareFunction {
    int compare(int i1, int i2);
  }

  private static interface SwapFunction {
    void swap(int i1, int i2);
  }

  private static final Random random = new Random();

  private static void quickSort(
      int lo, int hi, CompareFunction compareFunction, SwapFunction swapFunction) {
    if (hi <= lo + 1) {
      return;
    }
    int pivotIdx = lo + random.nextInt(hi - lo);
    swapFunction.swap(pivotIdx, hi - 1);
    int p1 = lo - 1;
    int p2 = hi - 1;
    while (p1 < p2) {
      do { p1++; } while (p1 < p2 && compareFunction.compare(p1, hi - 1) < 0);
      do { p2--; } while (p2 > p1 && compareFunction.compare(p2, hi - 1) >= 0);
      if (p1 < p2) {
        swapFunction.swap(p1, p2);
      }
    }
    quickSort(lo, p1, compareFunction, swapFunction);
    p1--;
    p2 = hi - 1;
    while (p1 < p2) {
      do { p1++; } while (p1 < p2 && compareFunction.compare(p1, hi - 1) == 0);
      do { p2--; } while (p1 < p2 && compareFunction.compare(p2, hi - 1) > 0);
      if (p1 < p2) {
        swapFunction.swap(p1, p2);
      }
    }
    swapFunction.swap(p1, hi - 1);
    quickSort(p1, hi, compareFunction, swapFunction);
  }
}
