package euler;

import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Iterables;

public class Problem336 {

  public static void main(String[] args) {
    System.out.println(new Problem336().solve());
  }

  public String solve() {
    SortedSet<String> oldTrains = new TreeSet<>();
    SortedSet<String> newTrains = new TreeSet<>();
    int n = 11;
    String firstTrain = "";
    for (int i = 0; i < n; i++) {
      firstTrain += (char) ('A' + i);
    }
    oldTrains.add(firstTrain);
    for (int round = 0; round < (n - 1) * 2 - 1; round++) {
      for (String train : oldTrains) {
        if (round == 0) {
          newTrains.add(rotate(train, 2));
        } else if (round % 2 == 0) {
          for (int k = 0; k < round / 2; k++) {
            newTrains.add(rotate(train, k + 2));
          }
        } else {
          newTrains.add(rotate(train, round / 2 + 3));
        }
      }
      oldTrains = newTrains;
      newTrains = new TreeSet<>();
    }
    return Iterables.get(oldTrains, 2011 - 1);
  }

  private String rotate(String s, int k) {
    char[] chars = s.toCharArray();
    int n = chars.length;
    for (int i = 0; i < k / 2; i++) {
      int a = n - k + i;
      int b = n - i - 1;
      char tmp = chars[a];
      chars[a] = chars[b];
      chars[b] = tmp;
    }
    return new String(chars);
  }
}
