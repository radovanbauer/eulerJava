package euler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class Problem387 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem387().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long n = 16;
    SetMultimap<Integer, Long> harshadNumbers = HashMultimap.create();
    LongStream.rangeClosed(1, 9).forEach(l -> harshadNumbers.put(1, l));
    for (int i = 2; i < n; i++) {
      Long[] newNumbers = harshadNumbers.get(i - 1).stream()
          .flatMapToLong(harshad -> LongStream.rangeClosed(0,  9).map(d -> harshad * 10 + d))
          .filter(this::isHarshad)
          .boxed()
          .toArray(Long[]::new);
      harshadNumbers.putAll(i, Arrays.asList(newNumbers));
    }
    return harshadNumbers.values().stream().parallel()
        .filter(this::isStrong)
        .flatMapToLong(harshad -> LongStream.rangeClosed(0, 9).map(d -> harshad * 10 + d))
        .filter(this::isPrime)
        .sum();
  }

  private boolean isHarshad(long number) {
    return number % sumOfDigits(number) == 0;
  }

  private boolean isStrong(long number) {
    return isPrime(number / sumOfDigits(number));
  }

  private int sumOfDigits(long n) {
    return String.valueOf(n).chars().map(c -> c - '0').sum();
  }

  private boolean isPrime(long n) {
    if (n <= 1) {
      return false;
    }
    for (long d = 2; d * d <= n; d++) {
      if (n % d == 0) {
        return false;
      }
    }
    return true;
  }
}
