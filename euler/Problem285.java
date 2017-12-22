package euler;

import com.google.auto.value.AutoValue;

public class Problem285 {

  public static void main(String[] args) {
    System.out.println(new Problem285().solve());
  }

  public String solve() {
    double sum = 0;
    for (int k = 1; k <= 100_000; k++) {
      sum += k * (area(k, 1 + 1D / (2 * k)) - (k > 1 ? area(k, 1 - 1D / (2 * k)) : 0));
    }
    return String.format("%.5f", sum);
  }

  private double area(double k, double radius) {
    Vector3 zero = Vector3.create(1D/k, 1D/k, 0);
    Vector3 v1 = Vector3.create(Math.sqrt(radius*radius - 1D/(k*k)), 1D/k, 0);
    double angle = Math.atan2(zero.y(), zero.x()) - Math.atan2(v1.y(), v1.x());
    return Math.pow(radius, 2) * angle - v1.crossProduct(zero).norm();
  }

  @AutoValue
  abstract static class Vector3 {
    abstract double x();
    abstract double y();
    abstract double z();

    static Vector3 create(double x, double y, double z) {
      return new AutoValue_Problem285_Vector3(x, y, z);
    }

    double norm() {
      return Math.sqrt(x() * x() + y() * y() + z() * z());
    }

    Vector3 crossProduct(Vector3 that) {
      return create(
          this.y() * that.z() - this.z() * that.y(),
          this.z() * that.x() - this.x() * that.z(),
          this.x() * that.y() - this.y() * that.x());
    }
  }
}
