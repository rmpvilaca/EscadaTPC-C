package escada.tpc.tpcc.database.oracle;


import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import escada.tpc.common.*;
import escada.tpc.common.clients.*;
import escada.tpc.tpcc.database.*;
import escada.tpc.tpcc.trace.*;

import oracle.jdbc.oracore.OracleType;

/** It is an interface to a postgreSQL, which based is based on the
 * the distributions of the TPC-C.
 **/
public class dbOracle
extends dbTPCCDatabase {

    public static int NewOrderTransNum = 0;
    public static int PaymentTransNum = 0;
    public static int OrderStatusTransNum = 0;
    public static int StockLevelTransNum = 0;
    public static int DeliveryTransNum = 0;


    public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
	    SQLException {

        Connection con = null;
        HashSet dbtrace = null;

        try {
            StringBuffer lErro = new StringBuffer("0");

	    con = getConnection();

            dbLog.log("Beginning transaction new order (thread(" + Thread.currentThread().getName() + "))");

            Date lDateBegin1 = new Date();

            InitTransaction(obj, con, "tx neworder");

            dbtrace = NewOrderDB(lErro,obj, con);

	    CommitTransaction(con);

	    Date lDateEnd1 = new Date();

            dbLog.log("\tFinishing transaction new order (thread(" + Thread.currentThread().getName() + ")) T= "
		+(lDateEnd1.getTime()-lDateBegin1.getTime()) 
		+"\tB1 = "+lDateBegin1.getTime()
		+"\tE1 = "+lDateEnd1.getTime());
            //dbLog.logToFile(dbLog.NO,"NEW_ORDER = "+dbOracle.NewOrderTransNum++ +" T= "+(lDateEnd1.getTime()-lDateBegin2.getTime()) +" E= "+lErro+" T1= "+lDateBegin2.getTime()+" T2= "+lDateEnd1.getTime() +" Client= "+Thread.currentThread().getName());
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
            if (con != null) {
                RollbackTransaction(con, ex);
            }
        }
        finally {
            returnConnection(con);
        }
        return (dbtrace);
    }

    public HashSet NewOrderDB(StringBuffer pErro, OutInfo obj, Connection con) throws java.sql.
    SQLException {
        while (true) {
            CallableStatement statement = null;

            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_neworder(?,?,?,?,?,?,?,?); end;";

            HashSet dbtrace = new HashSet();
            StringBuffer iid = new StringBuffer();
            StringBuffer wid = new StringBuffer();
            StringBuffer qtdi = new StringBuffer();

            try {

                statement = con.prepareCall(query);

                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );



                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));

                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setInt(5, Integer.parseInt( (String) obj.getInfo("qtd")));
                statement.setInt(6, Integer.parseInt( (String) obj.getInfo("localwid")));

                int icont = 0;
                int qtd = Integer.parseInt( (String) obj.getInfo("qtd"));
                while (icont < qtd) {
                    iid.append( (String) obj.getInfo("iid" + icont));
                    iid.append(",");
                    wid.append((String) obj.getInfo("supwid" + icont));
                    wid.append(",");
                    qtdi.append( (String) obj.getInfo("qtdi" + icont));
                    qtdi.append(",");
                    icont++;
                }

                statement.setString(7, iid.toString());
                statement.setString(8, wid.toString());
                statement.setString(9, qtdi.toString());

                /*
                    Count the time dispended in the database execution
                 */
                Date startTime = new Date();
                Date endTime   = new Date();
                startTime = new Date();
                statement.execute();
                endTime   = new Date();
                dbLog.logToFile(dbLog.NO,"NEW_ORDER_EXECUTION_TIME = " +dbOracle.NewOrderTransNum++ 
					+" T= "+(endTime.getTime()-startTime.getTime()) 
					+" E= "+pErro+" T1= "+startTime.getTime()
					+" T2= "+endTime.getTime() 
					+" Client= "+Thread.currentThread().getName());



                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;

            }
            catch (java.sql.SQLException sqlex) {
                pErro.append("1");


                if(sqlex.getErrorCode()==1){
                    dbLog.log(Thread.currentThread().getName() + " NewOrder - SQL Exception " + "UNIQUE CONSTRAINT");
                }if(sqlex.getErrorCode()==1403){
        	    dbLog.log("ITEM : "+iid +" "+wid+" "+qtdi);
		}else{
                    dbLog.logException(sqlex);

                    dbLog.log(Thread.currentThread().getName() + " NewOrder - SQL Exception " + sqlex.getMessage());
                    if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)) {
                        RollbackTransaction(con, sqlex);
                        InitTransaction(obj, con, "tx neworder");
                        continue;
                    }
                }
            }
            catch (java.lang.Exception ex) {
                pErro.append("2");
                dbLog.log(Thread.currentThread().getName() + " NewOrder - General Exception " + ex.getMessage());
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
            StringBuffer lErro = new StringBuffer("0");

            con = getConnection();

            dbLog.log("Beginning trasaction delivery (thread(" + Thread.currentThread().getName() + "))");

            Date lDateBegin1 = new Date();

            InitTransaction(obj, con, "tx delivery");

            dbtrace = DeliveryDB(lErro, obj, con);

            CommitTransaction(con);

            Date lDateEnd1 = new Date();

            dbLog.log("\tFinishing trasaction delivery (thread(" + Thread.currentThread().getName() 
		    + "))\tB1 = "+lDateBegin1.getTime()
                    +"\tE1 = "+lDateEnd1.getTime());
            //dbLog.logToFile(dbLog.DL,"DELIVERY = "+dbOracle.DeliveryTransNum++ +" T= "+(lDateEnd1.getTime()-lDateBegin2.getTime()) +" E= "+lErro+" T1= "+lDateBegin2.getTime()+" T2= "+lDateEnd1.getTime()+" Client= "+Thread.currentThread().getName());
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
            if (con != null) {
                RollbackTransaction(con, ex);
            }
        }
        finally {
            returnConnection(con);
        }
        return (dbtrace);
    }

    public HashSet DeliveryDB(StringBuffer pErro,OutInfo obj, Connection con) throws java.sql.
    SQLException {
        while (true) {
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_delivery(?,?); end;";

            HashSet dbtrace = new HashSet();


            try {

                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("crid")));
                /*
                     Count the time dispended in the database execution
                 */

                Date startTime = new Date();
                Date endTime   = new Date();
                startTime = new Date();
                statement.execute();
                endTime   = new Date();
                dbLog.logToFile(dbLog.DL,"DELIVERY_EXECUTION_TIME = "+dbOracle.DeliveryTransNum++ 
				        +" T= "+(endTime.getTime()-startTime.getTime()) 
 					+" E= "+pErro+" T1= "+startTime.getTime()
					+" T2= "+endTime.getTime() 
					+" Client= "+Thread.currentThread().getName());



                rs = (ResultSet)statement.getObject(1);

                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
            }
            catch (java.sql.SQLException sqlex) {
                pErro.append("1");
                dbLog.log(Thread.currentThread().getName() + " Delivery - SQL Exception " + sqlex.getMessage());
                if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)){
                    RollbackTransaction(con, sqlex);
                    InitTransaction(obj, con, "tx delivery");
                    continue;
                }
            }
            catch (java.lang.Exception ex) {
                pErro.append("2");
                dbLog.log(Thread.currentThread().getName() + " Delivery - General Exception " + ex.getMessage());
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
        StringBuffer lErro = new StringBuffer("0");
        try {

            con = getConnection();

            dbLog.log("Beginning transaction order status (thread(" + Thread.currentThread().getName() + "))");
            //boolean lautocommit = con.getAutoCommit();
            //con.setAutoCommit(false);

            Date lDateBegin1 = new Date();

            InitTransaction(obj, con, "tx orderstatus");

            dbtrace = OrderStatusDB(lErro, obj, con);

            CommitTransaction(con);

            Date lDateEnd1 = new Date();

            //con.setAutoCommit(lautocommit);

            dbLog.log("\tFinishing transaction order status (thread(" + Thread.currentThread().getName() 
		    + "))\tB1 = "+lDateBegin1.getTime()
                    + "\tE1 = "+lDateEnd1.getTime());
            //dbLog.logToFile(dbLog.OS,"ORDER_STATUS = "+dbOracle.OrderStatusTransNum++ +" T= "+(lDateEnd1.getTime()-lDateBegin2.getTime()) +" E= "+lErro+" T1= "+lDateBegin2.getTime()+" T2= "+lDateEnd1.getTime()+" Client= "+Thread.currentThread().getName());
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
            if (con != null) {
                RollbackTransaction(con, ex);
            }
        }
        finally {
            returnConnection(con);
        }
        return (dbtrace);
    }


    public HashSet OrderStatusDB(StringBuffer pErro,OutInfo obj, Connection con) throws java.
    sql.SQLException {
        while (true) {
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_orderstatus(?,?,?,?); end;";
            HashSet dbtrace = new HashSet();

            try {
                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt((String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setString(5, (String) obj.getInfo("lastname")+"%");

                /*
                     Count the time dispended in the database execution
                 */

                Date startTime = new Date();
                Date endTime   = new Date();
                startTime = new Date();
                statement.execute();
                endTime   = new Date();
                dbLog.logToFile(dbLog.OS,"ORDER_STATUS_EXECUTION_TIME = "+dbOracle.OrderStatusTransNum++ 
					+" T= "+(endTime.getTime()-startTime.getTime()) 
					+" E= "+pErro+" T1= "+startTime.getTime()
					+" T2= "+endTime.getTime() 
					+" Client= "+Thread.currentThread().getName());


                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;

            }
            catch (java.sql.SQLException sqlex) {
                pErro.append("1");
                dbLog.log(Thread.currentThread().getName() + " OrderStatus - SQL Exception " + sqlex.getMessage());
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
                pErro.append("2");
                dbLog.log(Thread.currentThread().getName() + " OrderStatus - General Exception " + ex.getMessage());
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
        StringBuffer lErro = new StringBuffer("0");
        try {
            con = getConnection();

            dbLog.log("Beginning transaction payment (thread(" + Thread.currentThread().getName() + "))");

            Date lDateBegin1 = new Date();

            InitTransaction(obj, con, "tx payment");

            dbtrace = PaymentDB(lErro, obj, con);

            CommitTransaction(con);

            Date lDateEnd1 = new Date();

            dbLog.log("\tFinishing transaction payment (thread(" + Thread.currentThread().getName() 
		    + "))\tB1 = "+lDateBegin1.getTime()
		    + "\tE1 = "+lDateEnd1.getTime());
            //dbLog.logToFile(dbLog.PA,"PAYMENT = "+dbOracle.PaymentTransNum++ +" T= "+(lDateEnd1.getTime()-lDateBegin2.getTime()) +" E= "+lErro+" T1= "+lDateBegin2.getTime()+" T2= "+lDateEnd1.getTime()+" Client= "+Thread.currentThread().getName());
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
            if (con != null) {
                RollbackTransaction(con, ex);
            }
        }
        finally {
            returnConnection(con);
        }
        return (dbtrace);
    }

    public HashSet PaymentDB(StringBuffer  pErro,OutInfo obj, Connection con) throws java.sql.
    SQLException {
        while (true) {
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_payment(?,?,?,?,?,?,?); end;";
            HashSet dbtrace = new HashSet();

            try {

                statement = con.prepareCall(query);
                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );
                statement.setInt(2, Integer.parseInt( (String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("cwid")));
                statement.setFloat(4, Float.parseFloat( (String) obj.getInfo("hamount")));
                statement.setInt(5, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(6, Integer.parseInt( (String) obj.getInfo("cdid")));
                statement.setInt(7, Integer.parseInt( (String) obj.getInfo("cid")));
                statement.setString(8, ((String) obj.getInfo("lastname"))+"%");

                /*
                     Count the time dispended in the database execution
                 */

                Date startTime = new Date();
                Date endTime   = new Date();
                startTime = new Date();
                statement.execute();
                endTime   = new Date();
                dbLog.logToFile(dbLog.PA,"PAYMENT_EXECUTION_TIME = "+dbOracle.PaymentTransNum++ 
					+" T= "+(endTime.getTime()-startTime.getTime()) 
					+" E= "+pErro+" T1= "+startTime.getTime()
					+" T2= "+endTime.getTime() +" Client= "
					+Thread.currentThread().getName());


                rs = (ResultSet)statement.getObject(1);

                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
            }
            catch (java.sql.SQLException sqlex) {

                pErro.append("1");
                dbLog.log(Thread.currentThread().getName() + " Payment - SQL Exception " + sqlex.getMessage());
                dbLog.logException(sqlex);
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
                pErro.append("2");
                dbLog.log(Thread.currentThread().getName() + " Payment - General Exception " + ex.getMessage());
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
        StringBuffer lErro = new StringBuffer("0");
        try {
            con = getConnection();

            dbLog.log("Beginning transaction stock level (thread(" + Thread.currentThread().getName() + "))");

            Date lDateBegin1 = new Date();

            InitTransaction(obj, con, "tx stocklevel");

            dbtrace = StockLevelDB(lErro, obj, con);

            CommitTransaction(con);

            Date lDateEnd1 = new Date();

            dbLog.log("\tFinishing transaction stock level (thread(" + Thread.currentThread().getName() 
		    + "))\tB1 = "+lDateBegin1.getTime()
                    + "\tE1 = "+lDateEnd1.getTime());
            //dbLog.logToFile(dbLog.SL,"STOCK_LEVEL = "+dbOracle.StockLevelTransNum++ +" T= "+(lDateEnd1.getTime()-lDateBegin2.getTime()) +" E= "+lErro+" T1= "+lDateBegin2.getTime()+" T2= "+lDateEnd1.getTime()+" Client= "+Thread.currentThread().getName());
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
            if (con != null) {
                RollbackTransaction(con, ex);
            }
        }
        finally {
            returnConnection(con);
        }
        return (dbtrace);
    }


    public HashSet StockLevelDB(StringBuffer  pErro,OutInfo obj, Connection con) throws java.
    sql.SQLException {
        while (true) {
            CallableStatement statement = null;
            ResultSet rs = null;
            String cursor = null;
            String query = "begin ? := PKG_TPCC.tpcc_stocklevel(?,?,?); end;";
            HashSet dbtrace = new HashSet();

            try {
                statement = con.prepareCall(query);

                statement.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR );

                statement.setInt(2, Integer.parseInt( (String) obj.getInfo("wid")));
                statement.setInt(3, Integer.parseInt( (String) obj.getInfo("did")));
                statement.setInt(4, Integer.parseInt( (String) obj.getInfo("threshhold")));

                /*
                     Count the time dispended in the database execution
                 */

                Date startTime = new Date();
                Date endTime   = new Date();
                startTime = new Date();
                rs = statement.executeQuery();
                endTime   = new Date();
                dbLog.logToFile(dbLog.SL,"STOCK_LEVEL_EXECUTION_TIME = "+dbOracle.StockLevelTransNum++ 
					+" T= "+(endTime.getTime()-startTime.getTime()) 
					+" E= "+pErro+" T1= "+startTime.getTime()
					+" T2= "+endTime.getTime() 
					+" Client= "+Thread.currentThread().getName());

                //rs = statement.executeQuery();


                rs = (ResultSet)statement.getObject(1);
                if (rs.next()) {
                    cursor = (String) rs.getString(1);
                }
                rs.close();
                rs = null;
                statement.close();
                statement = null;
            }
            catch (java.sql.SQLException sqlex) {
                pErro.append("1");
                dbLog.log(Thread.currentThread().getName() + " StockLevel - SQL Exception " + sqlex.getMessage());
                if ((sqlex.getMessage().indexOf("serialize") != -1) || (sqlex.getMessage().indexOf("deadlock") != -1)) {
                    RollbackTransaction(con, sqlex);
                    InitTransaction(obj, con, "tx stocklevel");
                    continue;
                }
            }
            catch (java.lang.Exception ex) {
                pErro.append("2");
                dbLog.log(Thread.currentThread().getName() + " StockLevel - General Exception " + ex.getMessage());
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
            if (trace(obj) || traceString(obj)) {
                statement.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED");
            }
            else {
                statement.execute("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE");
            }
        }catch (java.sql.SQLException ex) {
            if (con != null) {
                RollbackTransaction(con, ex);
            }
            throw ex;
        }catch (java.lang.Exception ex) {
            dbLog.logException(ex);
        }finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void CommitTransaction(Connection con) throws java.sql.
    SQLException {
        {
            try {
                if(con!=null)  {
                    con.commit();
                }
            }
            catch (java.lang.Exception ex) {
                if (con != null) {
                    RollbackTransaction(con, ex);
                }
                throw new java.sql.SQLException();
            }

        }
    }

    public static void RollbackTransaction(Connection con, Exception dump) throws
    java.sql.SQLException {
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.execute("rollback");
        }
        catch (java.lang.Exception ex) {
            dbLog.logException(ex);
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
// arch-tag: 5fdbe754-a4ea-4fa6-b768-7ce766ea5c82
