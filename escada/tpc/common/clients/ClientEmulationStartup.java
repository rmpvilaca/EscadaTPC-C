/*
 * TPCC Client
 * Copyright (C) 2006 University of Minho
 * See http://gorda.di.uminho.pt/ for more information.
 *
 * Partially funded by the European Union Framework Programme for
 * Research and Technological Development, thematic priority
 * Information Society and Media, project GORDA (004758).
 * 
 * Contributors:
 *  - Rui Oliveira <rco@di.uminho.pt>
 *  - Jose Orlando Pereira <jop@di.uminho.pt>
 *  - Antonio Luis Sousa <als@di.uminho.pt>
 *  - Alfranio Tavares Correia Junior <alfranio@lsd.di.uminho.pt> 
 *  - Luis Soares <los@di.uminho.pt>
 *  - Ricardo Manuel Pereira Vilaca <rmvilaca@di.uminho.pt>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */


package escada.tpc.common.clients;

import java.util.Vector;

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
import escada.tpc.common.database.DatabaseManager;
import escada.tpc.common.util.Pad;

public class ClientEmulationStartup {

	private double slowDown;

	private double speedUp;

	private long start;

	private long term;

	private static Logger logger = Logger
			.getLogger(ClientEmulationStartup.class);

	public static void main(String args[]) {
		try {
			ClientEmulationStartup c = new ClientEmulationStartup(args);
		} catch (Exception ex) {
			Thread.dumpStack();
			System.exit(-1);
		}
	}

	public ClientEmulationStartup(String args[]) {
		Vector ebs = new Vector(0);
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
// arch-tag: d7a75e9a-a418-4fae-877c-72938e7dadc9
