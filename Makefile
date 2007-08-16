include ./Makefile.vars

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
	     -LOGconfig configuration.files/logger.xml \
             -KEY false \
             -CLI 1 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost:5432/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd 123456 \
             -POOL 20 \
             -MI 300 \
	     -FRAG 1 \
	     -RESUBMIT false

PGSQL_FLAGS02 =-EBclass escada.tpc.tpcc.TPCCEmulation \
	     -LOGconfig configuration.files/logger.xml \
             -KEY false \
             -CLI 5 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://localhost:5433/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 20 \
             -MI 300 \
	     -FRAG 2 \
	     -RESUBMIT false


PGSQL_FLAGS03 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI 30 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.74.16/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 30 \
             -MI 45 \
             -FRAG 5 \
             -RESUBMIT false


PGSQL_FLAGS04 =-EBclass escada.tpc.tpcc.TPCCEmulation \
             -LOGconfig configuration.files/logger.xml \
             -KEY true \
             -CLI 30 \
             -STclass escada.tpc.tpcc.TPCCStateTransition \
             -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql \
             -TRACEFLAG TRACE \
             -PREFIX TPC-C \
             -DBpath jdbc:postgresql://192.168.74.10/tpcc \
             -DBdriver org.postgresql.Driver \
             -DBusr tpcc \
             -DBpasswd tpcc \
             -POOL 30 \
             -MI 45 \
             -FRAG 8 \
             -RESUBMIT false


real-oracle:
	$(JVM) -cp $(classpath) -Xmx1024M escada.tpc.common.clients.ClientEmulationStartup $(ORACLE_FLAGS)

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


# arch-tag: 35a104c6-523c-493b-9afe-e85f72d9d865
