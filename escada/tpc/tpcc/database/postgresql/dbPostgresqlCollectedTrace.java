package Escada.tpc.tpcc.database.postgresql;

import Escada.tpc.tpcc.database.*;
import Escada.tpc.common.*;
import Escada.tpc.common.trace.*;

import java.io.*;
import java.sql.*;
import java.util.*;

/** It is an interface to a postgreSQL, which based is based on the
* the distributions of the TPC-C.
**/
public class dbPostgresqlCollectedTrace
    extends dbPostgresql {

  public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    String tid = null;
    String cursor = null;
    int wid = Integer.parseInt( (String) obj.getInfo("wid"));
    Iterator i = null;

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      if (trace(obj)) {
        con = getConnection();
        InitTransaction(obj, con, "tx neworder");

        tid = dbTrace.initTransactionTrace("tx neworder",
                                           (String) obj.getInfo("thinktime"),hid);

        statement = con.prepareStatement("select tracetpcc_getdistrict (?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "district", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "district", wid, hid);

        dbtrace.clear();

        int icont = 0;
        int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
        while (icont < qtd) {
          statement = con.prepareStatement("select tracetpcc_getitem (?)");
          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("iid" + icont)));
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
          icont++;
        }
        dbTrace.TransactionTrace(dbtrace, "r", tid, "item", wid, hid);

        dbtrace.clear();

        icont = 0;
        qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
        while (icont < qtd) {
          statement = con.prepareStatement("select tracetpcc_getstock (?,?)");
          statement.setInt(1,
                           Integer.parseInt( (String) obj.getInfo("supwid" + icont)));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("iid" + icont)));
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
            if (Integer.parseInt( (String) obj.getInfo("supwid" + icont)) == wid) {
              dbtrace.add(rs.getString(1));
            }
            else {
              tmptrace.add(rs.getString(1));
              dbTrace.TransactionTrace(tmptrace, "r", tid, "stock",
                                       Integer.
                                       parseInt( (String) obj.getInfo("supwid" +
                  icont)),hid);
              dbTrace.TransactionTrace(tmptrace, "w", tid, "stock",
                                       Integer.
                                       parseInt( (String) obj.getInfo("supwid" +
                  icont)),hid);
              tmptrace.clear();
            }
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;
          icont++;
        }
        dbTrace.TransactionTrace(dbtrace, "r", tid, "stock", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "stock", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getcustomer (?,?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);

        dbtrace.clear();

        NewOrderDB(obj, con);

        tmptrace.clear();

        statement = con.prepareStatement(
            "{select tracetpcc_getcurrentneworder(?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
          tmptrace.add(rs.getString(1));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        if (tmptrace.size() == 0) {
          System.out.println(obj);
          throw new Exception("Induced");
        }

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getorderline(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
	i = tmptrace.iterator();
        statement.setInt(3, Integer.parseInt( (String) i.next()));
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
        dbTrace.TransactionTrace(dbtrace, "w", tid, "orderline", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getorders(?,?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
	i = tmptrace.iterator();
        statement.setInt(3, Integer.parseInt( (String) i.next()));
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
        dbTrace.TransactionTrace(dbtrace, "w", tid, "orders", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getneworder(?,?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
	i = tmptrace.iterator();
        statement.setInt(3, Integer.parseInt( (String) i.next()));
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
        dbTrace.TransactionTrace(dbtrace, "w", tid, "neworder", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getwarehouse(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "warehouse", wid, hid);

        dbtrace.clear();

        CommitTransaction(con);

        dbTrace.closeTransactionTrace(tid, (String) obj.getInfo("file"),hid);
      }
      else if (traceString(obj)) {
        int qtd = 0, icont = 0;

        con = getConnection();

        InitTransaction(obj, con, "tx neworder");

        NewOrderDB(obj, con);

        statement = con.prepareStatement("select tracetpcc_getdistrict (?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
          obj.putInfo("d_next_o_id", rs.getString("d_next_o_id"));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
        while (icont < qtd) {
          statement = con.prepareStatement("select tracetpcc_getstock (?,?)");
          statement.setInt(1,
                           Integer.parseInt( (String) obj.getInfo("supwid" + icont)));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("iid" + icont)));
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
            obj.putInfo("s_quantity" + icont, rs.getString("s_quantity"));
            obj.putInfo("s_ytd" + icont, rs.getString("s_ytd"));
            obj.putInfo("s_order_cnt" + icont, rs.getString("s_order_cnt"));
            obj.putInfo("s_remote_cnt" + icont, rs.getString("s_remote_cnt"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;

          statement = con.prepareStatement(
              "select tracetpcc_getexactorderline (?,?,?,?)");
          statement.setInt(1,
                           Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
          statement.setInt(3,
                           Integer.parseInt( (String) obj.getInfo("d_next_o_id")));
          statement.setInt(4, icont + 1);
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
            obj.putInfo("ol_amount" + icont, rs.getString("ol_amount"));
            obj.putInfo("ol_dist_info" + icont, rs.getString("ol_dist_info"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;

          icont++;
        }

        statement = con.prepareStatement("select tracetpcc_getorders (?,?,?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3,
                         Integer.parseInt( (String) obj.getInfo("d_next_o_id")));
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
          /**** ----------------------------------------------- ****/
          // BUG in the JDBC driver that does not allow to
          // access timestamp fields. See site to correct bug.
          // To circumvent the problem the field is constant...
          // obj.putInfo("o_entry_d",rs.getString("o_entry_d"));
          /**** ----------------------------------------------- ****/
          obj.putInfo("o_ol_cnt", rs.getString("o_ol_cnt"));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        dbTrace.compileTransactionTrace("neworder", obj);

        CommitTransaction(con);
      }
      else {
        con = getConnection();
        InitTransaction(obj, con, "tx neworder");

        NewOrderDB(obj, con);

        CommitTransaction(con);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (statement != null) {
        statement.close();
      }
      returnConnection(con);
    }
    return (dbtrace);
  }

  public Object TraceDeliveryDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    String tid = null;
    String cursor = null;
    Iterator i = null;
    int wid = Integer.parseInt( (String) obj.getInfo("wid"));

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      if (trace(obj)) {
        con = getConnection();
        InitTransaction(obj, con, "tx delivery");

        tid = dbTrace.initTransactionTrace("tx delivery",
                                           (String) obj.getInfo("thinktime"), hid);

        statement = con.prepareStatement("select tracetpcc_deliveryneworder(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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

        dbTrace.TransactionTrace(dbtrace, "r", tid, "neworder", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "neworder", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement(
            "select tracetpcc_latestneworderbydistrict(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "w", tid, "neworder", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_ordersbydistrict(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orders", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "orders", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement(
            "select tracetpcc_latestcustomerbydistrict(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "customer", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_linebydistrict(?)");
        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orderline", wid, hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "orderline", wid, hid);

        dbtrace.clear();

        DeliveryDB(obj, con);

        CommitTransaction(con);

        dbTrace.closeTransactionTrace(tid, (String) obj.getInfo("file"),hid);
      }
      else if (traceString(obj)) {
        int icont = 0, ndis = 10;

        con = getConnection();

        InitTransaction(obj, con, "tx delivery");

        obj.putInfo("ndis", "10");
        while (icont < ndis) {

          statement = con.prepareStatement(
              "select tracetpcc_exactdeliveryneworder(?,?)");
          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, icont + 1);
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
            obj.putInfo("oid" + icont, rs.getString("no_o_id"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;

          statement = con.prepareStatement(
              "select tracetpcc_getorders(?,?,?)");
          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, icont + 1);
          statement.setInt(3,
                           Integer.parseInt( (String) obj.getInfo("oid" + icont)));
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
            obj.putInfo("cid" + icont, rs.getString("o_c_id"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;

          icont++;
        }

        DeliveryDB(obj, con);

        icont = 0;
        while (icont < ndis) {
          statement = con.prepareStatement(
              "select tracetpcc_getcustomer(?,?,?)");
          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, icont + 1);
          statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid" + icont)));
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
            obj.putInfo("c_balance" + icont, rs.getString("c_balance"));
            obj.putInfo("c_delivery_cnt" + icont, rs.getString("c_delivery_cnt"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;

          icont++;
        }

        dbTrace.compileTransactionTrace("delivery", obj);

        CommitTransaction(con);
      }
      else {
        con = getConnection();
        InitTransaction(obj, con, "tx delivery");

        DeliveryDB(obj, con);

        CommitTransaction(con);
      }

    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (statement != null) {
        statement.close();
      }
      returnConnection(con);
    }
    return (dbtrace);
  }

  public Object TraceOrderStatusDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    String tid = null;
    String cursor = null;
    String cid = null;
    Iterator i = null;
    int wid = Integer.parseInt( (String) obj.getInfo("wid"));

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      if (trace(obj)) {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");
        if (str.equals("0")) {

          InitTransaction(obj, con, "tx orderstatus 01");
          tid = dbTrace.initTransactionTrace("tx orderstatus 01",
                                             (String) obj.getInfo("thinktime"),hid);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomerbyname(?,?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
          statement.setInt(3, 0);
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
          dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);

          if (dbtrace.size() == 0) {
            System.out.println(obj);
            throw new Exception("Induced");
          }
	  i = dbtrace.iterator();
          tmptrace.add(i.next());
          dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", wid, hid);

          dbtrace.clear();
        }
        else {
          InitTransaction(obj, con, "tx orderstatus 02");
          tid = dbTrace.initTransactionTrace("tx orderstatus 02",
                                             (String) obj.getInfo("thinktime"),hid);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomer(?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
          statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
	  i = dbtrace.iterator();
          tmptrace.add(i.next());

          dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);

          dbtrace.clear();
        }
	i = tmptrace.iterator();	
        cid = (String) i.next();

        tmptrace.clear();
        tmptrace = OrderStatusDB(obj, con);

        if (tmptrace.size() == 0) {
          System.out.println(obj);
          throw new Exception("Induced");
        }

        statement = con.prepareStatement(
            "select tracetpcc_getordersbycustomer(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt(cid));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orders", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getorders(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
	i = tmptrace.iterator();	
        statement.setInt(3, Integer.parseInt( (String) i.next()));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orders", wid, hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getorderline(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
	i = tmptrace.iterator();
        statement.setInt(3, Integer.parseInt( (String) i.next()));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orderline", wid, hid);

        tmptrace.clear();
        dbtrace.clear();

        CommitTransaction(con);

        dbTrace.closeTransactionTrace(tid, (String) obj.getInfo("file"),hid);
      }
      else if (traceString(obj)) {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");
        if (str.equals("0")) {
          InitTransaction(obj, con, "tx orderstatus 01");

          OrderStatusDB(obj, con);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomerbyname(?,?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
          statement.setInt(3, 0);
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

          obj.putInfo("abort", "1");
          while (rs.next()) {
            obj.putInfo("cid", rs.getString("c_id"));
            obj.putInfo("abort", "0");
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;
        }
        else {
          InitTransaction(obj, con, "tx orderstatus 02");

          OrderStatusDB(obj, con);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomer(?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
          statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
            obj.putInfo("lastname", rs.getString("c_last"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;
        }
        statement = con.prepareStatement(
            "select tracetpcc_getordersbycustomer(?,?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
        statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
          obj.putInfo("oid", rs.getString("o_id"));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        dbTrace.compileTransactionTrace("orderstatus", obj);

        CommitTransaction(con);
      }
      else {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");
        if (str.equals("0")) {
          InitTransaction(obj, con, "tx orderstatus 01");
        }
        else {
          InitTransaction(obj, con, "tx orderstatus 02");
        }

        OrderStatusDB(obj, con);

        CommitTransaction(con);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (statement != null) {
        statement.close();
      }
      returnConnection(con);
    }
    return (dbtrace);
  }

  public Object TracePaymentDB(OutInfo obj,String hid) throws java.sql.SQLException {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    String tid = null;
    String cursor = null;
    Iterator i = null;
    int wid = Integer.parseInt( (String) obj.getInfo("cwid"));

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      if (trace(obj)) {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");

        if (str.equals("0")) {
          InitTransaction(obj, con, "tx payment 01");
          tid = dbTrace.initTransactionTrace("tx payment 01",
                                             (String) obj.getInfo("thinktime"),hid);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomerbyname(?,?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("cwid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cdid")));
          statement.setInt(3, 0);
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
          dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);

          if (dbtrace.size() == 0) {
            System.out.println(obj);
            throw new Exception("Induced");
          }
	  i = dbtrace.iterator();	  
          tmptrace.add(i.next());
          dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", wid, hid);

          dbtrace.clear();
        }
        else {
          InitTransaction(obj, con, "tx payment 02");
          tid = dbTrace.initTransactionTrace("tx payment 02",
                                             (String) obj.getInfo("thinktime"),hid);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomer(?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("cwid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cdid")));
          statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
	  i = dbtrace.iterator();	  
          tmptrace.add(i.next());
          dbTrace.TransactionTrace(dbtrace, "r", tid, "customer", wid, hid);

          dbtrace.clear();
        }

        dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", wid, hid);
        dbTrace.TransactionTrace(tmptrace, "w", tid, "customer", wid, hid);

        // In the worst case, we consider the situation that the costumer
        // must has c_credit = BC
        dbTrace.TransactionTrace(tmptrace, "r", tid, "customer", wid, hid);
        dbTrace.TransactionTrace(tmptrace, "w", tid, "customer", wid, hid);

        tmptrace.clear();

        statement = con.prepareStatement("select tracetpcc_getdistrict(?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "district",
                                 Integer.parseInt( (String) obj.getInfo("wid")),hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "district",
                                 Integer.parseInt( (String) obj.getInfo("wid")),hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_getwarehouse(?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "warehouse",
                                 Integer.parseInt( (String) obj.getInfo("wid")),hid);
        dbTrace.TransactionTrace(dbtrace, "w", tid, "warehouse",
                                 Integer.parseInt( (String) obj.getInfo("wid")),hid);

        dbtrace.clear();

        // We are not considering access to the history relation
        // because there is not conflict...
        PaymentDB(obj, con);

        CommitTransaction(con);

        dbTrace.closeTransactionTrace(tid, (String) obj.getInfo("file"),hid);
      }
      else if (traceString(obj)) {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");
        if (str.equals("0")) {
          InitTransaction(obj, con, "tx payment 01");

          PaymentDB(obj, con);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomerbyname(?,?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("cwid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cdid")));
          statement.setInt(3, 0);
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

          obj.putInfo("abort", "1");
          while (rs.next()) {
            obj.putInfo("cid", rs.getString("c_id"));
            obj.putInfo("c_balance", rs.getString("c_balance"));
            obj.putInfo("c_ytd_payment", rs.getString("c_ytd_payment"));
            obj.putInfo("c_payment_cnt", rs.getString("c_payment_cnt"));
            obj.putInfo("c_delivery_cnt", rs.getString("c_delivery_cnt"));
            obj.putInfo("c_data", rs.getString("c_data"));
            obj.putInfo("abort", "0");
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;
        }
        else {
          InitTransaction(obj, con, "tx payment 02");

          PaymentDB(obj, con);

          statement = con.prepareStatement(
              "select tracetpcc_getcustomer(?,?,?)");

          statement.setInt(1, Integer.parseInt( (String) obj.getInfo("cwid")));
          statement.setInt(2, Integer.parseInt( (String) obj.getInfo("cdid")));
          statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cid")));
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
            obj.putInfo("lastname", rs.getString("c_last"));
            obj.putInfo("c_balance", rs.getString("c_balance"));
            obj.putInfo("c_ytd_payment", rs.getString("c_ytd_payment"));
            obj.putInfo("c_payment_cnt", rs.getString("c_payment_cnt"));
            obj.putInfo("c_delivery_cnt", rs.getString("c_delivery_cnt"));
            obj.putInfo("c_data", rs.getString("c_data"));
          }
          rs.close();
          rs = null;
          statement.close();
          statement = null;
        }

        statement = con.prepareStatement(
            "select tracetpcc_getdistrict(?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
          obj.putInfo("d_ytd", rs.getString("d_ytd"));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        statement = con.prepareStatement(
            "select tracetpcc_getwarehouse(?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
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
          obj.putInfo("w_ytd", rs.getString("w_ytd"));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        dbTrace.compileTransactionTrace("payment", obj);

        CommitTransaction(con);

      }
      else {
        con = getConnection();

        String str = (String) (obj).getInfo("cid");
        if (str.equals("0")) {
          InitTransaction(obj, con, "tx payment 01");
        }
        else {
          InitTransaction(obj, con, "tx payment 02");
        }

        PaymentDB(obj, con);

        CommitTransaction(con);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (statement != null) {
        statement.close();
      }
      returnConnection(con);
    }
    return (dbtrace);
  }

  public Object TraceStockLevelDB(OutInfo obj,String hid) throws java.sql.
      SQLException {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    String tid = null;
    String cursor = null;
    Iterator i = null;
    int wid = Integer.parseInt( (String) obj.getInfo("wid"));

    HashSet dbtrace = new HashSet();
    HashSet tmptrace = new HashSet();

    try {
      if (trace(obj)) {
        con = getConnection();
        InitTransaction(obj, con, "tx stocklevel");

        tid = dbTrace.initTransactionTrace("tx stocklevel",
                                           (String) obj.getInfo("thinktime"),hid);

        statement = con.prepareStatement("select tracetpcc_getdistrict(?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "district", wid,hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_stocklevel01(?,?,?)");

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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "stock", wid,hid);

        dbtrace.clear();

        statement = con.prepareStatement("select tracetpcc_stocklevel02(?,?,?)");

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
        dbTrace.TransactionTrace(dbtrace, "r", tid, "orderline", wid, hid);

        dbtrace.clear();

        StockLevelDB(obj, con);

        CommitTransaction(con);

        dbTrace.closeTransactionTrace(tid, (String) obj.getInfo("file"),hid);
      }
      else if (traceString(obj)) {
        con = getConnection();

        InitTransaction(obj, con, "tx stocklevel");

        statement = con.prepareStatement("select tracetpcc_getdistrict(?,?)");

        statement.setInt(1, Integer.parseInt( (String) obj.getInfo("wid")));
        statement.setInt(2, Integer.parseInt( (String) obj.getInfo("did")));
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
          int oid = rs.getInt("d_next_o_id");
          int high = oid - 1;
          int low = oid - 20;
          obj.putInfo("high", Integer.toString(high));
          obj.putInfo("low", Integer.toString(high));
        }
        rs.close();
        rs = null;
        statement.close();
        statement = null;

        StockLevelDB(obj, con);

        dbTrace.compileTransactionTrace("stocklevel", obj);

        CommitTransaction(con);
      }
      else {
        con = getConnection();
        InitTransaction(obj, con, "tx stocklevel");

        StockLevelDB(obj, con);

        CommitTransaction(con);
      }
    }
    catch (java.lang.Exception ex) {
      ex.printStackTrace();
      if (con != null) {
        RollbackTransaction(con, ex);
      }
    }
    finally {
      if (rs != null) {
        rs.close();
      }
      if (statement != null) {
        statement.close();
      }
      returnConnection(con);
    }
    return (dbtrace);
  }
}
// arch-tag: f64ca957-189d-4961-bd07-f9b6d3ac6c6d
