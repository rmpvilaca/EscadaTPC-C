package Escada.tpc.tpcc.database.virtualdatabase;

import Escada.tpc.tpcc.database.*;
import Escada.tpc.tpcc.*;
import Escada.tpc.tpcc.util.*;
import Escada.tpc.common.util.*;

import java.util.*;

/**
 * This class represents a virtual database based on the distributions defined by the TPC-C.
 * In other words, the emulation program instead of accessing a real database, it access this
 * class in order to retrieve and update data. 
 * This class was built to circumvent the scalability problems faced when used the TPC-C with
 * our simulation environment. 
 * It is important to notice that it does not mischaracterize the TPC-C's definitions.
 *
 * This class can be divided into two main sections: (i) the first one defines a persitent mechanism
 * which simulates the tables of the database; (ii) the second one defines a set of methods, similar
 * to stored procedures which are used to access the tables.
 * 
 **/
public class dbVirtualDatabase {
  private Random rand = new Random();
  private static persistentObjects ptrClass = null;
  private class persistentObjects {
  	private Hashtable orders = new Hashtable();
  	private Hashtable neworder = new Hashtable();
  	private Hashtable customerLastName = new Hashtable();
  	private Vector rangeCustomerLastName = new Vector();

  	private persistentObjects(int totcli) {
  		Hashtable ptrOrdersHash = null;
  		Hashtable ptrNewOrderHash = null;
  		Hashtable ptrCustomerLastNameHash[] = null;
  		String lastname = null;
  		String occur = null;

  		int wid = ((totcli - 1) / TPCCConst.numMinClients) + 1;

  		System.out.println("Total Clients (" + totcli + ") Starting the following number of warehouses " + wid);
  				int contw = 1;
  				while (contw <= wid) {
  					ptrOrdersHash = new Hashtable();
  					ptrNewOrderHash = new Hashtable();
  					ptrCustomerLastNameHash = new Hashtable[TPCCConst.rngDistrict];

  					orders.put(Integer.toString(contw),ptrOrdersHash);
  					neworder.put(Integer.toString(contw),ptrNewOrderHash);
  					
  					customerLastName.put(Integer.toString(contw),ptrCustomerLastNameHash);

  					int contd = 1;
  					while (contd <= TPCCConst.rngDistrict) {
  						int nValue = 0;
  						ptrOrdersHash.put(Integer.toString(contd), 
  								Integer.toString(3000));
  						ptrNewOrderHash.put(Integer.toString(contd), 
  								Integer.toString(2100));
  						ptrCustomerLastNameHash[contd - 1] = new Hashtable();

  						int contc = 0;
  						while (contc <= TPCCConst.numENDCustomer)
  						{
  							lastname = 
  							TPCCRandGen.digSyl(RandGen.NURand(rand,TPCCConst.LastNameA,TPCCConst.numINILastName,TPCCConst.numENDLastName));
  							occur = (String) ptrCustomerLastNameHash[contd - 
																	 1].get(lastname);
  							if (occur != null)
  							{
  								nValue = Integer.parseInt(occur) + 1;
  								ptrCustomerLastNameHash[contd - 
														1].put(lastname,Integer.toString(nValue));
  							}
  							else
  							{
  								ptrCustomerLastNameHash[contd - 
														1].put(lastname,Integer.toString(1));
  							}

  							nValue = rangeCustomerLastName.indexOf(lastname);
  							if (nValue == -1)
  							{
  								rangeCustomerLastName.add(lastname);
  							}
  							contc++;
  						}
  						contd++;
  					}
  					contw++;
  				}
  	}
  }
  

  /**
   * It instantiates the virtual database and populates it according to the 
   * number of warehouses.
   *
   * @param int the number of warehouses
   **/
  public dbVirtualDatabase(int nwid) {
      if (ptrClass == null) {
        ptrClass = new persistentObjects(nwid);
    }
  }

  public String getCurrentNewOrder(String wid, String did) {
    Hashtable ptrNewOrderHash = (Hashtable) ptrClass.neworder.get(wid);

    return ( (String) ptrNewOrderHash.get(did));
  }


  public HashSet getCurrentNewOrder(String wid) {
    int cont = 1;
    HashSet dbtrace = new HashSet();
    Hashtable ptrNewOrderHash = (Hashtable) ptrClass.neworder.get(wid);

    while (cont <= TPCCConst.rngDistrict) {
      dbtrace.add(wid + Integer.toString(cont) +
                  (String) ptrNewOrderHash.get(Integer.toString(cont)));
      cont++;
    }
    return (dbtrace);
  }

  public String getCurrentOrders(String wid, String did) {
    Hashtable ptrOrdersHash = (Hashtable) ptrClass.orders.get(wid);

    return ( (String) ptrOrdersHash.get(did));
  }

  public HashSet getCustomerNewOrder(String wid) {
    HashSet dbtrace = new HashSet();
    int cont = 1;
    int cid = 0;

    while (cont <= TPCCConst.rngDistrict) {
      cid = RandGen.NURand(rand,
                           TPCCConst.CustomerA, TPCCConst.numINICustomer,
                           TPCCConst.numENDCustomer);

      dbtrace.add(wid + Integer.toString(cont) + Integer.toString(cid));
      cont++;
    }
    return (dbtrace);
  }

