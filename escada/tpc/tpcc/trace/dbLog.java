package escada.tpc.tpcc.trace;

import java.util.*;
import java.io.*;

public class dbLog {
    
    public static DataOutputStream dataOutputStreamNO;
    public static FileOutputStream fileOutputStreamNO;
    public static DataOutputStream dataOutputStreamPA;
    public static FileOutputStream fileOutputStreamPA;
    public static DataOutputStream dataOutputStreamOS;
    public static FileOutputStream fileOutputStreamOS;
    public static DataOutputStream dataOutputStreamDL;
    public static FileOutputStream fileOutputStreamDL;
    public static DataOutputStream dataOutputStreamSL;
    public static FileOutputStream fileOutputStreamSL;
    public static DataOutputStream dataOutputStream;
    public static FileOutputStream fileOutputStream;
                                                                                
    public static String NO = "NO";
    public static String DL = "DL";
    public static String SL = "SL";
    public static String PA = "PA";
    public static String OS = "OS";
    public static String LOG = "LOG";

    public static void log(String plog){
        System.out.println(plog);
    }

    public static void logException(Exception e){
        StackTraceElement ste[] = e.getStackTrace();
        dbLog.log("Exception: "+e.getMessage());
	for(int i = 0; i < e.getStackTrace().length; i++){
		dbLog.logToFile("","Exception: "+(e.getStackTrace())[i]+"\n");
	}

    }
    
    public static void logToFile(String pfile, String plog){
        try{
            if(pfile.equals("NO")){
		synchronized(dbLog.NO){
                	dbLog.dataOutputStreamNO.writeBytes(plog+"\n");
		}
            }else if(pfile.equals("DL")){
		synchronized(dbLog.DL){
                dbLog.dataOutputStreamDL.writeBytes(plog+"\n");                
		}
            }else if(pfile.equals("OS")){
		synchronized(dbLog.OS){
	                dbLog.dataOutputStreamOS.writeBytes(plog+"\n");
		}
            }else if(pfile.equals("PA")){
		synchronized(dbLog.PA){
                dbLog.dataOutputStreamPA.writeBytes(plog+"\n");
		}
            }else if(pfile.equals("SL")){
		synchronized(dbLog.SL){
                dbLog.dataOutputStreamSL.writeBytes(plog+"\n");
		}
            }
	    synchronized(dbLog.LOG){
                dbLog.dataOutputStream.writeBytes(plog+"\n");
	    }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void openFile(){
        try{long l = (new Date()).getTime();
            fileOutputStreamDL = new FileOutputStream("./log_"+l+"_TPCC_Delivery.txt");
            dataOutputStreamDL = new DataOutputStream(fileOutputStreamDL);
            
            fileOutputStreamNO = new FileOutputStream("./log_"+l+"_TPCC_NewOrder.txt");
            dataOutputStreamNO = new DataOutputStream(fileOutputStreamNO);
            
            fileOutputStreamOS = new FileOutputStream("./log_"+l+"_TPCC_OrderStatus.txt");
            dataOutputStreamOS = new DataOutputStream(fileOutputStreamOS);
            fileOutputStreamPA = new FileOutputStream("./log_"+l+"_TPCC_Payment.txt");
            dataOutputStreamPA = new DataOutputStream(fileOutputStreamPA);
            fileOutputStreamSL = new FileOutputStream("./log_"+l+"_TPCC_StockLevel.txt");
            dataOutputStreamSL = new DataOutputStream(fileOutputStreamSL);
            
            fileOutputStream = new FileOutputStream("./log_"+l+"_TPCC.txt");
            dataOutputStream = new DataOutputStream(fileOutputStream);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static void closeFile(){
        try{
            dataOutputStreamDL.flush();
            fileOutputStreamDL.close();
            dataOutputStreamNO.flush();
            fileOutputStreamNO.close();
            dataOutputStreamOS.flush();
            fileOutputStreamOS.close();
            dataOutputStreamPA.flush();
            fileOutputStreamPA.close();
            dataOutputStreamSL.flush();
            fileOutputStreamSL.close();
            dataOutputStream.flush();
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
   }
}
