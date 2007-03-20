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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateArg extends Arg {
	public Date d;

	protected DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,
			DateFormat.LONG);

	public DateArg(String arg, String name, String desc) {
		super(arg, name, desc, false, false);
	}

	public DateArg(String arg, String name, String desc, ArgDB db) {
		super(arg, name, desc, false, false, db);
	}

	// NOTE: Due to the vulgarities of Java, the current time is
	// used as the default time, even if defNow is false.
	public DateArg(String arg, String name, String desc, boolean defNow) {
		super(arg, name, desc, false, true);
		d = new Date(System.currentTimeMillis());
	}

	// NOTE: Due to the vulgarities of Java, the current time is
	// used as the default time, even if defNow is false.
	public DateArg(String arg, String name, String desc, boolean defNow,
			ArgDB db) {
		super(arg, name, desc, false, true, db);
		d = new Date(System.currentTimeMillis());
	}

	public DateArg(String arg, String name, String desc, long def, ArgDB db) {
		super(arg, name, desc, false, true, db);
		d = new Date(def);
	}

	// Customize to parse arguments.
	protected int parseMatch(String[] args, int a) throws Arg.Exception {
		if (a == args.length) {
			throw new Arg.Exception("Date argument missing time.", a);
		}
		try {
			df.setLenient(true);
			d = df.parse(args[a]);
		} catch (ParseException ex) {
			throw new Arg.Exception("Unable to parse date (" + args[a] + ").",
					a);
		}

		return (a + 1);
	}

	public String value() {
		return (df.format(d));
	}
}

// arch-tag: a2431b18-47b8-4f52-9812-a01c51379547
