package Escada.tpc.common.args;

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

  public StringArg(String arg, String name, String desc, String def, 
						 ArgDB db) {
	 super(arg, name, desc, false, true, db);
	 s = def;
  }

  protected int parseMatch(String [] args, int a)
		 throws Arg.Exception
  {
	 if (a == args.length) {
		throw new Arg.Exception("String argument missing.", a);
	 }

	 s = args[a];
	 return(a+1);
  }
	
  public String value() { return(s); }
}
// arch-tag: 9c2ea511-10f5-4076-aa5e-4c4c68f0d4df
