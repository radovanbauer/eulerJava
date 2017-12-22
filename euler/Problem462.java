package euler;

import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import io.vavr.collection.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.math.LongMath.checkedMultiply;
import static com.google.common.math.LongMath.checkedPow;

public class Problem462 {
  public static void main(String[] args) {
    Runner.run(new Problem462()::solve);
  }

  public long solve() {
    long max = LongMath.pow(10, 3);
    List<Integer> pows = new ArrayList<>();
    int count = 0;
    for (int pow3 = 0; checkedPow(3, pow3) <= max; pow3++) {
      int pow2 = 0;
      count++;
      while (checkedMultiply(checkedPow(2, pow2 + 1), checkedPow(3, pow3)) <= max) {
        pow2++;
        count++;
      }
      pows.add(pow2);
    }
    pows.add(-1);
    pows = ImmutableList.of(1, 1, 0, 0, 0, 0, -1);
    System.out.println("count=" + count);
    System.out.println(pows);
    long res = count(Vector.ofAll(pows));
    System.out.println("cache=" + cache.size());
    return res;
  }

  private final Map<Vector<Integer>, Long> cache = new HashMap<>();

  private long count(Vector<Integer> pows) {
    if (pows.forAll(pow -> pow == -1)) {
      return 1;
    }
    if (cache.containsKey(pows)) {
      return cache.get(pows);
    }
    long count = 0;
    for (int i = 0; i < pows.length(); i++) {
      if (pows.get(i) >= 0 && pows.get(i + 1) < pows.get(i)) {
        count += count(pows.update(i, pows.get(i) - 1));
      }
    }
    cache.put(pows, count);
    return count;
  }
}
