package escada.tpc.common.clients.jmx;

import java.lang.management.ManagementFactory;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import escada.tpc.common.Emulation;
import escada.tpc.common.args.Arg;
import escada.tpc.common.args.ArgDB;
import escada.tpc.common.args.BooleanArg;
import escada.tpc.common.args.DateArg;
import escada.tpc.common.args.DoubleArg;
import escada.tpc.common.args.IntArg;
import escada.tpc.common.args.StringArg;
import escada.tpc.common.clients.ClientEmulation;
import escada.tpc.common.database.DatabaseManager;
import escada.tpc.common.util.Pad;

public class ClientEmulationStartup implements ClientEmulationStartupMBean {
	private final ExecutorService executor = Executors.newFixedThreadPool(2);	
	private Stage stage = Stage.INIT;
	
	private final static Logger logger = Logger.getLogger(ClientEmulationStartup.class);
	
	private final Vector<ClientEmulation> ebs = new Vector<ClientEmulation>(0);
	
	private synchronized void init() {
		try {
			// register jmx bean
			ObjectName name = new ObjectName("escada.tpc.common.clients.jmx:type=ClientEmulationStartup");
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, name);
			
			// wait for start
			while ( !this.stage.equals(Stage.STOPPED) )
				this.wait();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}
	
	public void start(String arg) throws InvalidTransactionException {
		if ( this.stage.equals(Stage.INIT) ) {
			this.setStage(Stage.RUNNING);
			
			String[] args = arg.split("[ ]+");
			this.executor.submit(new Start(args));
		}
		else
			throw new InvalidTransactionException("start on "+this.stage);
	}
	
	class Start implements Runnable {
		private String[] args;
		
		public Start(String[] args) {
			this.args = args;
		}
		
		public void run() {
			startClientEmulation(this.args);
		}
	}
	
	public void pause() throws InvalidTransactionException {
		if ( this.stage.equals(Stage.RUNNING) ) {
			this.setStage(Stage.PAUSED);
			
			for ( ClientEmulation e:ebs ) {
				e.pause();
			}
		}
		else
			throw new InvalidTransactionException("pause on "+this.stage);
	}
	
	public void unpause() throws InvalidTransactionException {
		if ( this.stage.equals(Stage.PAUSED) ) {
			this.setStage(Stage.RUNNING);
			
			for ( ClientEmulation e:ebs ) {
				e.unpause();
			}
		}
		else
			throw new InvalidTransactionException("unpause on "+this.stage);
	}

	public void stop() throws InvalidTransactionException {
		if ( this.stage.equals(Stage.RUNNING) || this.stage.equals(Stage.PAUSED) ) {
			this.setStage(Stage.STOPPED);
			
			for ( ClientEmulation e:ebs ) {
				e.stopit();
			}
			
			this.executor.submit(new Kill());
		}
		else
			throw new InvalidTransactionException("stop on "+this.stage);
	}

	public void kill() {
		this.executor.submit(new Kill());
	}

	class Kill implements Runnable {
		public void run() {
			System.exit(0);
		}
	}

	private synchronized void setStage(Stage stage) {
		logger.debug("setStage("+stage+")");
		this.stage = stage;
		this.notify();
	}
	
	public static void main(String[] args) {
		ClientEmulationStartup ces = new ClientEmulationStartup();
		ces.init();
	}
	
