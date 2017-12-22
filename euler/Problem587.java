package euler;

import com.google.auto.value.AutoValue;

import static euler.Runner.run;

public class Problem587 {

  public static void main(String[] args) {
    run(new Problem587()::solve);
  }

  public double solve() {
    for (double n = 1; true; n++) {
      double ratio = ratio(n);
      if (ratio < 0.001) {
        return n;
      }
    }
  }

  private double ratio(double n) {
    double x=n / (n + 1 + Math.sqrt(2 * n));
    double y=x/n;

    double angle = Vector3.create(0, -1, 0).angle(Vector3.create(x - 1, y - 1, 0));
    double arcArea = angle / 2;
    double upperArea = Vector3.create(0, 1 / n - 1, 0).area(Vector3.create(x - 1, y - 1, 0));
    double concaveTriangleArea = 1 / (2 * n) - (arcArea - upperArea);
    double lSectionArea = 1 - Math.PI / 4;
    return concaveTriangleArea / lSectionArea;
  }

  @AutoValue
  abstract static class Vector3 {
    abstract double x();
    abstract double y();
    abstract double z();

    static Vector3 create(double x, double y, double z) {
      return new AutoValue_Problem587_Vector3(x, y, z);
    }

    double dotProduct(Vector3 that) {
      return this.x() * that.x() + this.y() * that.y() + this.z() * that.z();
    }

    double norm() {
      return Math.sqrt(this.dotProduct(this));
    }

    Vector3 crossProduct(Vector3 that) {
      return create(
          this.y() * that.z() - this.z() * that.y(),
          this.z() * that.x() - this.x() * that.z(),
          this.x() * that.y() - this.y() * that.x());
    }

    double angle(Vector3 that) {
      return Math.acos(this.dotProduct(that));
    }

    double area(Vector3 that) {
      return this.crossProduct(that).norm() / 2;
    }
  }
}
