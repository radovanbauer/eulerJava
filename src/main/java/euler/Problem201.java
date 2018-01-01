package euler;

public class Problem201 {

  public static void main(String[] args) {
    System.out.println(new Problem201().solve());
  }

  public long solve() {
    int[] sums = new int[101];
    for (int i = 1; i <= 100; i++) {
      sums[i] = sums[i - 1] + i * i;
    }
    long res = 0L;
    for (int sum = 0; sum <= 295425; sum++) {
      Count count = Count.ZERO;
      for (int lastTermIdx = 50; lastTermIdx <= 100; lastTermIdx++) {
        count = count.plus(count(sum, 50, lastTermIdx, sums));
      }
      if (count == Count.ONE) {
        res += sum;
      }
    }
    return res;
  }

  private enum Count {
    ZERO, ONE, MANY;

    public Count plus(Count that) {
      if (this == ZERO && that == ZERO) return ZERO;
      if (this == ZERO && that == ONE) return ONE;
      if (this == ONE && that == ZERO) return ONE;
      return MANY;
    }
  }

  private Count[][][] cache = new Count[295426][51][101];  

  private Count count(int sum, int terms, int lastTermIdx, int[] sums) {
    if (cache[sum][terms][lastTermIdx] != null) {
      return cache[sum][terms][lastTermIdx];
    }
    Count res;
    if (terms == 0) {
      res = sum == 0 && lastTermIdx == 0 ? Count.ONE : Count.ZERO;
    } else if (lastTermIdx < terms) {
      res = Count.ZERO;
    } else if (lastTermIdx * lastTermIdx + sums[terms - 1] > sum) {
      res = Count.ZERO;
    } else if (sums[lastTermIdx] - sums[lastTermIdx - terms] < sum) {
      res = Count.ZERO;
    } else {
      res = Count.ZERO;
      for (int nextIdx = lastTermIdx - 1; nextIdx >= 0; nextIdx--) {
        res = res.plus(count(
            sum - lastTermIdx * lastTermIdx,
            terms - 1,
            nextIdx,
            sums));
        if (res == Count.MANY) {
          break;
        }
      }
    }
    return cache[sum][terms][lastTermIdx] = res;
  }
}
