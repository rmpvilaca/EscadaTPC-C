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

import java.io.FileOutputStream;
import java.io.PrintStream;

public class PrintStreamArg extends Arg {
	public PrintStream s = null;

	public String fName = null;

	public PrintStreamArg(String arg, String name, String desc) {
		super(arg, name, desc, true, false);
	}

	public PrintStreamArg(String arg, String name, String desc, ArgDB db) {
		super(arg, name, desc, true, false, db);
	}

	// Customize to parse arguments.
	protected int parseMatch(String[] args, int a) throws Arg.Exception {
		if (a == args.length) {
			throw new Arg.Exception("File name missing.", a);
		}

		PrintStream oldS = s;
		try {
			s = new PrintStream(new FileOutputStream(args[a]));
			fName = args[a];
			if (oldS != null)
				oldS.close();
		} catch (java.io.IOException fnf) {
			throw new Arg.Exception("Unable to open file " + args[a] + ".", a);
		}

		return (a + 1);
	}

	public String value() {
		return (fName);
	}
}
// arch-tag: 761bd3e7-a33d-4700-969a-86235bcb6957
