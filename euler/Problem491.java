package euler;

import static com.google.common.math.LongMath.checkedMultiply;

import java.util.HashSet;
import java.util.Set;

import com.google.common.primitives.Ints;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.math.LongMath;

public class Problem491 {

  public static void main(String[] args) {
    System.out.println(new Problem491().solve());
  }

  public long solve() {
    HashSet<Multiset<Integer>> sets = new HashSet<Multiset<Integer>>();
    generateSets(0, new int[10], -1, sets);
    return sets.stream().mapToLong(this::count).sum();
  }

  private long count(Multiset<Integer> set1) {
    int sum = 0;
    for (int i : set1) {
      sum += i;
    }
    if (sum % 11 != 1) {
      return 0L;
    }
    Multiset<Integer> set2 = HashMultiset.create();
    for (int i = 0; i < 10; i++) {
      set2.add(i, 2);
    }
    Multisets.removeOccurrences(set2, set1);
    long res = checkedMultiply(permutationCount(set1), permutationCount(set2));
    if (set1.contains(0)) {
      HashMultiset<Integer> set1WithoutZero = HashMultiset.create(set1);
      set1WithoutZero.remove(0, 1);
      res -= checkedMultiply(permutationCount(set1WithoutZero), permutationCount(set2));
    }
    return res;
  }

  private void generateSets(int picked, int[] pick, int last, Set<Multiset<Integer>> set) {
    if (picked == 10) {
      set.add(HashMultiset.create(Ints.asList(pick)));
    } else {
      for (int next = last + 1; next < 11 + picked; next++) {
        pick[picked] = next % 10;
        generateSets(picked + 1, pick, next, set);
      }
    }
  }

  public long permutationCount(Multiset<?> multiset) {
    long res = LongMath.factorial(multiset.size());
    for (Multiset.Entry<?> entry : multiset.entrySet()) {
      if (entry.getCount() > 1) {
        res /= LongMath.factorial(entry.getCount());
      }
    }
    return res;
  }
}
