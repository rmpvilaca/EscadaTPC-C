package escada.tpc.tpcc.util;

/**
* It defines important functions used to generate random strings
* as specified in the TPC-C benchmark.
**/
public class TPCCRandGen {

  /** It defines a set of words that are combined to produce strings
  * used in the TPC-C
  **/
  private static final String[] digS = {
      "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE", "ANTI", "CALLY", "ATION",
      "EING"
  };

  public static String digSyl(int d, int n) {
    String s = "";

    if (n == 0) {
      return (digSyl(d));
    }

    for (; n > 0; n--) {
      int c = d % 10;
      s = digS[c] + s;
      d = d / 10;
    }

    return (s);
  }

  public static String digSyl(int d) {
    String s = "";

    for (; d != 0; d = d / 10) {
      int c = d % 10;
      s = digS[c] + s;
    }

    return (s);
  }
}// arch-tag: 7697fdd2-6cda-40c2-b600-6c26f96eddfe
// arch-tag: b8ae8fca-784f-4791-ab95-64f3b9ba8998
// arch-tag: c514e873-ee5c-4e3e-b918-328ce4260594
