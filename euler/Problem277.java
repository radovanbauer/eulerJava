package euler;

import java.util.Optional;

public class Problem277 {

  public static void main(String[] args) {
    System.out.println(new Problem277().solve());
  }

  public long solve() {
    long min = 1_000_000_000_000_000L;
    String sequence = "UDDDUdddDDUDDddDdDddDDUDDdUUDd";
    for (long n = 0;; n++) {
      Optional<Long> res = reverseCollatz(sequence, n);
      if (res.isPresent() && res.get() > min) {
        return res.get();
      }
    }
  }

  private Optional<Long> reverseCollatz(String sequence, long n) {
    char[] chars = sequence.toCharArray();
    for (int i = chars.length - 1; i >= 0; i--) {
      switch (chars[i]) {
      case 'D':
        n *= 3;
        break;
      case 'U':
        if ((n * 3 - 2) % 4 != 0) {
          return Optional.empty();
        }
        n = (n * 3 - 2) / 4;
        break;
      case 'd':
        if ((n * 3 + 1) % 2 != 0) {
          return Optional.empty();
        }
        n = (n * 3 + 1) / 2;
        break;
      }
    }
    return Optional.of(n);
  }
}
