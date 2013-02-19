for i in ./lib/*; do
    CP=$CP:$i
done

FLAGS="-LOGconfig etc/log4j.xml -EBclass escada.tpc.tpcc.TPCCEmulation -KEY false -CLI 1 -Stclass escada.tpc.tpcc.TPCCStateTransition -DBclass escada.tpc.tpcc.database.transaction.postgresql.dbPostgresql -TRACEFLAG TRACE -PREFIX PostgreSQL -DBpath jdbc:postgresql://192.168.111.218:5432/tpcc -DBdriver org.postgresql.Driver -DBusr gorda -DBpasswd gorda -POOL 100 -MI 45"
FLAGS="-LOGconfig etc/log4j.xml -EBclass escada.tpc.tpcc.TPCCEmulation -KEY false -CLI 1 -Stclass escada.tpc.tpcc.TPCCStateTransition -DBclass escada.tpc.tpcc.database.transaction.mysql.dbTransactionMySql -TRACEFLAG TRACE -PREFIX MySQL -DBpath jdbc:mysql://192.168.111.217/tpcc -DBdriver com.mysql.jdbc.Driver -DBusr root -DBpasswd root -POOL 100 -MI 5"

"$JAVA_HOME/bin/java" -cp $CP:etc -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5010 -Xmx1024M escada.tpc.common.clients.jmx.ClientEmulationStartup $FLAGS

