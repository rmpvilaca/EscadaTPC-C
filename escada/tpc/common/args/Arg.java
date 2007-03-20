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

import escada.tpc.common.util.Pad;

public abstract class Arg {
	private String arg; // Command line -arg name.

	private String name; // Descriptive name.

	private String desc; // Description.

	protected boolean set; // Whether this argument was set.

	protected boolean req; // This argument must be set.

	protected boolean def; // This argument has a default value.

	public Arg(String arg, String name, String desc, boolean req, boolean def,
			ArgDB db) {
		init(arg, name, desc, req, def);
		db.add(this);
	}

	public Arg(String arg, String name, String desc, boolean req, boolean def) {
		init(arg, name, desc, req, def);
	}

	private void init(String arg, String name, String desc, boolean req,
			boolean def) {
		this.arg = arg.toUpperCase();
		this.name = name;
		this.desc = desc;
		this.req = req;
		this.def = def;
		set = false;
	}

	public final boolean set() {
		return (set);
	};

	public final boolean required() {
		return (req);
	};

	public final int parse(String[] args, int a) throws Arg.Exception {
		if (arg.equals(args[a].toUpperCase())) {
			set = true;
			try {
				return (parseMatch(args, a + 1));
			} catch (Arg.Exception ex) {
				ex.start = a;
				throw (ex);
			}
		} else {
			return (a);
		}
	}

	public String toString() {
		String v;
		if (set) {
			v = value();
		} else {
			if (req) {
				v = "required";
			} else if (def) {
				v = value() + " (default)";
			} else {
				v = "unset";
			}
		}
		return (Pad.l(8, arg) + " " + Pad.l(25, name) + " " + Pad.l(20, v)
				+ "\n         " + desc);
	}

	protected abstract int parseMatch(String[] args, int a)
			throws Arg.Exception;

	protected abstract String value();

	public static class Exception extends java.lang.Exception {
		public int start, end;

		public Exception(String message, int a) {
			super(message);
			this.end = a;
		}

		public String getMessage() {
			return (super.getMessage() + " arguments(" + (start + 1) + " to "
					+ (end + 1) + ")");
		}
	}
}
// arch-tag: c66f4c9c-6b49-4e0f-8066-8b3ebc4faee5
