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
             -KEY true \
             -CLI 50 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.oracle.dbOracle \
             -TRACEFLAG TRACE \
             -PREFIX Client \
             -DBpath jdbc:oracle:thin:@192.168.82.141:1521:tpcc \
             -DBdriver oracle.jdbc.driver.OracleDriver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 50 \
             -MI 45 \
	     -FRAG 1 \
	     -RESUBMIT false

MYSQL_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY false \
             -CLI 10 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.mysql.dbMySql \
             -TRACEFLAG TRACE \
             -PREFIX Client \
             -DBpath jdbc:mysql://localhost/tpcc \
             -DBdriver com.mysql.jdbc.Driver \
             -DBusr root \
             -DBpasswd 123456 \
             -POOL 50 \
             -MI 45 \
	     -FRAG 1

PGSQL_FLAGS01 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY true \
             -CLI 20 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd 123456 \
             -POOL 10 \
             -MI 45 \
	     -FRAG 1 \
	     -RESUBMIT false

PGSQL_FLAGS02 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY true \
             -CLI 20 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost:5433/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 10 \
             -MI 45 \
	     -FRAG 1 \
	     -RESUBMIT false

real-oracle:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(ORACLE_FLAGS)

real-mysql:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(MYSQL_FLAGS)

real-pgsql-01:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS01)

real-pgsql-02:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS02)


# arch-tag: 35a104c6-523c-493b-9afe-e85f72d9d865
