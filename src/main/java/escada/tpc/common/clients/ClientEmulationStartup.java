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

package escada.tpc.common.clients;

import escada.tpc.common.args.*;
import escada.tpc.common.database.DatabaseManager;
import escada.tpc.common.util.Pad;
import escada.tpc.logger.PerformanceLogger;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.lang.reflect.Constructor;
import java.util.Vector;

public class ClientEmulationStartup implements ClientEmulationMaster {

	private double slowDown;

	private double speedUp;

	private long start;

	private long term;

	private static Logger logger = Logger
			.getLogger(ClientEmulationStartup.class);



	public ClientEmulationStartup(String args[]) {
		Vector<ClientEmulation> ebs = new Vector<ClientEmulation>(0);
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

			IntArg hostArg = new IntArg("-HOST", "Information on host...",
					"% Information on host...",
					0, db);
			
			BooleanArg isConnectionPoolEnabled = new BooleanArg("-enabledPOOL", "Enable POOL.",
					"% It enables or disables the connection pool.", true, db);

			if (args.length == 0) {
				Usage(args, db);
				return;
			}

			db.parse(args);

			DOMConfigurator.configure(log4jArg.s);

			logger.info("Starting up the client application.");
			logger
					.info("Universidade do Minho (Grupo de Sistemas Distribuidos)");
			logger.info("Version 0.1");

			Usage(args, db);
            Thread.sleep(5000);
			DatabaseManager dbManager = null;

			Class cl = null;
			Constructor co = null;
			cl = Class.forName(dbArg.s);
			try {
				co = cl.getConstructor(new Class[] { Integer.TYPE });
			} catch (Exception ex) {
			}
			if (co == null) {
				dbManager = (DatabaseManager) cl.newInstance();
			} else {
				dbManager = (DatabaseManager) co
						.newInstance(new Object[] { new Integer(cli.num) });
			}
            PerformanceLogger.setPrintWriter("TPCC-"+prefix.s+"-time-"+mi.num+"-clients-"+cli.num+"-frag-"+fragArg.num+"-think-"+key.flag+".dat");

			dbManager.setConnectionPool(isConnectionPoolEnabled.flag);
			dbManager.setMaxConnection(poolArg.num);
			dbManager.setDriverName(driverArg.s);
			dbManager.setjdbcPath(pathArg.s);
			dbManager.setUserInfo(usrArg.s, passwdArg.s);

			int i = 0;
			for (i = 0; i < cli.num; i++) {

				e = new ClientEmulation();

				e.setFinished(false);
				e.setTraceInformation(prefix.s);
				e.setNumberConcurrentEmulators(cli.num);
				e.setStatusThinkTime(key.flag);
				e.setStatusReSubmit(resArg.flag);
				e.setDatabase(dbManager);
				e.setEmulationName(prefix.s);
				e.setHostId(Integer.toString(hostArg.num));

				e.create(ebArg.s, stArg.s, i, fragArg.num, this, null);

				Thread t = new Thread(e);
				t.setName(prefix.s + "-" + i);
				e.setThread(t);
				t.start();

				ebs.add(e);
			}

			logger.info("Running simulation for " + mi.num + " minute(s).");

			waitForRampDown(0, mi.num);

			for (i = 0; i < cli.num; i++) {
				e = (ClientEmulation) ebs.elementAt(i);
				logger.info("Waiting for the eb " + i + " to finish its job..");
				try {
					e.setCompletion(true);
					e.getThread().join();
				} catch (InterruptedException inte) {
					inte.printStackTrace();
					continue;
				}
			}



		} catch (Arg.Exception ae) {
			logger.info("Error:");
			logger.info(ae);
			Usage(args, db);
			return;
		} catch (Exception ex) {
            ex.printStackTrace();
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
			if (term < 0) {
				return;
			}
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

	public synchronized void notifyThreadsCompletion(String key) {
		notifyAll();
	}

	public synchronized void notifyThreadsError(String key) {
		notifyAll();
	}
}
