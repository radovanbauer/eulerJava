package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Problem546 {

  public static void main(String[] args) {
    System.out.println(new Problem546().solve());
  }

  private static final int MOD = 1_000_000_007;
  private static final int MAX = 47;

  public long solve() {
    LongMod res = LongMod.zero(MOD);
    for (int k = 2; k <= 10; k++) {
      res = res.add(solve(LongMath.pow(10, 14), k));
    }
    return res.n();
  }

  public LongMod solve(long n, int k) {
    Folynome[] folynomes = new Folynome[MAX];
    folynomes[0] = Folynome.create(ImmutableList.of(Member.create(LongMod.create(1, MOD), 0, 0))).sum(k);
    for (int i = 1; i < folynomes.length; i++) {
      folynomes[i] = folynomes[i - 1].sum(k);
    }
    long[][] modPows = new long[k][MAX + 1];
    for (int i = 0; i < k; i++) {
      for (int exp = 0; exp <= MAX; exp++) {
        if (exp == 0) {
          modPows[i][exp] = 1;
        } else {
          modPows[i][exp] = LongMod.create(modPows[i][exp - 1], MOD).multiply(i).n();
        }
      }
    }
    return g(n, k, 0, folynomes, modPows, new HashMap<>());
  }

  private static LongMod g(long n, int k, int degree, Folynome[] folynomes, long[][] modPows, Map<CacheKey, LongMod> cache) {
    if (n == 0) {
      return LongMod.create(1, MOD);
    } else {
      CacheKey key = CacheKey.create(degree, n);
      if (cache.containsKey(key)) {
        return cache.get(key);
      } else {
        if (degree >= folynomes.length) {
          System.out.println(n);
        }
        LongMod res = folynomes[degree].eval(n, k, folynomes, modPows, cache);
        cache.put(key, res);
        return res;
      }
    }
  }

  @AutoValue
  static abstract class CacheKey {
    abstract int degree();
    abstract long n();

    static CacheKey create(int degree, long n) {
      return new AutoValue_Problem546_CacheKey(degree, n);
    }
  }

  @AutoValue
  static abstract class Folynome {
    abstract ImmutableList<Member> members();

    static Folynome create(Iterable<Member> members) {
      return new AutoValue_Problem546_Folynome(Ordering.natural().immutableSortedCopy(members));
    }

    public Folynome sum(int k) {
      Table<Integer, Integer, Member> newMembers = HashBasedTable.create();
      for (Member member : members()) {
        for (Member summedMember : member.sum(k)) {
          Member existingMember = newMembers.get(summedMember.modExp(), summedMember.sumDegree());
          if (existingMember == null) {
            newMembers.put(summedMember.modExp(), summedMember.sumDegree(), summedMember);
          } else {
            LongMod constantSum = existingMember.constant().add(summedMember.constant());
            newMembers.put(summedMember.modExp(), summedMember.sumDegree(),
                Member.create(constantSum, summedMember.modExp(), summedMember.sumDegree()));
          }
        }
      }
      return create(newMembers.values());
    }

    LongMod eval(long n, int k, Folynome[] folynomes, long[][] modPows, Map<CacheKey, LongMod> cache) {
      checkArgument(n >= 0);
      LongMod res = LongMod.zero(MOD);
      for (Member member : members()) {
        res = res.add(member.eval(n, k, folynomes, modPows, cache));
      }
      return res;
    }
  }

  @AutoValue
  static abstract class Member implements Comparable<Member> {
    abstract LongMod constant();
    abstract int modExp();
    abstract int sumDegree();

    static Member create(LongMod constant, int modExp, int sumDegree) {
      return new AutoValue_Problem546_Member(constant, modExp, sumDegree);
    }

    List<Member> sum(int k) {
      List<Member> result = new ArrayList<>();
      LongMod mPowSum = LongMod.zero(MOD);
      for (int m = 0; m < k; m++) {
        mPowSum = mPowSum.add(LongMod.create(m, MOD).pow(modExp()));
      }
      result.add(Member.create(constant().multiply(mPowSum), 0, sumDegree() + 1));
      result.add(Member.create(constant().multiply(mPowSum).negate(), 0, sumDegree()));
      for (int newExp = 1; newExp <= modExp() + 1; newExp++) {
        BigFraction coef = c[modExp()][newExp];
        result.add(Member.create(constant().multiply(coef.n()).divide(coef.d()), newExp, sumDegree()));
      }
      if (modExp() == 0) {
        result.add(Member.create(constant(), 0, sumDegree()));
      }
      return result;
    }

    LongMod eval(long n, int k, Folynome[] folynomes, long[][] modPows, Map<CacheKey, LongMod> cache) {
      return constant()
          .multiply(modPows[Ints.checkedCast(n % k)][modExp()])
          .multiply(g(n / k, k, sumDegree(), folynomes, modPows, cache));
    }

    @Override
    public int compareTo(Member that) {
      return ComparisonChain.start()
          .compare(this.sumDegree(), that.sumDegree(), Comparator.reverseOrder())
          .compare(this.modExp(), that.modExp(), Comparator.reverseOrder())
          .result();
    }

    @Override
    public String toString() {
      return String.format("%d*mod^%d*G_%d", constant().n(), modExp(), sumDegree());
    }
  }

  private static final BigFraction[][] c = new BigFraction[MAX][MAX + 2];
  static {
    Arrays.fill(c[0], BigFraction.ZERO);
    c[0][1] = BigFraction.ONE;
    for (int k = 1; k < c.length; k++) {
      Arrays.fill(c[k], BigFraction.ZERO);
      BigFraction sum = BigFraction.ZERO;
      for (int i = 2; i <= k + 1; i++) {
        c[k][i] = c[k - 1][i - 1].multiply(k).divide(i);
        sum = sum.add(c[k][i]);
      }
      c[k][1] = BigFraction.ONE.subtract(sum);
    }
  }
}
