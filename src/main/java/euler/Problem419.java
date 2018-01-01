package euler;

import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class Problem419 {

  public static void main(String[] args) {
    System.out.println(new Problem419().solve());
  }

  private static final Multimap<Integer, Integer> transitions = ImmutableMultimap.<Integer, Integer>builder()
      .putAll(1, 63)
      .putAll(2, 64, 62)
      .putAll(3, 65)
      .putAll(4, 66)
      .putAll(5, 68)
      .putAll(6, 69)
      .putAll(7, 84, 55)
      .putAll(8, 70)
      .putAll(9, 71)
      .putAll(10, 76)
      .putAll(11, 77)
      .putAll(12, 82)
      .putAll(13, 78)
      .putAll(14, 79)
      .putAll(15, 80)
      .putAll(16, 81, 29, 91)
      .putAll(17, 81, 29, 90)
      .putAll(18, 81, 30)
      .putAll(19, 75, 29, 92)
      .putAll(20, 75, 32)
      .putAll(21, 72)
      .putAll(22, 73)
      .putAll(23, 74)
      .putAll(24, 83)
      .putAll(25, 86)
      .putAll(26, 87)
      .putAll(27, 88)
      .putAll(28, 89, 92)
      .putAll(29, 1)
      .putAll(30, 3)
      .putAll(31, 4)
      .putAll(32, 2, 61, 29, 85)
      .putAll(33, 5)
      .putAll(34, 28)
      .putAll(35, 24, 33, 61, 29, 91)
      .putAll(36, 24, 33, 61, 29, 90)
      .putAll(37, 7)
      .putAll(38, 8)
      .putAll(39, 9)
      .putAll(40, 10)
      .putAll(41, 21)
      .putAll(42, 22)
      .putAll(43, 23)
      .putAll(44, 11)
      .putAll(45, 19)
      .putAll(46, 12)
      .putAll(47, 13)
      .putAll(48, 14)
      .putAll(49, 15)
      .putAll(50, 18)
      .putAll(51, 16)
      .putAll(52, 17)
      .putAll(53, 20)
      .putAll(54, 6, 61, 29, 92)
      .putAll(55, 26)
      .putAll(56, 27)
      .putAll(57, 25, 29, 92)
      .putAll(58, 25, 29, 67)
      .putAll(59, 25, 29, 85)
      .putAll(60, 25, 29, 68, 61, 29, 89)
      .putAll(61, 61)
      .putAll(62, 33)
      .putAll(63, 40)
      .putAll(64, 41)
      .putAll(65, 42)
      .putAll(66, 43)
      .putAll(67, 38, 39)
      .putAll(68, 44)
      .putAll(69, 48)
      .putAll(70, 54)
      .putAll(71, 49)
      .putAll(72, 50)
      .putAll(73, 51)
      .putAll(74, 52)
      .putAll(75, 47, 38)
      .putAll(76, 47, 55)
      .putAll(77, 47, 56)
      .putAll(78, 47, 57)
      .putAll(79, 47, 58)
      .putAll(80, 47, 59)
      .putAll(81, 47, 60)
      .putAll(82, 47, 33, 61, 29, 92)
      .putAll(83, 45)
      .putAll(84, 46)
      .putAll(85, 53)
      .putAll(86, 38, 29, 89)
      .putAll(87, 38, 30)
      .putAll(88, 38, 31)
      .putAll(89, 34)
      .putAll(90, 36)
      .putAll(91, 35)
      .putAll(92, 37)
      .build();

  private static final ImmutableMap<Integer, String> elements = ImmutableMap.<Integer, String>builder()
      .put(1, "1112")
      .put(2, "1112133")
      .put(3, "111213322112")
      .put(4, "111213322113")
      .put(5, "1113")
      .put(6, "11131")
      .put(7, "111311222112")
      .put(8, "111312")
      .put(9, "11131221")
      .put(10, "1113122112")
      .put(11, "1113122113")
      .put(12, "11131221131112")
      .put(13, "111312211312")
      .put(14, "11131221131211")
      .put(15, "111312211312113211")
      .put(16, "111312211312113221133211322112211213322112")
      .put(17, "111312211312113221133211322112211213322113")
      .put(18, "11131221131211322113322112")
      .put(19, "11131221133112")
      .put(20, "1113122113322113111221131221")
      .put(21, "11131221222112")
      .put(22, "111312212221121123222112")
      .put(23, "111312212221121123222113")
      .put(24, "11132")
      .put(25, "1113222")
      .put(26, "1113222112")
      .put(27, "1113222113")
      .put(28, "11133112")
      .put(29, "12")
      .put(30, "123222112")
      .put(31, "123222113")
      .put(32, "12322211331222113112211")
      .put(33, "13")
      .put(34, "131112")
      .put(35, "13112221133211322112211213322112")
      .put(36, "13112221133211322112211213322113")
      .put(37, "13122112")
      .put(38, "132")
      .put(39, "13211")
      .put(40, "132112")
      .put(41, "1321122112")
      .put(42, "132112211213322112")
      .put(43, "132112211213322113")
      .put(44, "132113")
      .put(45, "1321131112")
      .put(46, "13211312")
      .put(47, "1321132")
      .put(48, "13211321")
      .put(49, "132113212221")
      .put(50, "13211321222113222112")
      .put(51, "1321132122211322212221121123222112")
      .put(52, "1321132122211322212221121123222113")
      .put(53, "13211322211312113211")
      .put(54, "1321133112")
      .put(55, "1322112")
      .put(56, "1322113")
      .put(57, "13221133112")
      .put(58, "1322113312211")
      .put(59, "132211331222113112211")
      .put(60, "13221133122211332")
      .put(61, "22")
      .put(62, "3")
      .put(63, "3112")
      .put(64, "3112112")
      .put(65, "31121123222112")
      .put(66, "31121123222113")
      .put(67, "3112221")
      .put(68, "3113")
      .put(69, "311311")
      .put(70, "31131112")
      .put(71, "3113112211")
      .put(72, "3113112211322112")
      .put(73, "3113112211322112211213322112")
      .put(74, "3113112211322112211213322113")
      .put(75, "311311222")
      .put(76, "311311222112")
      .put(77, "311311222113")
      .put(78, "3113112221131112")
      .put(79, "311311222113111221")
      .put(80, "311311222113111221131221")
      .put(81, "31131122211311122113222")
      .put(82, "3113112221133112")
      .put(83, "311312")
      .put(84, "31132")
      .put(85, "311322113212221")
      .put(86, "311332")
      .put(87, "3113322112")
      .put(88, "3113322113")
      .put(89, "312")
      .put(90, "312211322212221121123222113")
      .put(91, "312211322212221121123222112")
      .put(92, "32112")
      .build();

  public String solve() {
    for (int i = 1; i <= 92; i++) {
      String elem = elements.get(i);
      String nextElem = "";
      for (int next : transitions.get(i)) {
        nextElem += elements.get(next);
      }
      checkState(next(elem).equals(nextElem),
          "elem=%s, nextElem=%s vs %s", elem, nextElem, next(elem));
    }

    long mod = 1L << 30;
    long n = 1_000_000_000_000L;
    int elementCount = elements.size();
    long[][] matrixArray = new long[elementCount][elementCount];
    for (int from = 1; from <= elementCount; from++) {
      for (int to : transitions.get(from)) {
        matrixArray[to - 1][from - 1]++;
      }
    }
    LongModMatrix2 matrix = LongModMatrix2.create(matrixArray, mod);
    long[][] vectorArr = new long[elementCount][1];
    vectorArr[24 - 1][0] = 1;
    vectorArr[39 - 1][0] = 1;
    LongModMatrix2 vector = LongModMatrix2.create(vectorArr, mod);
    LongModMatrix2 res = matrix.pow(n - 8).multiply(vector);

    LongMod count1 = LongMod.zero(mod);
    LongMod count2 = LongMod.zero(mod);
    LongMod count3 = LongMod.zero(mod);
    for (int elem = 1; elem <= elementCount; elem++) {
      count1 = count1.add(res.element(elem - 1, 0) * count(elements.get(elem), '1'));
      count2 = count2.add(res.element(elem - 1, 0) * count(elements.get(elem), '2'));
      count3 = count3.add(res.element(elem - 1, 0) * count(elements.get(elem), '3'));
    }
    return count1.n() + "," + count2.n() + "," + count3.n();
  }

  private long count(String s, char c) {
    return s.chars().filter(x -> x == c).count();
  }

  private String next(String s) {
    char last = 0;
    int count = 0;
    int idx = 0;
    char[] res = new char[s.length() * 2];
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == last) {
        count++;
      } else {
        if (last != 0) {
          res[idx++] = (char) ('0' + count);
          res[idx++] = last;
        }
        last = c;
        count = 1;
      }
    }
    res[idx++] = (char) ('0' + count);
    res[idx++] = last;
    return new String(Arrays.copyOf(res, idx));
  }
}
