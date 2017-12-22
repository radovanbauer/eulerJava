package euler;

import com.google.common.base.Stopwatch;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Runner {
  public static void run(Callable<Object> runnable) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    try {
      System.out.println(runnable.call());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
    System.out.printf("Elapsed: %.3f s\n", millis/1000.0);
  }
}
