package escada.tpc.tpcc.database;

import escada.tpc.common.OutInfo;
import escada.tpc.common.database.CommonDatabase;

/**
 * It defines the common methods that must be available at any dbImplementation
 * in order to emulate the TPC-C benchmark.
 * Basically, it defines one method for each transaction available in the TPC-C.
 **/
abstract public class dbTPCCDatabase
    extends CommonDatabase {
 /**
  * It defines the behavior of the transaction new order.
  * 
  * @param OutInfo it contains information in the format string mapping to objet
  * in order to build the transaction request
  * @param String the host id to which the client is attached to
  * @return the result of the transaction 
  **/
  abstract public Object TraceNewOrderDB(OutInfo obj,String hid) throws java.sql.
      SQLException;

  /**
  * It defines the behavior of the transaction delivery.
  *
  * @param OutInfo it contains information in the format string mapping to objet  
  * in order to build the transaction request.
  * @param String the host id to which the client is attached to
  * @return the result of the transaction
  **/
  abstract public Object TraceDeliveryDB(OutInfo obj,String hid) throws java.sql.
      SQLException;

  /**
  * It defines the behavior of the transaction order status.
  *
  * @param OutInfo it contains information in the format string mapping to objet  
  * in order to build the transaction request
  * @param String the host id to which the client is attached to
  * @return the result of the transaction
  **/
  abstract public Object TraceOrderStatusDB(OutInfo obj,String hid) throws java.sql.
      SQLException;

  /**
  * It defines the behavior of the transaction payment.
  *
  * @param OutInfo it contains information in the format string mapping to objet  
  * in order to build the transaction request
  * @param String the host id to which the client is attached to
  * @return the result of the transaction
  **/
  abstract public Object TracePaymentDB(OutInfo obj,String hid) throws java.sql.
      SQLException;

  /**
  * It defines the behavior of the transaction stock level.
  *
  * @param OutInfo it contains information in the format string mapping to objet  
  * in order to build the transaction request
  * @param String the host id to which the client is attached to
  * @return the result of the transaction
  **/
  abstract public Object TraceStockLevelDB(OutInfo obj,String hid) throws java.sql.
      SQLException;
}// arch-tag: 44ab82c5-4413-4b5c-84e3-daaa94482efb
// arch-tag: 48688264-6e96-461a-b5e9-33580f67822f
// arch-tag: 6321f395-c60b-4df6-948d-b9fb7dce1475
