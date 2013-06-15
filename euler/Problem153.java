package euler;

public class Problem153 {

  public static void main(String[] args) {
    System.out.println(new Problem153().solve(100000000L));
  }

  public long solve(long max) {
    long sum = 0L;
    for (long a = 1L; a <= max; a++) {
      sum += max / a * a;
      for (long b = 1L; a * a + b * b <= max; b++) {
        if (gcd(a, b) != 1L) {
          continue;
        }
        long c = a * a + b * b;
        for (long k = 1L; k * c <= max; k++) {
          sum += max / (k * c) * (k * a * 2L);
        }
      }
    }
    return sum;
  }

  private long gcd(long a, long b) {
    return b == 0L ? a : gcd(b, a % b);
  }
}
