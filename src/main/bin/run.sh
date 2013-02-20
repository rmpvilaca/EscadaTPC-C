for i in ./lib/*; do
    CP=$CP:$i
done


"$JAVA_HOME/bin/java" -cp $CP:etc -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5010 -Xmx1024M escada.tpc.common.clients.jmx.ClientEmulationStartup