  public HashSet getOrderLineNewOrder(String wid) {
    HashSet dbtrace = new HashSet();
    int cont = 1;
    int qtd = RandGen.nextInt(rand, TPCCConst.qtdINIItem,
                              TPCCConst.qtdENDItem + 1);

    while (cont <= TPCCConst.rngDistrict) {
      int qtdcont = 1;
      while (qtdcont <= qtd) {
        dbtrace.add(wid + Integer.toString(cont) +
                    getCurrentNewOrder(wid, Integer.toString(cont)) +
                    Integer.toString(qtdcont));
        qtdcont++;
      }
      cont++;
    }
    return (dbtrace);
  }

  public HashSet getCustomerOrders(String wid, String did, String cid) {
    HashSet dbtrace = new HashSet();
    dbtrace.add(wid + did + cid); 
    return (dbtrace);
  }

  public void insertNewOrder(String wid, String did) {
    Hashtable ptrNewOrderHash = (Hashtable) ptrClass.neworder.get(wid);

    int n = Integer.parseInt( (String) ptrNewOrderHash.get(did));
    n++;
    ptrNewOrderHash.put(did, Integer.toString(n));
  }

  public void insertNewOrder(String wid) {
    int n = 0;
    int cont = 1;
    Hashtable ptrNewOrderHash = (Hashtable) ptrClass.neworder.get(wid);
    while (cont <= TPCCConst.rngDistrict) {
      n = Integer.parseInt( (String) ptrNewOrderHash.get(Integer.toString(cont)));
      n++;
      ptrNewOrderHash.put(Integer.toString(cont), Integer.toString(n));
      cont++;
    }
  }

  public void insertOrders(String wid, String did) {
    Hashtable ptrOrdersHash = (Hashtable) ptrClass.orders.get(wid);
    
    int n = Integer.parseInt((String) ptrOrdersHash.get(did));
    n++;
    ptrOrdersHash.put(did, Integer.toString(n));
  }

  public HashSet getNewOrder(String wid) {
    int contdis = 1;
    HashSet dbtrace = new HashSet();
    Hashtable ptrOrdersHash = (Hashtable) ptrClass.orders.get(wid);
    Hashtable ptrNewOrderHash = (Hashtable) ptrClass.neworder.get(wid);

    while (contdis <= TPCCConst.rngDistrict) {
      int contini = Integer.parseInt((String)ptrNewOrderHash.get(Integer.toString(contdis)));
      int contend = Integer.parseInt((String)ptrOrdersHash.get(Integer.toString(contdis)));

      while (contini <= contend)
      {
        dbtrace.add(wid + contdis + Integer.toString(contini));
        contini++;
      }
      contdis++;
    }
    return (dbtrace);
  }

  public HashSet getOrderLineStockLevel(String wid, String did) {
    int oldorders = Integer.parseInt(getCurrentOrders(wid, did)) - 20;
    int curorders = Integer.parseInt(getCurrentOrders(wid, did));
    HashSet dbtrace = new HashSet();

    while (oldorders < curorders) {
      int qtd = RandGen.nextInt(rand, TPCCConst.qtdINIItem,
                                TPCCConst.qtdENDItem + 1);
      int contqtd = 1;
      while (contqtd <= qtd) {
        dbtrace.add(wid + did + oldorders + contqtd);
        contqtd++;
      }
      oldorders++;
    }
    return (dbtrace);
  }

  public HashSet getStockLevel(String wid, String did) {
    int oldorders = Integer.parseInt(getCurrentOrders(wid, did)) - 20;
    int curorders = Integer.parseInt(getCurrentOrders(wid, did));
    HashSet dbtrace = new HashSet();

    while (oldorders < curorders) {
      int qtd = RandGen.nextInt(rand, TPCCConst.qtdINIItem,
                                TPCCConst.qtdENDItem + 1);
      int contqtd = 1;

      while (contqtd <= qtd) {
        int iid = RandGen.NURand(rand, TPCCConst.iidA, TPCCConst.numINIItem,
                                 TPCCConst.numENDItem);
        dbtrace.add(wid + iid);
        contqtd++;
      }
      oldorders++;
    }
    return (dbtrace);
  }
  
  public HashSet getCustomerLastName(String wid, String did, String lastname)
  {
    Hashtable ptrCustomerLastNameHash [] = (Hashtable []) ptrClass.customerLastName.get(wid);
    HashSet dbtrace = new HashSet();    
    
    String occur = (String) ptrCustomerLastNameHash[Integer.parseInt(did) - 1].get(lastname);
    
    if (occur != null)
    {
       int contini = 1;
       int contend = Integer.parseInt(occur);
       int range = ptrClass.rangeCustomerLastName.indexOf(lastname) * 1000;
       int nValue = 0;
       while (contini <= contend)
       {
          nValue = contini + range;
          dbtrace.add(wid + did + nValue);
	  contini++;
       }
    }

    return (dbtrace);
  }
}

// arch-tag: d254f0e6-c3d8-41a2-8857-5bdc06efd581
