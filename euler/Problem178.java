package euler;

public class Problem178 {

  public static void main(String[] args) {
    System.out.println(new Problem178().solve(40));
  }

  public long solve(int n) {
    long res = 0L;
    for (int digits = 1; digits <= n; digits++) {
      for (int firstDigit = 1; firstDigit <= 9; firstDigit++) {
        res += count(digits, firstDigit, (1 << 10) - 1);
      }
    }
    return res;
  }

  private Long[][][] cache = new Long[41][10][1 << 10];

  private long count(int digits, int firstDigit, int presentDigits) {
    if (cache[digits][firstDigit][presentDigits] != null) {
      return cache[digits][firstDigit][presentDigits];
    }
    long res;
    if (digits == 0) {
      res = 0L;
    } else if (digits == 1) {
      if (presentDigits == 1 << firstDigit) {
        res = 1L;
      } else {
        res = 0L;
      }
    } else {
      res = 0L;
      if ((presentDigits & (1 << firstDigit)) != 0) {
        if (firstDigit < 9) {
          res += count(digits - 1, firstDigit + 1, presentDigits);
          res += count(digits - 1, firstDigit + 1, presentDigits ^ (1 << firstDigit));
        }
        if (firstDigit > 0) {
          res += count(digits - 1, firstDigit - 1, presentDigits);
          res += count(digits - 1, firstDigit - 1, presentDigits ^ (1 << firstDigit));
        }
      }
    }
    return cache[digits][firstDigit][presentDigits] = res;
  }
}
