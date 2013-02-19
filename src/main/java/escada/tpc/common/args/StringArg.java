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
