package escada.tpc.common.clients;

import java.lang.reflect.*;

import escada.tpc.common.*;
import escada.tpc.common.database.*;


/**
 * It initializes the client Emulation instantiating the appropriate objects.
 * 
 **/
public class ClientEmulation extends Thread {
  Emulation e = null;	
  
 /**
  * It initializes the client Emulation instantiating the appropriate objects.
  * Specifically, it defines the emulation object to be used (e.g., TPCCEmulation),
  * the state transition object (e.g., TPCCStateTransition), the database interface
  * (e.g., dbPostgresql). Besides, it also defines the total number of clients, the 
  * client id and the host id to which the client belongs. It is important to notice
  * that the parameters bellow which reference a class must be fully qualified.
  *
  * @param emParam the class used to instantiate the emulation object (e.g., TPCCEmulation)
  * @param stateParam the class used to instantiate the state transition object (e.g., TPCCStateTransition)
  * @param dbParam the class used to instantiate the database object (e.g., dbPostgresql)
  * @param totalCli the total number of clients
  * @param ncli the identification of the client
  * @param nfrag the offset the must be applied to the client in order to it to get the correct identification.
  * @param hid the host to which the client belongs
  **/
public ClientEmulation(String emParam,String stateParam,String dbParam,int totalCli,int ncli,String trace,String hid,int nfrag) {
 
    	StateTransition s = null;
    	CommonDatabase d = null;
    	Class cl = null;
    	Constructor co = null;

    	System.out.println("Starting " + ncli + " Ems.");
    	
    	try {
	    e = (Emulation) Class.forName(emParam).newInstance();
	    s = (StateTransition) Class.forName(stateParam).newInstance();

    	    cl = Class.forName(dbParam);
	    try {
		co = cl.getConstructor(new Class[] {Integer.TYPE});
	    }
	    catch (Exception ex) {
	    }
	    if (co == null) d = (CommonDatabase) cl.newInstance();
	    else d = (CommonDatabase) co.newInstance(new Object[] {new Integer(totalCli)});

	    int temp = (ncli + ((nfrag - 1) * 10)); 

    	    e.initialize();
    	    e.setEmulationId(temp);
    	    e.setEmulationName(trace + "-" + temp);
    	    e.setStateTransition(s);
    	    e.setDatabase(d);
	    e.setHostId(hid);
    	}
    	catch (java.lang.Exception ex) {
	    ex.printStackTrace(System.err);
	    System.out.println("Error loading objects and setting up the system...");
	    System.exit(-1);
    	}
  }

  /**
   * This function calls the appropriate emulation.
   **/
  public void run()  {
      e.process();
  }

 /**
   * This function calls the step by step producer.
   *
   * @return Object it returns an object Transaction
  **/
  public Object processIncrement() {
      return(e.processIncrement());
  }

 /**
   * This function calls the producer.
  **/
  public void process() {
      e.process();
  }

}

// arch-tag: f05c77d8-2aeb-4e8a-a28f-f7bc8311b84a
