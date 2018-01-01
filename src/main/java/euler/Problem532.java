package euler;

import com.google.auto.value.AutoValue;

public class Problem532 {

  public static void main(String[] args) {
    System.out.println(new Problem532().solve());
  }

  public String solve() {
    int n = 827;
    double phi = Math.PI * 2 / n;
    double phiCos = Math.cos(phi);
    double phiSin = Math.sin(phi);
    double stepAngle = 0.0000001D;
    double stepAngleCos = Math.cos(stepAngle);
    double stepAngleSin = Math.sin(stepAngle);
    Vector3 v1 = Vector3.create(0.999D, 0D, Math.sqrt(1 - 0.999D * 0.999D));
    long steps = 0;
    while (true) {
      double x2 = v1.x() * phiCos - v1.y() * phiSin;
      double y2 = v1.x() * phiSin + v1.y() * phiCos;
      double z2 = Math.sqrt(1 - x2 * x2 - y2 * y2);
      Vector3 v2 = Vector3.create(x2, y2, z2);
      if (v1.angle(v2) < stepAngle) {
        break;
      }
      Vector3 cross = v1.crossProduct(v2).normalized();
      Vector3 newV1 = v1.multiply(stepAngleCos)
          .add(cross.crossProduct(v1).multiply(stepAngleSin))
          .add(cross.multiply((1 - stepAngleCos) * (cross.dotProduct(v1)))).normalized();
      if ((steps & 0xFFFFF) == 0) {
        System.out.println(Vector3.create(newV1.x(), newV1.y(), 0).norm() + " " + newV1 + " " + newV1.norm());
      }
      steps++;
      v1 = newV1;
    }
    System.out.println(String.format("%.2f", steps * stepAngle));
    return String.format("%.2f", steps * stepAngle * n);
  }

  @AutoValue
  abstract static class Vector3 {
    abstract double x();
    abstract double y();
    abstract double z();

    static Vector3 create(double x, double y, double z) {
      return new AutoValue_Problem532_Vector3(x, y, z);
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
