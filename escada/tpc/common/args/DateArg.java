package escada.tpc.common.args;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateArg extends Arg
{
   public Date d;

   protected DateFormat df =
	 DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

   public DateArg(String arg, String name, String desc)
   {
      super(arg, name, desc, false, false);
   }

   public DateArg(String arg, String name, String desc, ArgDB db)
   {
      super(arg, name, desc, false, false, db);
   }

   // NOTE:  Due to the vulgarities of Java, the current time is
   //  used as the default time, even if defNow is false.
   public DateArg(String arg, String name, String desc, boolean defNow)
   {
      super(arg, name, desc, false, true);
      d = new Date(System.currentTimeMillis());
   }

   // NOTE:  Due to the vulgarities of Java, the current time is
   //  used as the default time, even if defNow is false.
   public DateArg(String arg, String name, String desc, boolean defNow,
		  ArgDB db)
   {
      super(arg, name, desc, false, true, db);
      d = new Date(System.currentTimeMillis());
   }

   public DateArg(String arg, String name, String desc, long def,
		  ArgDB db)
   {
      super(arg, name, desc, false, true, db);
      d = new Date(def);
   }

   // Customize to parse arguments.
   protected int parseMatch(String [] args, int a)
   throws Arg.Exception
   {
      if (a == args.length)
      {
	 throw new Arg.Exception("Date argument missing time.", a);
      }
      try
      {
	 df.setLenient(true);
	 d = df.parse(args[a]);
      }
      catch(ParseException ex)
      {
	 throw new Arg.Exception("Unable to parse date (" +
				  args[a] + ").", a);
      }

      return(a+1);
   }

   public String value() { return(df.format(d)); }
}

// arch-tag: a2431b18-47b8-4f52-9812-a01c51379547
