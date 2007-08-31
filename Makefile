export projdir=$(shell pwd)/..
include ./Makefile.vars

ORACLE_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
             -KEY true \
             -CLI 50 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.oracle.dbOracle \
             -TRACEFLAG TRACE \
             -PREFIX Client \
             -DBpath jdbc:oracle:thin:@127.0.0.1:tpcc \
             -DBdriver oracle.jdbc.driver.OracleDriver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 50 \
             -MI 45 \
	     -FRAG 1 \
	     -RESUBMIT false

SEQUOIA_MYSQL_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY false \
             -CLI 1 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.mysql.dbTransactionMySql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:sequoia://localhost/tpcc \
             -DBdriver org.continuent.sequoia.driver.Driver \
             -DBusr rmpvilaca \
             -DBpasswd 123456 \
             -POOL 1\
             -MI 100 \
             -FRAG 1 \
             -RESUBMIT true

MYSQL_FLAGS=-EBclass escada.tpc.tpcc.TPCCEmulation \
	     -LOGconfig configuration.files/logger.xml \
             -KEY false \
             -CLI 10 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.mysql.dbTransactionMySql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:mysql://localhost/tpcc \
             -DBdriver com.mysql.jdbc.Driver \
             -DBusr rmpvilaca \
             -DBpasswd 123456 \
             -POOL 10\
             -MI 1000 \
	     -FRAG 1 \
             -RESUBMIT true

PGSQL_FLAGS01 =-EBclass escada.tpc.tpcc.TPCCEmulation \
	     -LOGconfig configuration.files/logger.xml \
             -KEY false \
             -CLI 10\
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://127.0.0.1:5432/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd 123456 \
             -POOL 10 \
             -MI 10 \
	     -FRAG 0 \
	     -RESUBMIT true

PGSQL_FLAGS02 =-EBclass escada.tpc.tpcc.TPCCEmulation \
	     -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI $(CLI) \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.180.2/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL $(CLI) \
             -MI 390 \
	     -FRAG $(FRAG) \
	     -RESUBMIT false


PGSQL_FLAGS03 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI $(CLI) \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.180.3/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL $(CLI) \
             -MI 390 \
             -FRAG $(FRAG) \
             -RESUBMIT false


PGSQL_FLAGS04 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI $(CLI) \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.180.4/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL $(CLI) \
             -MI 390 \
             -FRAG $(FRAG) \
             -RESUBMIT false


PGSQL_FLAGS05 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI $(CLI) \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.180.5/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL $(CLI) \
             -MI 390 \
             -FRAG $(FRAG) \
             -RESUBMIT false

real-oracle:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(ORACLE_FLAGS)

real-sequoia-mysql:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(SEQUOIA_MYSQL_FLAGS)

real-mysql:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(MYSQL_FLAGS)

real-pgsql-01:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS01)

real-pgsql-02:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS02)

load-pgsql-01:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.tpcc.database.populate.Populate P 2

real-pgsql-03:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS03)

real-pgsql-04:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS04)

real-pgsql-05:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(PGSQL_FLAGS05)


# arch-tag: 35a104c6-523c-493b-9afe-e85f72d9d865
