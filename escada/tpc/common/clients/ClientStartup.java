package Escada.tpc.common.clients;

import java.util.*;

import Escada.tpc.common.*;
import Escada.tpc.common.util.*;
import Escada.tpc.common.args.*;
import Escada.tpc.common.database.*;

public class ClientStartup {

  private double slowDown;
  private double speedUp;         
  private long start;
  private long term;

  public static void main(String args[]) {
      ClientStartup c = new ClientStartup(args);
  }
    
  public ClientStartup(String args[]) {
    Vector ebs = new Vector(0);
    ClientEmulation e = null;
    ArgDB db = new ArgDB();

    try {

    System.out.println("Remote Emulator for Database Benchmark ...");
    System.out.println("Universidade do Minho (Grupo de Sistemas Distribuidos)");
    System.out.println("\nVersion 0.1\n");

    StringArg ebArg =
      new StringArg("-EBclass", "EB Factory",
		       "% Factory <class> used to create EBs.",db);
    StringArg stArg = 
      new StringArg("-STclass", "State Machine for Emulation",
		       "% Defines the class used as a state machine for emulation.",db);

    StringArg dbArg =
      new StringArg("-DBclass", "DB Database Class",
		       "% String <class> used to instantiate the database.",db);

    StringArg driverArg =
      new StringArg("-DBdriver", "DBDriver Database Class",
		       "% String <class> which specifies the driver used to contact the database.",db);

    StringArg pathArg =
      new StringArg("-DBpath", "DBpath Database Connection Information",
		       "% String which specifies information used to connect to the database.",db);
    StringArg usrArg =
      new StringArg("-DBusr", "DBusr Database User",
		       "% String <usr> which specifies the user connecting to the database",db);

    StringArg passwdArg =
      new StringArg("-DBpasswd", "DBpasswd User password",
		       "% String <passwd> which specifies the password correpondent to user connecting to the database.",db);


    IntArg poolArg = new IntArg("-POOL", "Connection Pool",
	       "% The number of entries available for connection pool...", db);

    DateArg st =
      new DateArg("-ST", "Starting time for ramp-up",
		  "% Time (such as Nov 2, 1999 11:30:00 AM CST) " +
		  "at which to start ramp-up." +
		  "  Useful for synchronizing multiple RBEs.",
		  System.currentTimeMillis() + 2000L, db);

    IntArg ru = new IntArg("-RU", "Ramp-up time",
			   "% Seconds used to warm-up the simulator.", 10*60, db); 

    IntArg mi = new IntArg("-MI", "Measurement interval",
			   "% Minutes used for measuring SUT performance.", 30*60, db); 

    IntArg rd = new IntArg("-RD", "Ramp-down time",
	       "% Seconds of steady-state operation following " +
			   "measurment interval.", 5*60, db); 

    DoubleArg slow =
      new DoubleArg("-SLOW", "Slow-down factor",
		    "% 1000 means one thousand real seconds equals one " +
		    "simulated second.  " +
		    "Accepts factional values and E notation.", 1.0, db); 

    BooleanArg key =
		new BooleanArg("-KEY", "Interactive control.",
			       "% Require user to hit RETURN before every interaction.  Overrides think time.",
			       false, db);
    IntArg cli =
      new IntArg("-CLI", "Number of clients",
		 "% Number of clients concurrently accessing the database.", db);

    StringArg prefix =
      new StringArg("-PREFIX", "Emulation identification and also used as part of the emulation id",
	            "% It defines the compositon of the trace file identification and is also used as a component of the " +
                    "emulator id.",db);

    StringArg traceFlag =
      new StringArg("-TRACEflag", "trace files",
	            "% It defines the usage of trace file or not (NOTRACE,TRACE,TRACESTRING,TRACETIME)",db);

    IntArg fragArg = new IntArg("-FRAG", "Shift the clients...",
          "% It shift the clients in order to access different warehouses...",0,db);


    if (args.length==0) {
      Usage(args, db);
      return;
    }

    db.parse(args);

    Usage(args, db);

    CommonDatabase.setConnectionPool(true);
    CommonDatabase.setMaxConnection(poolArg.num);
    CommonDatabase.setDriverName(driverArg.s);
    CommonDatabase.setjdbcPath(pathArg.s);
    CommonDatabase.setUserInfo(usrArg.s,passwdArg.s);

    Emulation.setFinished(false);
    Emulation.setTraceInformation(prefix.s);
    Emulation.setNumberConcurrentEmulators(cli.num);

    System.out.println("\nThe simulation will be started shifting the clients by " + fragArg.num + "\n");

    if (traceFlag.equals("TRACETIME") ) dbLog.openFile();

    int i=0;
    for (i=0; i < cli.num; i++) {
	e = new ClientEmulation(ebArg.s,stArg.s,dbArg.s,cli.num,i,prefix.s,null,fragArg.num);
	e.setName(prefix.s + "-" + i);
	ebs.add(e);
	e.start();
    }

    System.out.println("Running simulation for " + mi.num + " minute(s).");

    waitForRampDown(0,mi.num); 

    Emulation.setFinished(true);
    
    for (i=0; i < cli.num; i++)
    {
      e = (ClientEmulation) ebs.elementAt(i);
      System.out.println("Waiting for the eb " + i + " to finish its job..");
      try
      {
	e.join();
      }
      catch (InterruptedException inte)
      {
	inte.printStackTrace();
	continue;
      }
    }

    System.out.println("EBs finished.");

    if (traceFlag.equals("TRACETIME") ) dbLog.closeFile();

    }
    catch (Arg.Exception ae) {
      System.out.println("Error:");
      System.out.println(ae);
      Usage(args, db);
      return;
    }
    catch (Exception ex) {
	System.out.println("Error: Invalid parameters.");
	System.exit(-1);
    }
  }

  private void Arguments(String args[]) {
    int a;

    for (a=0;a<args.length;a++) {
      System.out.println("#" + Pad.l(3,""+(a+1)) + "  " + args[a]);
    }
  }

  private void Usage(String args[], ArgDB db)
  {
    System.out.println("Input command-line arguments");
    Arguments(args);

    System.out.println("\nOptions");
    db.print(System.out);
  }
  private void waitForRampDown(int start,int term)
  {
         try {
	     waitForStart(start);
	     if (term < 0) return;
	     Thread.currentThread().sleep(term * 60 * 1000);
	 }
	 catch (InterruptedException ie) {
	     System.out.println("In waitforrampdown, caught interrupted exception");
	 }
  }

  private void waitForStart(int start)
	throws InterruptedException
  {
     if (start < 0) return;

     Thread.currentThread().sleep(start * 60 * 1000);
  }
 
}
// arch-tag: d7a75e9a-a418-4fae-877c-72938e7dadc9
