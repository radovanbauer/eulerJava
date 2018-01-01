package euler;

import java.util.function.Function;

public class NewtonRaphson {

  public interface LongFractionFunction {
    LongFraction apply(LongFraction value);
  }

  public static <T> T calculate(
      Field<T> field,
      Function<T, T> f,
      Function<T, T> df,
      T start,
      int iterations) {
    T result = start;
    for (int i = 0; i < iterations; i++) {
      result = field.subtract(result, field.divide(f.apply(result), df.apply(result)));
    }
    return result;
  }
}
