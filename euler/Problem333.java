package euler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;

public class Problem333 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem333().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public long solve() {
    long max = 1_000_000 - 1;
    List<Long> partitions = new ArrayList<>();
    for (long i = 1; i <= max; i *= 2) {
      for (long j = 1; i * j <= max; j *= 3) {
        partitions.add(i * j);
      }
    }
    Collections.sort(partitions, Ordering.natural());
    Multiset<Long> count = HashMultiset.create();
    solve(max, partitions, 0, new ArrayDeque<Long>(), 0, count);
    FactorizationSieve sieve = new FactorizationSieve(max);
    return LongStream.rangeClosed(2, max).filter(n -> sieve.isPrime(n) && count.count(n) == 1).sum();
  }

  private void solve(long max, List<Long> partitions, int next, ArrayDeque<Long> chosen, long sum,
      Multiset<Long> count) {
    nextPartition: for (int i = next; i < partitions.size(); i++) {
      long partition = partitions.get(i);
      long newSum = sum + partition;
      if (newSum > max) {
        return;
      }
      for (long prevPartition : chosen) {
        if (prevPartition % partition == 0 || partition % prevPartition == 0) {
          continue nextPartition;
        }
      }
      count.add(newSum);
      chosen.addLast(partition);
      solve(max, partitions, i + 1, chosen, newSum, count);
      chosen.removeLast();
    }
  }
}
