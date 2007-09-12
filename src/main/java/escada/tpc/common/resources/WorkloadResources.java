package escada.tpc.common.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import escada.tpc.common.TPCConst;
import escada.tpc.tpcc.TPCCConst;
import escada.tpc.tpcc.database.populate.jmx.DatabasePopulate;

public class WorkloadResources implements WorkloadResourcesMBean {

	private static final Logger logger = Logger.getLogger(WorkloadResources.class);
	
	public final static String DEFAULT_NUMBER_OF_WAREHOUSES = "4";
	
	private int numberOfWarehouses = Integer.parseInt(DEFAULT_NUMBER_OF_WAREHOUSES);
	
	public WorkloadResources() {
		
		InputStream inStream = DatabasePopulate.class.getResourceAsStream("/workload-config.properties");
		Properties props = new Properties();
		try {
			props.load(inStream);
		} catch (IOException e) {
			logger.fatal("Unable to load properties from file (workload-config.properties). Using defaults!", e);
		}

		TPCConst.setNumMinClients(Integer.parseInt(props.getProperty("tpcc.numclients", Integer.toString(TPCCConst.getCliWareHouse()))));
		TPCCConst.setNumCustomer(Integer.parseInt(props.getProperty("tpcc.numcustomers", Integer.toString(TPCCConst.getNumCustomer()))));
		TPCCConst.setNumDistrict(Integer.parseInt(props.getProperty("tpcc.numdistricts", Integer.toString(TPCCConst.getNumDistrict()))));
		TPCCConst.setNumItem(Integer.parseInt(props.getProperty("tpcc.numitems", Integer.toString(TPCCConst.getNumItem()))));
		TPCCConst.setNumLastName(Integer.parseInt(props.getProperty("tpcc.numnames", Integer.toString(TPCCConst.getNumLastName()))));
		
		this.numberOfWarehouses = Integer.parseInt(props.getProperty("tpcc.number.warehouses", DEFAULT_NUMBER_OF_WAREHOUSES));
	}

	public synchronized void setNumberOfWarehouses(int numberOfWarehouses) {
		this.numberOfWarehouses = numberOfWarehouses;
	}

	public synchronized int getNumberOfWarehouses() {
		return numberOfWarehouses;
	}

	public int getNumMinClients() {
		return TPCConst.getNumMinClients();
	}

	public void setNumMinClients(int i) {
		TPCConst.setNumMinClients(i);
	}
	
}
