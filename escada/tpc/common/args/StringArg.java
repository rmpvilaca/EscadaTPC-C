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

public class StringArg extends Arg {
	public String s = null;

	public StringArg(String arg, String name, String desc) {
		super(arg, name, desc, true, false);
	}

	public StringArg(String arg, String name, String desc, ArgDB db) {
		super(arg, name, desc, false, true, db);
	}

	public StringArg(String arg, String name, String desc, String def) {
		super(arg, name, desc, false, true);
		s = def;
	}

	public StringArg(String arg, String name, String desc, String def, ArgDB db) {
		super(arg, name, desc, false, true, db);
		s = def;
	}

	protected int parseMatch(String[] args, int a) throws Arg.Exception {
		if (a == args.length) {
			throw new Arg.Exception("String argument missing.", a);
		}

		s = args[a];
		return (a + 1);
	}

	public String value() {
		return (s);
	}
}
// arch-tag: 9c2ea511-10f5-4076-aa5e-4c4c68f0d4df
