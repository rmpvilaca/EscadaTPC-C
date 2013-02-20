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

package escada.tpc.common.clients.jmx;

import escada.tpc.common.PerformanceCounters;
import escada.tpc.common.args.*;
import escada.tpc.common.clients.ClientEmulation;
import escada.tpc.common.clients.ClientEmulationMaster;
import escada.tpc.common.database.DatabaseManager;
import escada.tpc.common.resources.DatabaseResources;
import escada.tpc.common.resources.WorkloadResources;
import escada.tpc.common.util.Pad;
import escada.tpc.logger.PerformanceLogger;
import escada.tpc.tpcc.database.populate.jmx.DatabasePopulate;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ClientEmulationStartup implements ClientEmulationStartupMBean,
        ClientEmulationMaster {

    private final static Logger logger = Logger
            .getLogger(ClientEmulationStartup.class);

    private ExecutorService executor = Executors.newCachedThreadPool();

    private ScheduledExecutorService scheduler = Executors
            .newSingleThreadScheduledExecutor();

    private DatabaseResources databaseResources;

    private WorkloadResources workloadResources;

    private ServerControl server = new ServerControl();


    public ClientEmulationStartup() throws InvalidTransactionException {
        if (logger.isInfoEnabled()) {
            logger.info("Loading resources!");
        }

        databaseResources = new DatabaseResources();
        workloadResources = new WorkloadResources();
    }

    public static void main(String args[]) {
        try {
            ClientEmulationStartup c = new ClientEmulationStartup();
            InputStream inStream = DatabasePopulate.class.getResourceAsStream("/workload-config.properties");
            Properties props = new Properties();
            try {
                props.load(inStream);
            } catch (IOException e) {
                logger.fatal("Unable to load properties from file (workload-config.properties). Using defaults!", e);
            }
            String key=props.getProperty("prefix");
            String machine=c.getDatabaseResources().getConnectionString();
            c.startClients(key,machine,props.getProperty("clients"),props.getProperty("frag"));
        } catch (Exception ex) {
            Thread.dumpStack();
            System.exit(-1);
        }
    }


    private synchronized void start(String key, String arg, String machine)
            throws InvalidTransactionException {
        String[] args = arg.split("[ ]+");
        Stage stg = this.server.getClientStage(key);
        if (stg == null) {
            server.setClientStage(key, Stage.RUNNING);
            this.executor.execute(new Start(key, args, machine));
        } else {
            throw new InvalidTransactionException(key + " start on " + stg);
        }
    }

    public synchronized void startClients(String key, String connectionString,String clients,String frag)
            throws InvalidTransactionException {
        logger.info("Starting clients " + clients);
        String arg=this.configure(key,connectionString,clients,frag);
        this.server.addServer(connectionString);
        this.start(key,arg,connectionString);
    }



    public synchronized void pause(String key)
            throws InvalidTransactionException {
        server.pauseClient(key);
    }

    public synchronized void resume(String key)
            throws InvalidTransactionException {
        server.resumeClient(key);
    }

    public synchronized void stop(String key)
            throws InvalidTransactionException {
        server.stopClient(key);
    }

    public void kill() {
        this.executor.submit(new Kill());
    }



    private void startClientEmulation(String keyArgs, String machine,
                                      String[] args) {
        ClientEmulation e = null;
        ArgDB db = new ArgDB();
        Vector<ClientEmulation> ebs = new Vector<ClientEmulation>();
        DatabaseManager dbManager = null;

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

            BooleanArg isConnectionPoolEnabled = new BooleanArg("-enabledPOOL", "Enable POOL.",
                    "% It enables or disables the connection pool.", true, db);

            if (args.length == 0) {
                Usage(args, db);
                return;
            }

            db.parse(args);

            logger.info("Starting up the client application.");
            logger.info("Remote Emulator for Database Benchmark ...");
            logger
                    .info("Universidade do Minho (Grupo de Sistemas Distribuidos)");
            logger.info("Version 0.1");

            Usage(args, db);

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
            PerformanceLogger.setPrintWriter("TPCC-" + prefix.s + "-time-" + mi.num + "-clients-" + cli.num + "-frag-" + fragArg.num + "-think-" + key.flag + ".dat");
            PerformanceCounters.getReference();//Initialize instance

            dbManager.setConnectionPool(isConnectionPoolEnabled.flag);
            dbManager.setMaxConnection(poolArg.num);
            dbManager.setDriverName(driverArg.s);
            dbManager.setjdbcPath(pathArg.s);
            dbManager.setUserInfo(usrArg.s, passwdArg.s);

            for (int i = 0; i < cli.num; i++) {

                e = new ClientEmulation();

                e.setFinished(false);
                e.setTraceInformation(prefix.s);
                e.setNumberConcurrentEmulators(cli.num);
                e.setStatusThinkTime(key.flag);
                e.setStatusReSubmit(resArg.flag);
                e.setDatabase(dbManager);
                e.setEmulationName(prefix.s);
                e.setHostId(Integer.toString(hostArg.num));

                e.create(ebArg.s, stArg.s, i, fragArg.num, this, keyArgs);

                Thread t = new Thread(e);
                t.setName(prefix.s + "-" + i);
                e.setThread(t);
                t.start();

                ebs.add(e);
            }

            synchronized (this) {
                server.setClientEmulations(keyArgs, ebs);
                server.setClientConfiguration(keyArgs, args);
                server.attachClientToServer(keyArgs, machine);
            }

            logger.info("Running simulation for " + mi.num + " minute(s).");

            waitForRampDown(keyArgs, 0, mi.num);

            for (int i = 0; i < cli.num; i++) {
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

            logger.info("EBs finished.");
            PerformanceLogger.info("-------------------- SUMMARY ---------------------------");
            PerformanceLogger.info("Incoming rate:" + PerformanceCounters.getReference().getIncommingRate());
            PerformanceLogger.info("Commit rate:"+PerformanceCounters.getReference().getCommitRate());
            PerformanceLogger.info("Abort rate:" + PerformanceCounters.getReference().getAbortRate());
            PerformanceLogger.info("Average latency:"+PerformanceCounters.getReference().getAverageLatency());
            PerformanceLogger.info("Measured tpmC:"+PerformanceCounters.getReference().getTotalNewOrderCommitRate());
            PerformanceLogger.close();

        } catch (Arg.Exception ae) {
            logger.info("Error:");
            logger.info(ae);
            Usage(args, db);
            return;
        } catch (Exception ex) {
            logger.info("Error while creating clients: ", ex);
        } finally {
            synchronized (this) {
                this.server.removeClientEmulations(keyArgs);
                this.server.removeClientStage(keyArgs);
                this.server.detachClientToServer(keyArgs, machine);
                this.server.removeClientConfiguration(keyArgs);
                notifyAll();
            }

            try {
                dbManager.releaseConnections();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            logger.info("Ebs finished their jobs..");
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

    public synchronized void notifyThreadsCompletion(String key) {
        server.setClientStage(key, Stage.STOPPED);
        notifyAll();
    }

    public synchronized void notifyThreadsError(String key) {
        server.setClientStage(key, Stage.FAILOVER);
        notifyAll();
    }

    private synchronized void waitForRampDown(String key, int start, int term) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Start time " + start + " completion time " + term);
            }

            waitForStart(start);

            if (term < 0) {
                return;
            }

            long ini = System.currentTimeMillis();
            long end = 0;
            long remaining = term * 60 * 1000; // TODO: It must be changed to a
            // constant

            while (remaining > 0 && this.server.getClientStage(key) != null
                    && !this.server.getClientStage(key).equals(Stage.STOPPED)
                    && !this.server.getClientStage(key).equals(Stage.FAILOVER)) {

                wait(remaining);

                end = System.currentTimeMillis();

                if (logger.isInfoEnabled()) {
                    logger.info("Start remain " + remaining + " ini " + ini
                            + " end " + end);
                }

                remaining = remaining - (end - ini);
                ini = end;

                if (logger.isInfoEnabled()) {
                    logger.info("Start remain " + remaining + " ini " + ini
                            + " end " + end);
                }
            }
        } catch (InterruptedException ie) {
            logger.error("In waitforrampdown, caught interrupted exception");
        }
    }

    private void waitForStart(int start) throws InterruptedException {
        if (start < 0) {
            return;
        }

        Thread.sleep(start * 60 * 1000); // TODO - It must be changed to a
        // constant.
    }

    public DatabaseResources getDatabaseResources() {
        return databaseResources;
    }

    public void setDatabaseResources(DatabaseResources databaseResources) {
        this.databaseResources = databaseResources;
    }

    public WorkloadResources getWorkloadResources() {
        return workloadResources;
    }

    public void setWorkloadResources(WorkloadResources workloadResources) {
        this.workloadResources = workloadResources;
    }

    public synchronized void addServer(String key)
            throws InvalidTransactionException {
        this.server.addServer(key);
    }

    public synchronized void removeServer(String key)
            throws InvalidTransactionException {
        this.server.removeServer(key);
    }

    public HashSet<String> getClients() throws InvalidTransactionException {
        return (this.server.getClients());
    }

    public int getNumberOfClients(String key)
            throws InvalidTransactionException {
        return (this.server.getNumberOfClients(key));
    }

    public int getNumberOfClients() throws InvalidTransactionException {
        return (this.server.getNumberOfClients("*"));
    }

    public int getNumberOfClientsOnServer(String key)
            throws InvalidTransactionException {
        return (this.server.getNumberOfClientsOnServer(key));
    }

    public int getNumberOfClientsOnServer() throws InvalidTransactionException {
        return (this.server.getNumberOfClientsOnServer("*"));
    }

    public HashSet<String> getServers() throws InvalidTransactionException {
        return (this.server.getServers());
    }

    private String configure(String prefix,String url,String frag,String clients) {
        StringBuilder str=new StringBuilder();
        try{
            Class.forName(databaseResources.getDriver());
        } catch (ClassNotFoundException e) {
            logger.error("Unable to load database driver!", e);
        }

        InputStream inStream = DatabasePopulate.class.getResourceAsStream("/workload-config.properties");
        Properties props = new Properties();
        try {
            props.load(inStream);
        } catch (IOException e) {
            logger.fatal("Unable to load properties from file (workload-config.properties). Using defaults!", e);
        }

        str.append("-EBclass "+props.getProperty("eb.class"));
        str.append(" -LOGconfig etc/logger.xml");
        str.append(" -KEY "+props.getProperty("think.time"));
        str.append(" -CLI "+clients);
        str.append(" -STclass "+props.getProperty("st.class"));
        str.append(" -DBclass "+props.getProperty("db.class"));
        str.append(" -TRACEFLAG TRACE");
        str.append(" -PREFIX "+props.getProperty("prefix"));
        str.append(" -DBpath "+ url);
        str.append(" -DBdriver "+databaseResources.getDriver());
        str.append(" -DBusr "+databaseResources.getUserName());
        str.append(" -DBpasswd "+databaseResources.getPassword());
        str.append(" -POOL "+props.getProperty("pool"));
        str.append(" -FRAG "+ frag);
        str.append(" -MI "+props.getProperty("measurement.time"));
        str.append(" -RESUBMIT "+props.getProperty("resubmit.aborted"));

        return str.toString();
    }

    class Kill implements Runnable {
        public void run() {
            System.exit(0);
        }
    }

    class Start implements Runnable {
        private String[] args;

        private String key;

        private String machine;

        public Start(String key, String[] args, String machine) {
            this.key = key;
            this.args = args;
            this.machine = machine;
        }

        public void run() {
            startClientEmulation(this.key, this.machine, this.args);
        }
    }

    public void stop() throws InvalidTransactionException {
        server.stopFirstClient();
    }
}
