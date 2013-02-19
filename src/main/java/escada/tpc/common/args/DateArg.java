/*
 * Copyright 2013 Universidade do Minho
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software   distributed under the License is distributed on an "AS IS" BASIS,   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and limitations under the License.
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

