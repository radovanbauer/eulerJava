package euler;

import java.util.stream.IntStream;

public class Problem572 {
  public static void main(String[] args) {
    Runner.run(new Problem572()::solve);
  }

  // a*a + b*d + c*g = a
  // g = (a - a*a - b*d)/c
  //
  // a*b + b*e + c*h = b
  // h = (b - a*b - b*e)/c
  //
  // a*d + d*e + g*f = d
  // f = (d - a*d - d*e)/g
  //
  // a*c + b*f + c*i = c
  // i = (c - a*c - b*f)/c

  public long solve() {
    int n = 200;
    int[] all = IntStream.rangeClosed(-n, n).toArray();
    return IntStream.rangeClosed(-n, n).parallel().mapToLong(a -> solve(n, a, all)).sum();
  }

  private long solve(int n, int a, int[] all) {
    long count = 0;
    for (int b = -n; b <= n; b++) {
      for (int c = -n; c <= n; c++) {
        for (int d = -n; d <= n; d++) {
          int[] gs;
          if (c != 0) {
            if ((a - a * a - b * d) % c != 0) {
              continue;
            }
            int g = (a - a * a - b * d) / c;
            if (g < -n || g > n) {
              continue;
            }
            gs = new int[] {g};
          } else if (a - a*a - b*d == 0) {
            gs = all;
          } else {
            continue;
          }

          for (int g : gs) {
            for (int e = -n; e <= n; e++) {
              int[] hs;
              if (c != 0) {
                if ((b - a*b - b*e) % c != 0) {
                  continue;
                }
                int h = (b - a*b - b*e) / c;
                if (h < -n || h > n) {
                  continue;
                }
                hs = new int[] {h};
              } else if (b - a*b - b*e == 0) {
                hs = all;
              } else {
                continue;
              }

              int[] fs;
              if (g != 0) {
                if ((d - a*d - d*e) % g != 0) {
                  continue;
                }
                int f = (d - a*d - d*e) / g;
                if (f < -n || f > n) {
                  continue;
                }
                fs = new int[] {f};
              } else if (d - a*d - d*e == 0) {
                fs = all;
              } else {
                continue;
              }

              for (int f : fs) {
                for (int h : hs) {
                  if (b*d + e*e + h*f != e) {
                    continue;
                  }

                  int[] is;
                  if (c != 0) {
                    if ((c - a*c - b*f) % c != 0) {
                      continue;
                    }
                    int i = (c - a*c - b*f) / c;
                    if (i < -n || i > n) {
                      continue;
                    }
                    is = new int[] {i};
                  } else if (c - a*c - b*f == 0) {
                    is = all;
                  } else {
                    continue;
                  }

                  for (int i : is) {
                    if (c*d + f*e + i*f != f) {
                      continue;
                    }
                    if (a*g + d*h + g*i != g) {
                      continue;
                    }
                    if (g*b + h*e + i*h != h) {
                      continue;
                    }
                    if (c*g + f*h + i*i != i) {
                      continue;
                    }
                    count++;
                  }
                }
              }
            }
          }
        }
      }
    }
    System.out.println(a + ": " + count);
    return count;
  }
}
