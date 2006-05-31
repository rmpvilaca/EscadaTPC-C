package escada.tpc.tpcc.util;

/**
 * It defines important functions used to generate random strings as specified
 * in the TPC-C benchmark.
 */
public class TPCCRandGen {

	/**
	 * It defines a set of words that are combined to produce strings used in
	 * the TPC-C
	 */
	private static final String[] digS = { "BAR", "OUGHT", "ABLE", "PRI",
			"PRES", "ESE", "ANTI", "CALLY", "ATION", "EING" };

	public static String digSyl(int d, int n, int l) {
		StringBuffer s = new StringBuffer();
		int length = l - countLength(d);

		s.append(Integer.toString(d));
		for (; length > 0; length--) {
			s.append("0");
		}

		d = Integer.parseInt(s.toString());
		s = new StringBuffer();

		for (; n > 0; n--) {
			int c = d % 10;
			s.append(digS[c]);
			d = d / 10;
		}

		return (s.toString());
	}

	public static String digSyl(int d, int n) {
		return (digSyl(d, ((n == 0) ? countLength(d) : n), countLength(d)));
	}

	public static String digSyl(int d) {
		return (digSyl(d, 3, 3));
	}

	public static int countLength(int d) {
		int c = 0;

		for (; d > 0; d = d / 10, c++)
			;

		return (c);
	}
}// arch-tag: 7697fdd2-6cda-40c2-b600-6c26f96eddfe
// arch-tag: b8ae8fca-784f-4791-ab95-64f3b9ba8998
// arch-tag: c514e873-ee5c-4e3e-b918-328ce4260594
