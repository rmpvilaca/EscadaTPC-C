package Escada.tpc.tpcc.database.mssql;

import Escada.tpc.tpcc.database.*;
import Escada.tpc.common.*;

import java.io.*;
import java.sql.*;
import java.util.*;

/** It is an interface to a postgreSQL, which based is based on the
* the distributions of the TPC-C.
**/
public class dbMSSql
    extends dbTPCCDatabase {

  public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
      SQLException {

    Connection con = null;
    HashSet dbtrace = null;

    try {
        con = getConnection();
	
	System.out.println("Beginning transaction new order (thread(" + Thread.currentThread().getName() + "))");
	
        dbtrace = NewOrderDB(obj, con);

	System.out.println("Finishing transaction new order (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      System.err.println(Thread.currentThread().getName() + " NewOrder - General Main Exception " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet NewOrderDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      CallableStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
	  statement = con.prepareCall("{call tpcc_neworder(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setInt(4, Integer.parseInt( (String) obj.getInfo("qtd")));
        statement.setInt(5, Integer.parseInt( (String) obj.getInfo("localwid")));

	int icont = 0;
	int desParam = 6;
	int qtdTotal = 15;
        int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
     
	while (icont < qtd) {
	    statement.setInt(desParam, Integer.parseInt( (String) obj.getInfo("iid" + icont)));
	    statement.setInt(desParam + 1, Integer.parseInt( (String) obj.getInfo("supwid" + icont)));
	    statement.setInt(desParam + 2, Integer.parseInt( (String) obj.getInfo("qtdi" + icont)));
	    icont++; desParam=desParam + 3;
        }
	while (icont < qtdTotal) {
	    statement.setInt(desParam, 0);
	    statement.setInt(desParam + 1, 0);
	    statement.setInt(desParam + 2, 0);
	    icont++; desParam=desParam + 3;
        }
        rs = statement.executeQuery();
     
        while (rs.next()) {
          dbtrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
      }
      catch (java.sql.SQLException sqlex) {
        System.err.println(Thread.currentThread().getName() + " NewOrder - SQL Exception " + sqlex.getMessage());     
        if (sqlex.getMessage().indexOf("deadlock") != -1) continue;
        throw new java.sql.SQLException(sqlex.getMessage());
      }
      catch (java.lang.Exception ex) {
        System.err.println(Thread.currentThread().getName() + " NewOrder - General Exception " + ex.getMessage());
        throw new java.sql.SQLException(ex.getMessage());
      }
      finally {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      }
      return (dbtrace);
    }
  }

  public Object TraceDeliveryDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
                                                                                                                             
    Connection con = null;
    HashSet dbtrace = null;
                                                                                                                             
    try {
        con = getConnection();

	System.out.println("Beginning transaction new order (thread(" + Thread.currentThread().getName() + "))");
                                                                                                                             
        dbtrace = DeliveryDB(obj, con);
                                                                                                                             
	System.out.println("Finishing trasaction delivery (thread(" + Thread.currentThread().getName() + "))");

    }
    catch (java.lang.Exception ex) {
      System.err.println(Thread.currentThread().getName() + " Delivery - General Main Exception " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet DeliveryDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      CallableStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareCall("{call tpcc_delivery(?,?)}");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("crid")));
        rs = statement.executeQuery();

        while (rs.next()) {
          dbtrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
      }
      catch (java.sql.SQLException sqlex) {
        System.err.println(Thread.currentThread().getName() + " Delivery - SQL Exception " + sqlex.getMessage());
        if (sqlex.getMessage().indexOf("deadlock") != -1) continue;
        throw new java.sql.SQLException(sqlex.getMessage());
      }
      catch (java.lang.Exception ex) {
        System.err.println(Thread.currentThread().getName() + " Delivery - General Exception " + ex.getMessage());
        throw new java.sql.SQLException(ex.getMessage());
      }
      finally {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      }
      return (dbtrace);
    }
  }

  public Object TraceOrderStatusDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
                                                                                                                             
    Connection con = null;
    HashSet dbtrace = null;
                                                                                                                             
    try {
        con = getConnection();

	System.out.println("Beginning transaction order status (thread(" + Thread.currentThread().getName() + "))");
                                                                                                                             
        dbtrace = OrderStatusDB(obj, con);
                                                                                                                             
	System.out.println("Finishing transaction order status (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      System.err.println(Thread.currentThread().getName() + " OrderStatus - General Main Exception " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }


  public HashSet OrderStatusDB(OutInfo obj, Connection con) throws java.
      sql.SQLException {
    while (true) {
      CallableStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareCall("{call tpcc_orderstatus(?,?,?,?)}");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setString(4, (String) obj.getInfo("lastname"));
        rs = statement.executeQuery();

        while (rs.next()) {
          dbtrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
      }
      catch (java.sql.SQLException sqlex) {
        System.err.println(Thread.currentThread().getName() + " OrderStatus - SQL Exception " + sqlex.getMessage());
        if (sqlex.getMessage().indexOf("deadlock") != -1) continue;
        throw new java.sql.SQLException(sqlex.getMessage());
      }
      catch (java.lang.Exception ex) {
        System.err.println(Thread.currentThread().getName() + " OrderStatus - General Exception " + ex.getMessage());
        throw new java.sql.SQLException(ex.getMessage());
      }
      finally {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      }
      return (dbtrace);
    }
  }

  public Object TracePaymentDB(OutInfo obj,String hid) throws java.sql.SQLException {
                                                                                                                             
    Connection con = null;
    HashSet dbtrace = null;
                                                                                                                             
    try {
        con = getConnection();

	System.out.println("Beginning transaction payment (thread(" + Thread.currentThread().getName() + "))");
                                                                                                                             
        dbtrace = PaymentDB(obj, con);
                                                                                                                             
	System.out.println("Finishing transaction payment (thread(" + Thread.currentThread().getName() + "))");

    }
    catch (java.lang.Exception ex) {
      System.err.println(Thread.currentThread().getName() + " Payment - General Main Exception " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet PaymentDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      CallableStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareCall(
            "{call tpcc_payment(?,?,?,?,?,?,?)}");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cwid")));
        statement.setFloat(3, Float.parseFloat( (String) obj.getInfo("hamount")));
        statement.setInt(4, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(5, Integer.parseInt( (String) obj.getInfo("cdid")));
        statement.setInt(6, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setString(7, (String) obj.getInfo("lastname"));

        rs = statement.executeQuery();

        while (rs.next()) {
          dbtrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
      }
      catch (java.sql.SQLException sqlex) {
        System.err.println(Thread.currentThread().getName() + " Payment - SQL Exception " + sqlex.getMessage());
        if (sqlex.getMessage().indexOf("deadlock") != -1) continue;
        throw new java.sql.SQLException(sqlex.getMessage());
      }
      catch (java.lang.Exception ex) {
        System.err.println(Thread.currentThread().getName() + " Payment - General Exception " + ex.getMessage());
        throw new java.sql.SQLException(ex.getMessage());
      }
      finally {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      }
      return (dbtrace);
    }
  }

  public Object TraceStockLevelDB(OutInfo obj,String hid) throws java.sql.SQLException {
                                                                                                                             
    Connection con = null;
    HashSet dbtrace = null;
                                                                                                                             
    try {
        con = getConnection();

	System.out.println("Beginning transaction stock level (thread(" + Thread.currentThread().getName() + "))");
                                                                                                                             
        dbtrace = StockLevelDB(obj, con);
                                                                                                                             
	System.out.println("Finishing transaction stock level (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      System.err.println(Thread.currentThread().getName() + " StockLevel - General Main Exception " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet StockLevelDB(OutInfo obj, Connection con) throws java.
      sql.SQLException {
    while (true) {
      CallableStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareCall("{call tpcc_stocklevel(?,?,?)}");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("threshhold")));
        rs = statement.executeQuery();

        while (rs.next()) {
          dbtrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
      }
      catch (java.sql.SQLException sqlex) {
        System.err.println(Thread.currentThread().getName() + " StockLevel - SQL Exception " + sqlex.getMessage());
        if (sqlex.getMessage().indexOf("deadlock") != -1) continue;
        throw new java.sql.SQLException(sqlex.getMessage());
      }
      catch (java.lang.Exception ex) {
        System.err.println(Thread.currentThread().getName() + " StockLevel - General Exception " + ex.getMessage());
        throw new java.sql.SQLException(ex.getMessage());
      }
      finally {
        if (rs != null) {
          rs.close();
        }
        if (statement != null) {
          statement.close();
        }
      }
      return (dbtrace);
    }
  }

  public static void InitTransaction(OutInfo obj, Connection con,
                                     String transaction) throws
      java.sql.SQLException {
	throw new java.sql.SQLException("This method is not currently available for MSSQL.");
  }

  public static void CommitTransaction(Connection con) throws java.sql.
      SQLException {
	throw new java.sql.SQLException("This method is not currently available for MSSQL.");
  }

  public static void RollbackTransaction(Connection con, Exception dump) throws
      java.sql.SQLException {
	throw new java.sql.SQLException("This method is not currently available for MSSQL.");
  }

  public static boolean trace(OutInfo obj) {
    return ( ( (String) obj.getInfo("trace")).equalsIgnoreCase("TRACE"));
  }

  public static boolean traceString(OutInfo obj) {
    return ( ( (String) obj.getInfo("trace")).equalsIgnoreCase(
        "TRACESTRING"));
  }
}
// arch-tag: 96ed9699-68a0-4515-bd5f-be5a51f4c369
