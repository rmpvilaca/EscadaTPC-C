package escada.tpc.common.args;

import java.util.Vector;

import org.apache.log4j.Logger;

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

	public void print(Logger out) {
		int a;

		for (a = 0; a < argsList.size(); a++) {
			out.info("% " + argsList.elementAt(a));
		}
	}
}
// arch-tag: d256796e-d042-42b1-9045-f71762ea6327
