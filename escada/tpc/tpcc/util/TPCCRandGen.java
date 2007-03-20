/*
 * TPCC Client
 * Copyright (C) 2006 University of Minho
 * See http://gorda.di.uminho.pt/ for more information.
 *
 * Partially funded by the European Union Framework Programme for
 * Research and Technological Development, thematic priority
 * Information Society and Media, project GORDA (004758).
 * 
 * Contributors:
 *  - Rui Oliveira <rco@di.uminho.pt>
 *  - Jose Orlando Pereira <jop@di.uminho.pt>
 *  - Antonio Luis Sousa <als@di.uminho.pt>
 *  - Alfranio Tavares Correia Junior <alfranio@lsd.di.uminho.pt> 
 *  - Luis Soares <los@di.uminho.pt>
 *  - Ricardo Manuel Pereira Vilaca <rmvilaca@di.uminho.pt>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */


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
}

// arch-tag: 7697fdd2-6cda-40c2-b600-6c26f96eddfe
// arch-tag: b8ae8fca-784f-4791-ab95-64f3b9ba8998
// arch-tag: c514e873-ee5c-4e3e-b918-328ce4260594
