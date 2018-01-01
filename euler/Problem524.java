package euler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import com.google.auto.value.AutoValue;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;

public class Problem524 {

  public static void main(String[] args) {
    System.out.println(new Problem524().solve());
  }

  public long solve() {
    long k = LongMath.pow(12, 12);
    Perm bestPerm = r(k);
    return bestPerm.index().longValueExact();
  }

  private Perm r(long k) {
    List<Integer> fListPrototype = getFListFromK(k);
    Perm bestPerm = null;
    for (List<Integer> fList : Collections2.permutations(fListPrototype)) {
      List<Integer> perm = genPermFromFList(fList);
      BigInteger index = index(perm);
      if (bestPerm == null || bestPerm.index().compareTo(index) > 0) {
        bestPerm = Perm.create(perm, index);
        System.out.println(fList);
      }
    }
    return bestPerm;
  }

  @AutoValue
  static abstract class Perm {
    abstract ImmutableList<Integer> perm();
    abstract BigInteger index();

    static Perm create(Iterable<Integer> perm, BigInteger index) {
      return new AutoValue_Problem524_Perm(ImmutableList.copyOf(perm), index);
    }
  }

  private List<Integer> getFListFromK(long k) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < 64; i++) {
      if ((k & (1L << i)) != 0) {
        result.add(i + 1);
      }
    }
    return result;
  }

  private List<Integer> genPermFromFList(List<Integer> fList) {
    List<Integer> actualNumbers = new ArrayList<>();
    TreeSet<Integer> after = new TreeSet<>();
    for (int i = fList.size() - 1; i >= 0; i--) {
      int actual = fList.get(i);
      while (after.contains(actual) || actual - after.headSet(actual).size() < fList.get(i)) {
        actual++;
      }
      actualNumbers.add(actual);
      after.add(actual);
    }
    Collections.reverse(actualNumbers);
    HashSet<Integer> remaining = new LinkedHashSet<>(actualNumbers);
    List<Integer> result = new ArrayList<>();
    int max = 0;
    int idx = 0;
    while (!remaining.isEmpty()) {
      if (max > actualNumbers.get(idx)) {
        result.add(actualNumbers.get(idx));
        remaining.remove(actualNumbers.get(idx));
        idx++;
      } else {
        max++;
        while (remaining.contains(max)) {
          max++;
        }
        result.add(max);
      }
    }
    return result;
  }

  private BigInteger index(List<Integer> perm) {
    BigInteger sum = BigInteger.ZERO;
    BigInteger fact = BigInteger.ONE;
    TreeSet<Integer> after = new TreeSet<>();
    for (int i = perm.size() - 1; i >= 0; i--) {
      int lowerAfter = after.headSet(perm.get(i)).size();
      sum = sum.add(BigInteger.valueOf(lowerAfter).multiply(fact));
      after.add(perm.get(i));
      fact = fact.multiply(BigInteger.valueOf(perm.size() - i));
    }
    return sum.add(BigInteger.ONE);
  }
}
