package escada.tpc.tpcc.database.populate;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


public class Populate 
{
	public Populate(Connection conn,int numWareh)
	{
		
		PreparedStatement pstmt=null;
		Random rg=new Random(71830);
		Timestamp tStamp;
		try
		{	System.out.println("start...");
			System.out.println("populating warehouse...");
			pstmt=conn.prepareStatement("insert into warehouse (w_id,w_name,w_street_1," +
					"w_street_2,w_city,w_state,w_zip,w_tax,w_ytd) " +
					"values (?,?,?,?,?,?,?,?,?)");
			for(int j=1;j<(numWareh+1);j++)
			{
						pstmt.setInt(1,j);
						pstmt.setString(2,generateString(10));
						pstmt.setString(3,generateString(20));
						pstmt.setString(4,generateString(20));
						pstmt.setString(5,generateString(20));
						pstmt.setString(6,generateString(2));
						pstmt.setString(7,generateString(8));
						pstmt.setFloat(8,(rg.nextFloat())/5);
						pstmt.setInt(9,300000);
						//pstmt.setInt(10,0);
						pstmt.executeUpdate();
				
			}
			conn.commit();
			
			System.out.println("populating district...");
			pstmt=conn.prepareStatement("insert into district (d_id,d_w_id,d_name," +
					"d_street_1,d_street_2,d_city,d_state,d_zip,d_tax,"+
					"d_ytd,d_next_o_id) values (?,?,?,?,?,?,?,?,?,?,?)");
			
			for(int j=1;j<11;j++)
			{			
						for(int k=1;k<(numWareh+1);k++){
						pstmt.setInt(1,j);
						pstmt.setInt(2,k);
						pstmt.setString(3,generateString(10));
						pstmt.setString(4,generateString(20));
						pstmt.setString(5,generateString(20));
						pstmt.setString(6,generateString(20));
						pstmt.setString(7,generateString(2));
						pstmt.setString(8,generateString(8));
						pstmt.setFloat(9,(rg.nextFloat())/5);
						pstmt.setInt(10,30000);
						pstmt.setInt(11,3001);
						//pstmt.setInt(12,0);
						pstmt.executeUpdate();
						}
				}
			conn.commit();
		
			System.out.println("populating customer...");
			pstmt=conn.prepareStatement("insert into customer (c_id,c_d_id,c_w_id,c_first," +
					"c_middle,c_last,c_street_1,c_street_2,c_city,c_state,c_zip,c_phone," +
					"c_since,c_credit,c_credit_lim,c_discount,c_balance," +
					"c_ytd_payment,c_payment_cnt,c_delivery_cnt,c_data)" +
					" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			 for(int i=1;i<(numWareh+1);i++){
				 for(int j=1;j<11;j++){
					 for(int a=1;a<3001;a++){
						 	pstmt.setInt(1,a);
							pstmt.setInt(2,j);
							pstmt.setInt(3,i);
							pstmt.setString(4,generateString(16));
							pstmt.setString(5,"OE");
							pstmt.setString(6,generateString(0));
							pstmt.setString(7,generateString(20));
							pstmt.setString(8,generateString(20));
							pstmt.setString(9,generateString(20));
							pstmt.setString(10,generateString(2));
							pstmt.setString(11,generateString(8));
							pstmt.setString(12,generateString(16));
							
							tStamp=new Timestamp(System.currentTimeMillis());
							pstmt.setTimestamp(13,tStamp);
							if(rg.nextInt(100)<10)
								pstmt.setString(14,"BC");
							else
								pstmt.setString(14,"GC");
							pstmt.setInt(15,50000);
							pstmt.setFloat(16,(rg.nextFloat())/2);
							pstmt.setInt(17,-10);
							pstmt.setInt(18,10);
							pstmt.setInt(19,1);
							pstmt.setInt(20,0);
							pstmt.setString(21, generateString(300));
							//pstmt.setInt(22,0);
							pstmt.executeUpdate();
					 }
				 }
			 }	
			 conn.commit();
			
			 System.out.println("populating history...");
			pstmt=conn.prepareStatement("insert into history " +
					"(h_c_id,h_c_d_id,h_c_w_id,h_d_id,h_w_id,h_date," +
					"h_amount,h_data) values (?,?,?,?,?,?,?,?)");
			for(int j=1;j<(numWareh+1);j++)
			{
				for(int i=1;i<11;i++){
					for(int a=1;a<3001;a++){
						pstmt.setInt(1,a);
						pstmt.setInt(2,i);
						pstmt.setInt(3,j);
						pstmt.setInt(4,i);
						pstmt.setInt(5,j);
						tStamp=new Timestamp(System.currentTimeMillis());
						pstmt.setTimestamp(6,tStamp);
						pstmt.setInt(7,10);
						pstmt.setString(8,generateString(24));
						pstmt.executeUpdate();
					}
				}	
				
			}
			conn.commit();
			
			System.out.println("populating orders...");
			pstmt=conn.prepareStatement("insert into orders " +
					"(o_id,o_d_id,o_w_id,o_c_id,o_entry_d,o_carrier_id,"+
					"o_ol_cnt,o_all_local) values (?,?,?,?,?,?,?,?)");
			int b=1;
			 for(int i=1;i<(numWareh+1);i++){
				 for(int j=1;j<11;j++){
					 for(int a=1;a<3001;a++){
						 	pstmt.setInt(1,a);
						 	pstmt.setInt(2,j);
						 	pstmt.setInt(3,i);
						 	pstmt.setInt(4,b);
						 	tStamp=new Timestamp(System.currentTimeMillis());
						 	pstmt.setTimestamp(5,tStamp);
						 	if(a<2101)
						 		pstmt.setInt(6,rg.nextInt(10)+1);
						 	else
						 		pstmt.setInt(6,0);
						 	pstmt.setInt(7,rg.nextInt(10)+5);
						 	pstmt.setInt(8,1);
						 	b++;
						 	if(b>3000)b=1;
						 	//pstmt.setInt(9,0);
						 	pstmt.executeUpdate();
					 	}
					 }
			}
			conn.commit();
	
			System.out.println("populating new_order...");
			pstmt=conn.prepareStatement("insert into new_order (no_o_id,no_d_id," +
					"no_w_id) values (?,?,?)");
			 for(int i=1;i<(numWareh+1);i++){
				 for(int j=1;j<11;j++){
					 for(int a=2101;a<3001;a++){
						 pstmt.setInt(1,a);
						 pstmt.setInt(2,j);
						 pstmt.setInt(3,i);
						 //pstmt.setInt(4,0);
						 pstmt.executeUpdate();
					 }
					 }
			}
			
			conn.commit();
			
			System.out.println("populating item...");
			pstmt=conn.prepareStatement("insert into item (i_id,i_im_id,i_name," +
					"i_price,i_data) values (?,?,?,?,?)");

			for(int i=1;i<100001;i++){
				pstmt.setInt(1,i);
				int j=rg.nextInt(10000)+1;
				pstmt.setInt(2,j);
				pstmt.setString(3,generateString(20));
				pstmt.setDouble(4,(rg.nextDouble()+100.0)/100.0);
				if(rg.nextInt(100)<10)
					pstmt.setString(5,generateString(22).concat("ORIGINAL"));
				else
					pstmt.setString(5,generateString(30));
				//pstmt.setInt(6,0);
				pstmt.executeUpdate();
			}
			conn.commit();
			
			System.out.println("populating stock...");
			pstmt=conn.prepareStatement("insert into stock " +
					"(s_i_id,s_w_id,s_quantity,s_dist_01,s_dist_02,s_dist_03,"+
					"s_dist_04,s_dist_05,s_dist_06,s_dist_07,s_dist_08,"+
					"s_dist_09,s_dist_10,s_ytd,s_order_cnt,s_remote_cnt,s_data) "+
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for(int j=1;j<100001;j++)
			{
				for(int i=1;i<numWareh+1;i++){
					
						pstmt.setInt(1,j);
						pstmt.setInt(2,i);
						pstmt.setInt(3,rg.nextInt(90)+10);
						pstmt.setString(4,generateString(24));
						pstmt.setString(5,generateString(24));
						pstmt.setString(6,generateString(24));
						pstmt.setString(7,generateString(24));
						pstmt.setString(8,generateString(24));
						pstmt.setString(9,generateString(24));
						pstmt.setString(10,generateString(24));
						pstmt.setString(11,generateString(24));
						pstmt.setString(12,generateString(24));
						pstmt.setString(13,generateString(24));
						pstmt.setInt(14,0);
						pstmt.setInt(15,0);
						pstmt.setInt(16,0);
						if(rg.nextInt(100)<10)
							pstmt.setString(17,generateString(22).concat("ORIGINAL"));
						else
							pstmt.setString(17,generateString(30));
						//pstmt.setInt(18,0);
						pstmt.executeUpdate();
					
				}	
				
			}
			conn.commit();
			
			System.out.println("populating order_line...");
			pstmt=conn.prepareStatement("insert into order_line  (ol_o_id,ol_d_id,ol_w_id," +
					"ol_number,ol_i_id,ol_supply_w_id,ol_delivery_d,ol_quantity,ol_amount,"+
					"ol_dist_info) values (?,?,?,?,?,?,?,?,?,?)");
			for(int i=1;i<(numWareh+1);i++){
				 for(int j=1;j<11;j++){
					 for(int a=1;a<3001;a++){
						 int ol_cnt=rg.nextInt(10)+5;
						 for(int c=1;c<ol_cnt;c++){
							 pstmt.setInt(1,a);
							 pstmt.setInt(2,j);
							 pstmt.setInt(3,i);
							 pstmt.setInt(4,c);
							 pstmt.setInt(5,rg.nextInt(100000)+1);
							 pstmt.setInt(6,i);
							 tStamp=new Timestamp(System.currentTimeMillis());
							 pstmt.setTimestamp(7,tStamp);
							 pstmt.setInt(8,5);
							 if(a<2101)
								 pstmt.setInt(9,0);
							 else
								 pstmt.setInt(9,rg.nextInt(10000));
							 pstmt.setString(10,generateString(24));
							 //pstmt.setInt(11,0);
							 pstmt.executeUpdate();
						 }
					 	}
					 }
			}
			
			conn.commit();		
			pstmt.close();
			conn.close();
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
			try{conn.rollback();}catch(SQLException ee){ee.printStackTrace();System.exit(-1);}
		}
		
	}
	public Populate(int numWareh){
		
	}
	public String generateString(int length){
		String rString="";
		Map lastName=new HashMap();
		Random rg=new Random();
		int number=0;
		int prevNumber=0;
		String sDummy="";
		if(length==0){
		lastName.put(new Integer(0),"BAR");
		lastName.put(new Integer(1),"OUGHT");
		lastName.put(new Integer(2),"ABLE");
		lastName.put(new Integer(3),"PRI");
		lastName.put(new Integer(4),"PRES");
		lastName.put(new Integer(5),"ESE");
		lastName.put(new Integer(6),"ANTI");
		lastName.put(new Integer(7),"CALLY");
		lastName.put(new Integer(8),"ATION");
		lastName.put(new Integer(9),"EING");
		for(int i=0;i<3;i++){
			while(number==prevNumber)
				number=rg.nextInt(10);
			prevNumber=number;
			sDummy=(String)lastName.get((new Integer(number)));
			rString=rString.concat(sDummy);
		}
		}else{
			sDummy = Long.toString(Math.abs(rg.nextLong()), 36);//13 characters long
			rString=sDummy;
			for(int i=0;i<length/13;i++){
				rString=rString.concat(sDummy);
			}
			if(length<rString.length()) rString=rString.substring(0,length);
			
		}
		
		return rString;
	}
	
	public static void printUsage()
	{
		System.err.println("Usage:");
		System.err.println("java NUMBER_WAREHOUSES [JDBC_URL] [USERNAME] [DRIVER]");
		System.exit(2);
	}
	
	public static void main(String[] args) 
	{
		int numberOfwarehouse=1;
		String driver="org.postgresql.Driver";
		String jdbc="jdbc:postgresql://localhost/tpcc";
		String userName="rmpvilaca";
		String password="";
		if (args.length>=1)
		{
			numberOfwarehouse=Integer.parseInt(args[0]);
			if (args.length>=2)
				jdbc=args[1];
			if (args.length>=3)
				userName=args[2];
			if (args.length>=4)
				password=args[3];
			if (args.length>=5)
				driver=args[4];
		}
		else
			printUsage();
		
		try
		{	
			Class.forName(driver);
			Connection conn=DriverManager.getConnection(jdbc,userName,password);
			conn.setAutoCommit(false);
			System.out.println("population:");
			new Populate(conn,numberOfwarehouse);
			System.out.println("done! ");
		}
		catch (SQLException e)
		{
			System.err.println("getConnection error");
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
