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


package escada.tpc.common.args;

public class DoubleArg extends Arg {
	public double num = 0;

	public DoubleArg(String arg, String name, String desc) {
		super(arg, name, desc, false, false);
	}

	public DoubleArg(String arg, String name, String desc, ArgDB db) {
		super(arg, name, desc, false, false, db);
	}

	public DoubleArg(String arg, String name, String desc, double def) {
		super(arg, name, desc, false, true);
		num = def;
	}

	public DoubleArg(String arg, String name, String desc, double def, ArgDB db) {
		super(arg, name, desc, false, true, db);
		num = def;
	}

	// Customize to parse arguments.
	protected int parseMatch(String[] args, int a) throws Arg.Exception {
		if (a == args.length) {
			throw new Arg.Exception("Double argument missing value.", a);
		}
		try {
			num = Double.valueOf(args[a]).doubleValue();
		} catch (NumberFormatException nfe) {
			throw new Arg.Exception("Unable to parse double value (" + args[a]
					+ ").", a);
		}

		return (a + 1);
	}

	public String value() {
		return ("" + num);
	}
}
// arch-tag: 585adb58-df8d-4684-9a35-2d3c08756fbf
