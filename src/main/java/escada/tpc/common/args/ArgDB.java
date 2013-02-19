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
