package euler;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.math.LongMath;

public class Problem480 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem480().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    String word = "thereisasyetinsufficientdataforameaningfulanswer";
    int[] charCount = new int['z' - 'a' + 1];
    for (int i = 0; i < word.length(); i++) {
      charCount[word.charAt(i) - 'a']++;
    }
    return wordAt(
        position("legionary", charCount) + position("calorimeters", charCount)
            - position("annihilate", charCount) + position("orchestrated", charCount)
            - position("fluttering", charCount),
        charCount);
  }

  private long count(int[] charCount, int maxLength) {
    return count(charCount, 0, 0, maxLength, 1);
  }

  private long count(int[] charCount, int next, int groupSize, int remaining, long groupFact) {
    long sum = 0L;
    if (groupSize > 0) {
      sum += LongMath.factorial(groupSize) / groupFact;
    }
    for (int i = next; i < charCount.length; i++) {
      for (int count = 1; count <= charCount[i] && count <= remaining; count++) {
        long newGroupFact = groupFact * LongMath.factorial(count);
        sum += count(charCount, i + 1, groupSize + count, remaining - count, newGroupFact);
      }
    }
    return sum;
  }

  private long position(String word, int[] charCount) {
    checkArgument(word.length() <= 15);
    long sum = 0;
    int[] remainingCharCount = charCount.clone();
    for (int i = 0; i < word.length(); i++) {
      for (char c = 'a'; c < word.charAt(i); c++) {
        if (remainingCharCount[c - 'a'] > 0) {
          remainingCharCount[c - 'a']--;
          sum += 1 + count(remainingCharCount, 14 - i);
          remainingCharCount[c - 'a']++;
        }
      }
      remainingCharCount[word.charAt(i) - 'a']--;
      sum++;
      checkArgument(remainingCharCount[word.charAt(i) - 'a'] >= 0);
    }
    return sum;
  }

  private String wordAt(long position, int[] charCount) {
    StringBuilder result = new StringBuilder();
    int[] remainingCharCount = charCount.clone(); 
    for (int i = 0; position > 0; i++) {
      char c = 'a';
      while (true) {
        if (remainingCharCount[c - 'a'] > 0) {
          position--;
          if (position == 0) {
            break;
          }
          remainingCharCount[c - 'a']--;
          long count = count(remainingCharCount, 14 - i);
          remainingCharCount[c - 'a']++;
          if (position - count <= 0) {
            break;
          } else {
            position -= count;
          }
        }
        c++;
      }
      result.append(c);
      remainingCharCount[c - 'a']--;
    }
    return result.toString();
  }
}
