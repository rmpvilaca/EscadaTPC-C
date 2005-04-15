export projdir=$(shell pwd)/..
include ./Makefile.vars
GARBAGE=`find . -name \*.class`

export SRCS=$(shell find escada/tpc -name \*.java)

all: jar

compile: $(SRCS)
	$(JC) $(JIKES_FLAGS) -g $^
	
jar: compile
	(cd $(classesdir) ; $(JAR) $(JAR_FLAGS) $(PROJ_JAR) `find  escada/tpc -name \*.class`);

clean:
	rm -rf $(GARBAGE)
	echo $(GARBAGE)
	
ORACLE_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 10 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.oracle.dbOracle \
             -TRACEflaf TRACE \
             -PREFIX Client \
             -DBpath jdbc:oracle:thin:@192.168.2.32:1521:tpccdb \
             -DBdriver oracle.jdbc.driver.OracleDriver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 50 \
             -MI 45 \
	     -FRAG 1

MYSQL_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 10 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.mysql.dbMySql \
             -TRACEflag TRACE \
             -PREFIX Client \
             -DBpath jdbc:mysql://localhost/tpcc \
             -DBdriver com.mysql.jdbc.Driver \
             -DBusr root \
             -DBpasswd 123456 \
             -POOL 50 \
             -MI 45 \
	     -FRAG 1

PGSQL_FLAGS01 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 1 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://lhona:5432/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 20 \
             -MI 45 \
	     -FRAG 1 \
	     -RESUBMIT false

PGSQL_FLAGS02 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 10 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost:5432/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 10 \
             -MI 45 \
	     -FRAG 1

DBMSEMU_FLAGS =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 1 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.dbmsemu.dbDBMSEmu \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost:5432/tpcc \
             -DBdriver escada.dbmsemu.jdbcdriver.DBMSEmuJDBCDriver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 10 \
             -MI 45 \
	     -FRAG 1

run-dbmsemu:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientStartup $(DBMSEMU_FLAGS)

real-oracle:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientStartup $(ORACLE_FLAGS)

real-mysql:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientStartup $(MYSQL_FLAGS)

real-pgsql-01:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientStartup $(PGSQL_FLAGS01)

real-pgsql-02:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientStartup $(PGSQL_FLAGS02)


# arch-tag: 35a104c6-523c-493b-9afe-e85f72d9d865
