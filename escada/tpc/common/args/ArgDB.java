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

import java.util.Vector;

public class ArgDB {
	private Vector argsList = new Vector(0);

	public ArgDB() {
	};

	public void add(Arg a) {
		argsList.addElement(a);
	}

	public final void parse(String[] args) throws Arg.Exception {
		int i;
		int a, a2;

		for (a = 0; a < args.length;) {
			a2 = a;
			for (i = 0; i < argsList.size(); i++) {
				a = ((Arg) argsList.elementAt(i)).parse(args, a);
				if (a2 != a)
					break;
			}
			if (i == argsList.size()) {
				Arg.Exception ex = new Arg.Exception("Unknown argument ("
						+ args[a] + ").", a);
				ex.start = a;
				throw (ex);
			}
		}

		String req = "";
		int numErr = 0;

		for (i = 0; i < argsList.size(); i++) {
			Arg e = ((Arg) argsList.elementAt(i));
			if (e.required() && !e.set()) {
				req = req + e.toString() + "\n";
				numErr++;
			}
		}

		if (numErr > 0) {
			Arg.Exception ex = new Arg.Exception("" + numErr
					+ " arguments not given.\n" + req, args.length - 1);
			ex.start = 0;
			throw ex;
		}
	}

	public void print() {
		int a;

		for (a = 0; a < argsList.size(); a++) {
			System.out.println("% " + argsList.elementAt(a));
		}
	}
}
// arch-tag: d256796e-d042-42b1-9045-f71762ea6327
