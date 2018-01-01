package euler;


public class LongModMatrix extends Matrix<LongMod, LongModMatrix> {

  private final Domain<LongMod> domain;

  private LongModMatrix(LongMod[][] m, Domain<LongMod> domain) {
    super(m);
    this.domain = domain;
  }

  public static LongModMatrix create(LongMod[][] m, long mod) {
    LongMod[][] copy = new LongMod[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = m[row].clone();
    }
    return new LongModMatrix(copy, Domain.longMods(mod));
  }

  public static LongModMatrix create(long[][] m, long mod) {
    LongMod[][] copy = new LongMod[m.length][];
    for (int row = 0; row < m.length; row++) {
      copy[row] = new LongMod[m[row].length];
      for (int column = 0; column < m[row].length; column++) {
        copy[row][column] = LongMod.create(m[row][column], mod);
      }
    }
    return new LongModMatrix(copy, Domain.longMods(mod));
  }

  @Override
  protected Domain<LongMod> domain() {
    return domain;
  }

  @Override
  protected LongModMatrix newInstance(LongMod[][] m) {
    return new LongModMatrix(m, domain);
  }
}
