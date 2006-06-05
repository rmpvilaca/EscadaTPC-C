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
			pstmt=conn.prepareStatement("insert into warehouse values (?,?,?,?,?,?,?,?,?,?)");
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
						pstmt.setInt(10,0);
						pstmt.executeUpdate();
				
			}
			conn.commit();
			
			System.out.println("populating district...");
			pstmt=conn.prepareStatement("insert into district values (?,?,?,?,?,?,?,?,?,?,?,?)");
			
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
						pstmt.setInt(12,0);
						pstmt.executeUpdate();
						}
				}
			conn.commit();
		
			System.out.println("populating customer...");
			pstmt=conn.prepareStatement("insert into customer values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
							pstmt.setInt(22,0);
							pstmt.executeUpdate();
					 }
				 }
			 }	
			 conn.commit();
			
			 System.out.println("populating history...");
			pstmt=conn.prepareStatement("insert into history values (?,?,?,?,?,?,?,?)");
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
			pstmt=conn.prepareStatement("insert into orders values (?,?,?,?,?,?,?,?,?)");
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
						 	pstmt.setInt(9,0);
						 	pstmt.executeUpdate();
					 	}
					 }
			}
			conn.commit();
			
			System.out.println("populating new_order...");
			pstmt=conn.prepareStatement("insert into new_order values (?,?,?,?)");
			 for(int i=1;i<(numWareh+1);i++){
				 for(int j=1;j<11;j++){
					 for(int a=2101;a<3001;a++){
						 pstmt.setInt(1,a);
						 pstmt.setInt(2,j);
						 pstmt.setInt(3,i);
						 pstmt.setInt(4,0);
						 pstmt.executeUpdate();
					 }
					 }
			}
			
			conn.commit();
			
			System.out.println("populating item...");
			pstmt=conn.prepareStatement("insert into item values (?,?,?,?,?,?)");

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
				pstmt.setInt(6,0);
				pstmt.executeUpdate();
			}
			conn.commit();
			
			System.out.println("populating stock...");
			pstmt=conn.prepareStatement("insert into stock values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
						pstmt.setInt(18,0);
						pstmt.executeUpdate();
					
				}	
				
			}
			conn.commit();
			
			System.out.println("populating order_line...");
			pstmt=conn.prepareStatement("insert into order_line values (?,?,?,?,?,?,?,?,?,?,?)");
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
							 pstmt.setInt(11,0);
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
	
	public static void main(String[] args)
	{
		String DB=args[0];
		int numberOfwarehouse=Integer.parseInt(args[1]);
		
		String sDBurl=null;
		if(DB.compareTo("D")==0)
		{
			try{Class.forName("org.apache.derby.jdbc.ClientDriver");}
			catch (ClassNotFoundException e){System.err.println("Driver not found");}
			sDBurl="jdbc:derby://localhost:1527/test;create=false";
		} 		
		else if(DB.compareTo("M")==0)
		{
			try{Class.forName("com.mysql.jdbc.Driver").newInstance();}
			catch (Exception e){System.err.println("Driver not found");}
			sDBurl="jdbc:mysql://localhost/tpcc?client";
		} 		
		else {System.err.println("Unknown DB identifier");System.exit(-1);}
		Connection conn=null;
		
		try
		{	
			conn=DriverManager.getConnection(sDBurl);
			conn.setAutoCommit(false);
			System.out.println("population:");
			new Populate(conn,numberOfwarehouse);
			System.out.println("done! ");
		}
		catch (SQLException e)
		{
			System.err.println("getConnection error");
			e.printStackTrace();
		}
		
		
		
	}
}

// arch-tag: cf53684c-f303-4705-8276-1b7e370e7bee
