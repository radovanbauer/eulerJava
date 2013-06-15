package euler;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Problem430v2 {

  public static void main(String[] args) {
    System.out.println(String.format("%.2f",
        new Problem430v2().solve(10000000000L, 4000)));
  }

  public double solve(final long n, final int k) {
    final int tasks = 20;
    ExecutorService executor = Executors.newFixedThreadPool(tasks);
    List<Future<Double>> futures = Lists.newArrayList(); 
    for (int task = 0; task < tasks; task++) {
      final long start = n / tasks * task;
      final long end = task < tasks - 1 ? n / tasks * (task + 1) : n;
      futures.add(executor.submit(new Callable<Double>() {
        @Override
        public Double call() {
          return count(n, k, start, end);
        }
      }));
    }
    double sum = 0D;
    for (Future<Double> future : futures) {
      try {
        sum += future.get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return sum;
  }

  private double count(long n, int k, long start, long end) {
    double sum = 0D;
    for (long i = start; i < end; i++) {
      double q = 1D * (n - i - 1) / n * (n - i - 1) / n + 1D * i / n * i / n;
      double p = 1D - q;
      double exp = (1D + Math.pow(p - q, k)) / 2D;
      sum += exp;
    }
    return sum;
  }
}