	private void startClientEmulation(String[] args) {
		ClientEmulation e = null;
		ArgDB db = new ArgDB();

		try {

			StringArg log4jArg = new StringArg("-LOGconfig",
					"Configuration file for Log4J.",
					"% Defines the logging output.", db);

			StringArg ebArg = new StringArg("-EBclass", "EB Factory",
					"% Factory <class> used to create EBs.", db);
			StringArg stArg = new StringArg(
					"-STclass",
					"State Machine for Emulation",
					"% Defines the class used as a state machine for emulation.",
					db);

			StringArg dbArg = new StringArg("-DBclass", "DB Database Class",
					"% String <class> used to instantiate the database.", db);

			StringArg driverArg = new StringArg(
					"-DBdriver",
					"DBDriver Database Class",
					"% String <class> which specifies the driver used to contact the database.",
					db);

			StringArg pathArg = new StringArg(
					"-DBpath",
					"DBpath Database Connection Information",
					"% String which specifies information used to connect to the database.",
					db);
			StringArg usrArg = new StringArg(
					"-DBusr",
					"DBusr Database User",
					"% String <usr> which specifies the user connecting to the database",
					db);

			StringArg passwdArg = new StringArg(
					"-DBpasswd",
					"DBpasswd User password",
					"% String <passwd> which specifies the password correpondent to user connecting to the database.",
					db);

			IntArg poolArg = new IntArg("-POOL", "Connection Pool",
					"% The number of entries available for connection pool...",
					db);

			DateArg st = new DateArg("-ST", "Starting time for ramp-up",
					"% Time (such as Nov 2, 1999 11:30:00 AM CST) "
							+ "at which to start ramp-up."
							+ "  Useful for synchronizing multiple RBEs.",
					System.currentTimeMillis() + 2000L, db);

			IntArg ru = new IntArg("-RU", "Ramp-up time",
					"% Seconds used to warm-up the simulator.", 10 * 60, db);

			IntArg mi = new IntArg("-MI", "Measurement interval",
					"% Minutes used for measuring SUT performance.", 30 * 60,
					db);

			IntArg rd = new IntArg("-RD", "Ramp-down time",
					"% Seconds of steady-state operation following "
							+ "measurment interval.", 5 * 60, db);

			DoubleArg slow = new DoubleArg("-SLOW", "Slow-down factor",
					"% 1000 means one thousand real seconds equals one "
							+ "simulated second.  "
							+ "Accepts factional values and E notation.", 1.0,
					db);

			BooleanArg key = new BooleanArg("-KEY", "Enable thinktime.",
					"% It enables or disables the think time.", true, db);

			IntArg cli = new IntArg("-CLI", "Number of clients",
					"% Number of clients concurrently accessing the database.",
					db);

			StringArg prefix = new StringArg(
					"-PREFIX",
					"Emulation identification and also used as part of the emulation id",
					"% It defines the compositon of the trace file identification and is also used as a component of the "
							+ "emulator id.", db);

			StringArg traceFlag = new StringArg(
					"-TRACEflag",
					"trace files",
					"% It defines the usage of trace file or not (NOTRACE,TRACE,TRACESTRING,TRACETIME)",
					db);

			IntArg fragArg = new IntArg(
					"-FRAG",
					"Shift the clients...",
					"% It shifts the clients in order to access different warehouses...",
					1, db);

			BooleanArg resArg = new BooleanArg(
					"-RESUBMIT",
					"Resubmit Transaction.",
					"% It enables the transaction resubmition when an error occurs.",
					true, db);

			IntArg hostArg = new IntArg("-HOST", "Connection Pool",
					"% The number of entries available for connection pool...",
					0, db);

			if (args.length == 0) {
				Usage(args, db);
				return;
			}

			db.parse(args);

			DOMConfigurator.configure(log4jArg.s);
			logger.info("Starting up the client application.");
			logger.info("Remote Emulator for Database Benchmark ...");
			logger
					.info("Universidade do Minho (Grupo de Sistemas Distribuidos)");
			logger.info("Version 0.1");

			Usage(args, db);

			DatabaseManager.setConnectionPool(true);
			DatabaseManager.setMaxConnection(poolArg.num);
			DatabaseManager.setDriverName(driverArg.s);
			DatabaseManager.setjdbcPath(pathArg.s);
			DatabaseManager.setUserInfo(usrArg.s, passwdArg.s);

			Emulation.setFinished(false);
			Emulation.setTraceInformation(prefix.s);
			Emulation.setNumberConcurrentEmulators(cli.num);
			Emulation.setStatusThinkTime(key.flag);
			Emulation.setStatusReSubmit(resArg.flag);

			int i = 0;
			for (i = 0; i < cli.num; i++) {
				e = new ClientEmulation(ebArg.s, stArg.s, dbArg.s, cli.num, i,
						prefix.s, Integer.toString(hostArg.num), fragArg.num);
				e.setName(prefix.s + "-" + i);
				ebs.add(e);
				e.start();
			}

			logger.info("Running simulation for " + mi.num + " minute(s).");

			waitForRampDown(0, mi.num);

			Emulation.setFinished(true);
			
			for (i = 0; i < cli.num; i++) {
				e = (ClientEmulation) ebs.elementAt(i);
				logger.info("Waiting for the eb " + i + " to finish its job..");
				try {
					e.join();
				} catch (InterruptedException inte) {
					inte.printStackTrace();
					continue;
				}
			}

			logger.info("EBs finished.");
			System.exit(0);

		} catch (Arg.Exception ae) {
			logger.info("Error:");
			logger.info(ae);
			Usage(args, db);
			return;
		} catch (Exception ex) {
			logger.fatal("Error: Invalid parameters.");
			System.exit(-1);
		}
	}

	private void Arguments(String args[]) {
		int a;

		for (a = 0; a < args.length; a++) {
			System.out.println("#" + Pad.l(3, "" + (a + 1)) + "  " + args[a]);
		}
	}

	private void Usage(String args[], ArgDB db) {
		logger.info("Input command-line arguments");
		Arguments(args);
		logger.info("Options");
		db.print();
	}

	private void waitForRampDown(int start, int term) {
		try {
			waitForStart(start);
			if (term < 0)
				return;
			Thread.sleep(term * 60 * 1000); // TODO: It must be changed to a
			// constant.
		} catch (InterruptedException ie) {
			logger.error("In waitforrampdown, caught interrupted exception");
		}
	}

	private void waitForStart(int start) throws InterruptedException {
		if (start < 0)
			return;

		Thread.sleep(start * 60 * 1000); // TODO - It must be changed to a
		// constant.
	} 
}
