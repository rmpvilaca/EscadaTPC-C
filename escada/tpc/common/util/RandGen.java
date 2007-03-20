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


package escada.tpc.common.util;

import java.util.Random;

public class RandGen {

	/**
	 * It defines a non-uniform distribution according to the specification of
	 * the TPC.
	 * 
	 * @param rand
	 *            the random generator
	 * @param A
	 *            it is a constant defined according to the range of x and y
	 * @param x
	 *            it is the first value of the possible range, including it
	 * @param y
	 *            it is the final value of the possible range, including it
	 * @return the calculated non-uniform number
	 */
	public static final int NURand(Random rand, int A, int x, int y) {
		return ((((nextInt(rand, A + 1)) | (nextInt(rand, y - x + 1) + x)) % (y
				- x + 1)) + x);
	}

	/**
	 * The exponetial distribution used by the TPC, for instance to calulate the
	 * transaction's thinktime.
	 * 
	 * @param rand
	 *            the random generator
	 * @param min
	 *            the minimum number which could be accepted for this
	 *            distribution
	 * @param max
	 *            the maximum number which could be accept for this distribution
	 * @param lMin
	 *            the minimum number which could be accept for the following
	 *            execution rand.nextDouble
	 * @param lMax
	 *            the maximum number which could be accept for the following
	 *            execution rand.nexDouble
	 * @param mu
	 *            the base value provided to calculate the exponetial number.
	 *            For instance, it could be the mean thinktime
	 * @return the caluclated exponetial number
	 */
	public static final long negExp(Random rand, long min, double lMin,
			long max, double lMax, double mu) {
		double r = rand.nextDouble();

		if (r < lMax) {
			return (max);
		}
		return ((long) (-mu * Math.log(r)));

	}

	/**
	 * It return a uniform random number, from 0 to (range - 1).
	 * 
	 * @param rand
	 *            the random generator
	 * @param int
	 *            the range
	 * @return the uniform random number
	 */
	public static int nextInt(Random rand, int range) {
		int i = Math.abs(rand.nextInt());
		return (i % (range));
	}

	/**
	 * It return a uniform random number, from inirange to (endrange - 1).
	 * 
	 * @param rand
	 *            the random generator
	 * @param inirange
	 *            the start of the range
	 * @param endrange
	 *            the end of the range
	 * @return the uniform random number
	 */
	public static int nextInt(Random rand, int inirange, int endrange) {
		return (inirange + nextInt(rand, endrange - inirange));
	}
}
// arch-tag: 77964eb3-28ea-4619-9a43-2cd5ac43e3b5
