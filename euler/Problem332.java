package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;

public class Problem332 {

  public static void main(String[] args) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    System.out.println(new Problem332().solve());
    System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public String solve() {
    double sum = IntStream.rangeClosed(1, 50).parallel().mapToDouble(r -> minArea(r)).sum();
    return String.format("%.6f", sum);
  }

  private double minArea(int r) {
    List<Vector3> Z = new ArrayList<>();
    for (int x = 0; x <= r; x++) {
      for (int y = -r; y <= r; y++) {
        for (int z = -r; z <= r; z++) {
          if (x * x + y * y + z * z == r * r) {
            Z.add(Vector3.create(x, y, z));
          }
        }
      }
    }
    double res = Double.MAX_VALUE;
    for (int a = 0; a < Z.size(); a++) {
      for (int b = a + 1; b < Z.size(); b++) {
        for (int c = b + 1; c < Z.size(); c++) {
          double area = minArea(r, Z.get(a), Z.get(b), Z.get(c));
          if (area < res && area > 1e-2) {
            res = area;
          }
        }
      }
    }
    return res;
  }

  private double minArea(double r, Vector3 A, Vector3 B, Vector3 C) {
    Vector3 CM = A.add(B).multiply(r * r / (r * r + A.dotProduct(B)));
    Vector3 AM = B.add(C).multiply(r * r / (r * r + B.dotProduct(C)));
    Vector3 BM = C.add(A).multiply(r * r / (r * r + C.dotProduct(A)));
    return area(r, CM.subtract(A), CM.subtract(B), AM.subtract(B), AM.subtract(C), BM.subtract(C), BM.subtract(A));
  }

  private double area(double r, Vector3 AB, Vector3 BA, Vector3 BC, Vector3 CB, Vector3 CA, Vector3 AC) {
    return area(r,
        Math.acos(AB.dotProduct(AC) / (AB.norm() * AC.norm())),
        Math.acos(BA.dotProduct(BC) / (BA.norm() * BC.norm())),
        Math.acos(CB.dotProduct(CA) / (CB.norm() * CA.norm())));
  }

  private double area(double r, double a, double b, double c) {
    return r * r * (a + b + c - Math.PI);
  }

  @AutoValue
  abstract static class Vector3 {
    abstract double x();
    abstract double y();
    abstract double z();

    static Vector3 create(double x, double y, double z) {
      return new AutoValue_Problem332_Vector3(x, y, z);
    }

    Vector3 add(Vector3 that) {
      return create(this.x() + that.x(), this.y() + that.y(), this.z() + that.z());
    }

    Vector3 subtract(Vector3 that) {
      return create(this.x() - that.x(), this.y() - that.y(), this.z() - that.z());
    }

    Vector3 multiply(double d) {
      return create(this.x() * d, this.y() * d, this.z() * d);
    }

    double dotProduct(Vector3 that) {
      return this.x() * that.x() + this.y() * that.y() + this.z() * that.z();
    }

    Vector3 negate() {
      return create(-this.x(), -this.y(), -this.z());
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

    Vector3 normalized() {
      double norm = norm();
      return create(this.x() / norm, this.y() / norm, this.z() / norm);
    }

    double angle(Vector3 that) {
      return Math.acos(this.dotProduct(that));
    }

    double distance(Vector3 that) {
      return this.subtract(that).norm();
    }
  }
}
