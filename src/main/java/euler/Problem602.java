package euler;

public class Problem602 {
  public static void main(String[] args) {
    Runner.run(new Problem602()::solve);
  }

  // e(e,p) = (1-p)*0 + p*(1-p)*((1-p)^3*1 + (1 - (1-p)^3)*0) + p^2*(1-p)*((1-p)^6*2*2*2 + (1-p)^5*p*2*2*2*1)

  public long solve() {
    return 0;
  }
}
