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
