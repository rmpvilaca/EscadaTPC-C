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

public class IntArg extends Arg {
	public int num = 0;

	public IntArg(String arg, String name, String desc) {
		super(arg, name, desc, false, false);
	}

	public IntArg(String arg, String name, String desc, ArgDB db) {
		super(arg, name, desc, false, false, db);
	}

	public IntArg(String arg, String name, String desc, int def) {
		super(arg, name, desc, false, true);
		num = def;
	}

	public IntArg(String arg, String name, String desc, int def, ArgDB db) {
		super(arg, name, desc, false, true, db);
		num = def;
	}

	// Customize to parse arguments.
	protected int parseMatch(String[] args, int a) throws Arg.Exception {
		if (a == args.length) {
			throw new Arg.Exception("Integer argument missing value.", a);
		}
		try {
			num = Integer.parseInt(args[a]);
		} catch (NumberFormatException nfe) {
			throw new Arg.Exception("Unable to parse integer value (" + args[a]
					+ ").", a);
		}

		return (a + 1);
	}

	public String value() {
		return ("" + num);
	}
}

