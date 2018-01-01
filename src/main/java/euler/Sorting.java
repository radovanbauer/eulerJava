package euler;

import java.util.Random;

public class Sorting {

  public static interface CompareFunction {
    int compare(int i1, int i2);
  }

  public static interface SwapFunction {
    void swap(int i1, int i2);
  }

  private static final Random random = new Random();

  public static void quickSort(
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

  public static void swap(long[] arr, int i, int j) {
    long tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }
}
