package escada.tpc.tpcc.database.postgresql;

import java.sql.*;
import java.util.*;

import escada.tpc.common.*;
import escada.tpc.tpcc.database.*;

/** It is an interface to a postgreSQL, which based is based on the
* the distributions of the TPC-C.
**/
public class dbPostgresql
    extends dbTPCCDatabase {

  public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
      SQLException {

    Connection con = null;
    HashSet dbtrace = null;

    try {
        con = getConnection();

	System.out.println("Beginning transaction new order (thread(" + Thread.currentThread().getName() + "))");

        InitTransaction(obj, con, "tx neworder");

        dbtrace = NewOrderDB(obj, con);

        CommitTransaction(con);

	System.out.println("Finishing transaction new order (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet NewOrderDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      PreparedStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareCall("select tpcc_neworder (?,?,?,?,?,?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setInt(4, Integer.parseInt( (String) obj.getInfo("qtd")));
        statement.setInt(5, Integer.parseInt( (String) obj.getInfo("localwid")));

        int icont = 0;
        int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
        StringBuffer iid = new StringBuffer();
        StringBuffer wid = new StringBuffer();
        StringBuffer qtdi = new StringBuffer();
        while (icont < qtd) {
          iid.append( (String) obj.getInfo("iid" + icont));
          iid.append(",");
          wid.append( (String) obj.getInfo("supwid" + icont));
          wid.append(",");
          qtdi.append( (String) obj.getInfo("qtdi" + icont));
          qtdi.append(",");
          icont++;
        }
        statement.setString(6, iid.toString());
        statement.setString(7, wid.toString());
        statement.setString(8, qtdi.toString());

        rs = statement.executeQuery();

        if (rs.next()) {
          cursor = (String) rs.getString(1);
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
        statement = con.prepareStatement("fetch all in \"" + cursor + "\"");
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
        if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)) {
          RollbackTransaction(con, sqlex);
          InitTransaction(obj, con, "tx neworder");
          continue;
        }
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

	System.out.println("Beginning trasaction delivery (thread(" + Thread.currentThread().getName() + "))");

        InitTransaction(obj, con, "tx delivery");
                                                                                                                             
        dbtrace = DeliveryDB(obj, con);
                                                                                                                             
        CommitTransaction(con);

	System.out.println("Finishing trasaction delivery (thread(" + Thread.currentThread().getName() + "))");

    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet DeliveryDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      PreparedStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareStatement("select tpcc_delivery(?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("crid")));
        rs = statement.executeQuery();

        if (rs.next()) {
          cursor = (String) rs.getString(1);
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
        statement = con.prepareStatement("fetch all in \"" + cursor + "\"");
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
        if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)){
          RollbackTransaction(con, sqlex);
          InitTransaction(obj, con, "tx delivery");
          continue;
        }
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

        InitTransaction(obj, con, "tx orderstatus");
                                                                                                                             
        dbtrace = OrderStatusDB(obj, con);
                                                                                                                             
        CommitTransaction(con);

	System.out.println("Finishing transaction order status (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }


  public HashSet OrderStatusDB(OutInfo obj, Connection con) throws java.
      sql.SQLException {
    while (true) {
      PreparedStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareStatement("select tpcc_orderstatus(?,?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setString(4, (String) obj.getInfo("lastname"));
        rs = statement.executeQuery();

        if (rs.next()) {
          cursor = (String) rs.getString(1);
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
        statement = con.prepareStatement("fetch all in \"" + cursor + "\"");
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
        if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)){
          RollbackTransaction(con, sqlex);

          String str = (String) (obj).getInfo("cid");
          if (str.equals("0")) {
            InitTransaction(obj, con, "tx orderstatus 01");
          }
          else {
            InitTransaction(obj, con, "tx orderstatus 02");
          }

          continue;
        }
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

        InitTransaction(obj, con, "tx payment");
                                                                                                                             
        dbtrace = PaymentDB(obj, con);
                                                                                                                             
        CommitTransaction(con);

	System.out.println("Finishing transaction payment (thread(" + Thread.currentThread().getName() + "))");

    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }

  public HashSet PaymentDB(OutInfo obj, Connection con) throws java.sql.
      SQLException {
    while (true) {
      PreparedStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareStatement(
            "select tpcc_payment(?,?,cast(? as numeric(6,2)),?,?,?,cast(? as char(16)))");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cwid")));
        statement.setFloat(3, Float.parseFloat( (String) obj.getInfo("hamount")));
        statement.setInt(4, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(5, Integer.parseInt( (String) obj.getInfo("cdid")));
        statement.setInt(6, Integer.parseInt( (String) obj.getInfo("cid")));
        statement.setString(7, (String) obj.getInfo("lastname"));

        rs = statement.executeQuery();

        if (rs.next()) {
          cursor = (String) rs.getString(1);
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
        statement = con.prepareStatement("fetch all in \"" + cursor + "\"");
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
        if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)) {
          RollbackTransaction(con, sqlex);
          String str = (String) (obj).getInfo("cid");
          if (str.equals("0")) {
            InitTransaction(obj, con, "tx payment 01");
          }
          else {
            InitTransaction(obj, con, "tx payment 02");
          }
          continue;
        }
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

        InitTransaction(obj, con, "tx stocklevel");
                                                                                                                             
        dbtrace = StockLevelDB(obj, con);
                                                                                                                             
        CommitTransaction(con);

	System.out.println("Finishing transaction stock level (thread(" + Thread.currentThread().getName() + "))");
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      returnConnection(con);
    }
    return (dbtrace);
  }


  public HashSet StockLevelDB(OutInfo obj, Connection con) throws java.
      sql.SQLException {
    while (true) {
      PreparedStatement statement = null;
      ResultSet rs = null;
      String cursor = null;

      HashSet dbtrace = new HashSet();

      try {
        statement = con.prepareStatement("select tpcc_stocklevel(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("threshhold")));
        rs = statement.executeQuery();

        if (rs.next()) {
          cursor = (String) rs.getString(1);
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;
        statement = con.prepareStatement("fetch all in \"" + cursor + "\"");
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
        if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)) {
          RollbackTransaction(con, sqlex);
          InitTransaction(obj, con, "tx stocklevel");
          continue;
        }
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
    Statement statement = null;
    try {
      statement = con.createStatement();
      statement.execute("begin transaction");
      if (trace(obj) || traceString(obj)) {
        statement.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
      }
      else {
        statement.execute("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE");
      }
      statement.execute("select '" + transaction + "'");
    }
    catch (java.lang.Exception ex) {
      if (con != null) {
        RollbackTransaction(con, ex);
      }
      throw new java.sql.SQLException();
    }
    finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  public static void CommitTransaction(Connection con) throws java.sql.
      SQLException {
    {
      Statement statement = null;
      try {
        statement = con.createStatement();
        statement.execute("commit transaction");
      }
      catch (java.lang.Exception ex) {
        if (con != null) {
          RollbackTransaction(con, ex);
        }
        throw new java.sql.SQLException();
      }
      finally {
        if (statement != null) {
          statement.close();
        }
      }
    }
  }

  public static void RollbackTransaction(Connection con, Exception dump) throws
      java.sql.SQLException {
    Statement statement = null;
    try {
      statement = con.createStatement();
      statement.execute("rollback transaction");
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
    }
    finally {
      if (statement != null) {
        statement.close();
      }
    }
  }

  public static boolean trace(OutInfo obj) {
    return ( ( (String) obj.getInfo("trace")).equalsIgnoreCase("TRACE"));
  }

  public static boolean traceString(OutInfo obj) {
    return ( ( (String) obj.getInfo("trace")).equalsIgnoreCase(
        "TRACESTRING"));
  }
}
// arch-tag: 5e93fc99-eedb-49eb-af2a-bbdb57146184
