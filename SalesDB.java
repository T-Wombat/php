package inventory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import dataBaseAccess.Login;
import gui.Sale;
import gui.StockItem;

// File Name Changed from Sale to SalesDB to prevent clashes with GUI Sale.java

// See Inventory.Java for SQL method Comments

public class SalesDB
{
	 protected Connection connect = null;
	 protected Statement statement = null;
	 protected PreparedStatement preparedStatement = null;
	 protected ResultSet resultSet = null;
	 protected int amount = 0;
	 protected int bar = 0;
	 protected int obsolescence = 0; // 0 = not discontinues, 1 = discontinued
	 protected double price = 0; // adds zero price
	 protected String name = "";
	 protected String category = "";
	 protected ResultSet SaleRecordSet = null;
	 protected ResultSet SaleItemSet = null;
	private String Staff_id;
	
	 public void AddSaleRec(String Sale_id, String Staff_id, LocalDateTime TimeofSale, String Staff_notes) throws Exception
	 {
		 try {
				connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
				statement = connect.createStatement();
				String sql = "INSERT INTO salerecord Values ('" + Sale_id +"','" + Staff_id + "','" + TimeofSale + "','" + Staff_notes + "')";
				statement.executeUpdate(sql);
				} 
	        catch (Exception e) { throw e; }    
	        finally { close();  }
	 }
	
	 public void AddSaleItem(String Sale_id, int Barcode,  int Amount, double Price) throws Exception
	 {
		 try {
				connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
				statement = connect.createStatement();
				String sql = "INSERT INTO saleitem Values ('" + Sale_id +"','" + Barcode + "','" + Amount + "','" + Price + "')";
				statement.executeUpdate(sql);
				} 
	        catch (Exception e) { throw e; }    
	        finally { close();  }
	 }
	
	
	public void RetrieveSaleRec()
	{
		
	}
	
	public void RetrieveSaleItem()
	{
		
	}
	
	/* public ArrayList<Sale> RetrieveSales(ArrayList<Sale> theSales) throws Exception 
	 {
		 try {
             Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
            statement = connect.createStatement();
            // Result set receives the results of the SQL query
            SaleRecordSet = statement.executeQuery("select * from "+Login.Database+".salerecord");
            
            String[] RecordId = null;
            String[] StaffId = null;
            LocalDateTime[] DateTimeofSale = null;
            int RecordCount = 0;
          /*  while(SaleRecordSet.next())
            {
            	RecordId[RecordCount] = SaleRecordSet.getString("Sale_Code");
            	StaffId[RecordCount] = SaleRecordSet.getString("Staff_Code");
            	DateTimeofSale[RecordCount] = SaleRecordSet.getObject("Sale_Time", LocalDateTime.class);
            	
            	Sale newSale = new Sale(StaffId[RecordCount], "noName" , StaffId[RecordCount], 10, DateTimeofSale[RecordCount]);
            	theSales.add(newSale);
            	RecordCount++;
            }
           int RecordLength = RecordCount;
          //NotRequired- For Console Testing only//  
          //  SaleItemSet = statement.executeQuery("select * from "+Login.Database+".saleitem");
          for(int i =0; i< RecordCount; i++)
          { // For loop, running through recordId, compare to resultset.next(), if it matches write new sale with info else do nothing and loop over for next one
            	while (SaleItemSet.getString("Sale_Code") == SaleRecordSet.getString("Sale_Code")) 
            	{
            		
            		int Barcode = SaleItemSet.getInt("item_barcode");  
            		int Amount = SaleItemSet.getInt("item_quantity"); 
            		double Price = SaleItemSet.getDouble("item_price");
            	
            		String miname = "not currently implemented"; // Will 
					String Sale_id = SaleItemSet.getString("Sale_Code");
					Sale newSale = new Sale(Sale_id, miname , Staff_id, Amount, DateTimeofSale[1],Price);
            		//Sale newSale = new Sale(menuID, miname, saleid, itmsaleid, daysold);
            		theSales.add(newSale);
            	}
          }
          return theSales;
          
            
        	}
       
        catch (Exception e) { throw e; }    
        finally 
        	{ 	    	close();	     	
        	}
	 }*/
		/* try {
             Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
            statement = connect.createStatement();
            // Result set receives the results of the SQL query
            SaleRecordSet = statement.executeQuery("select * from "+Login.Database+".salerecord");
          //NotRequired- For Console Testing only//  
           menu = writeResultSet(resultSet, menu);
           // ResultSet returnable = resultSet;
            
        	}
       
        catch (Exception e) { throw e; }    
        finally 
        	{ 	    	close();	     	}*/
	
	 private void close() 
		{// Ends all threads
	 		try {
	 			if (resultSet != null) 
	 			{
	 				resultSet.close();
	 			}

	 			if (statement != null)
	 			{
	 				statement.close();
	 			}

	 			if (connect != null) 
	 			{
	 				connect.close();
	 			}
	 	
	 			}
	 			catch (Exception e)
	 			{

	 			}
	 	
		}

	public ArrayList<Sale> RetrieveSoldItems(ArrayList<Sale> solditems) throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		 connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
		 statement = connect.createStatement();
		 SaleItemSet = statement.executeQuery("select * from "+Login.Database+".saleitem");
        
        while (SaleItemSet.next()) 
    	{
     	  String Barcode = (SaleItemSet.getString("item_barcode")); 
     	  int Quantities = (SaleItemSet.getInt("item_quantity"));
     	   double PricePerItem = (SaleItemSet.getDouble("item_price"));
     	   String Sale_id = (SaleItemSet.getString("Sale_Code"));
     	   Sale newSale = new Sale(Sale_id, Barcode , Quantities, PricePerItem);
     	   solditems.add(newSale);
    	}
		 return solditems;
		
	}
	 public ArrayList<Sale> RetrieveSalesRecord(ArrayList<Sale> ListofSales) throws Exception 
	 {
		 try {
            Class.forName("com.mysql.jdbc.Driver");
           connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
           statement = connect.createStatement();
           // Result set receives the results of the SQL query
           SaleRecordSet = statement.executeQuery("select * from "+Login.Database+".salerecord");
           
           //ArrayList<String> RecordId = new ArrayList<String>();
           //ArrayList<String> StaffId = new ArrayList<String>();
           //ArrayList<LocalDateTime> DateTimeofSale = new ArrayList<LocalDateTime>();
           String RecordId;
           String StaffId;
           LocalDateTime DateTimeofSale;
           int RecordCount = 0;
           
           while(SaleRecordSet.next())
           {
           	RecordId=(SaleRecordSet.getString("Sale_Code"));
           	//RecordId.add(RecordCount, SaleRecordSet.getString("Sale_Code"));
           	StaffId=(SaleRecordSet.getString("Staff_Code"));
           	DateTimeofSale=(SaleRecordSet.getObject("Sale_Time", LocalDateTime.class));
           	Sale newrecord = new Sale(RecordId, StaffId, DateTimeofSale);
           	//Sale newSale = new Sale(StaffId[RecordCount], "noName" , StaffId[RecordCount], 10, DateTimeofSale[RecordCount]);
           	ListofSales.add(newrecord);
           	RecordCount++;
           }
          return ListofSales;
          
        /*  SaleItemSet = statement.executeQuery("select * from "+Login.Database+".saleitem");
          ArrayList<String> Barcodes = new ArrayList<String>();
          ArrayList<Integer> Quantities = new ArrayList<>();
          ArrayList<Double> Prices = new ArrayList<>();
          ArrayList<String> Sale_id = new ArrayList<String>();
          while (SaleItemSet.next()) 
      	{
       	   Barcodes.add(SaleItemSet.getString("item_barcode")); 
       	   Quantities.add(SaleItemSet.getInt("item_quantity"));
       	   Prices.add(SaleItemSet.getDouble("item_price"));
       	   Sale_id.add(SaleItemSet.getString("Sale_Code"));
      	}
          LocalDateTime ldt = DateTimeofSale.get(1);
         for(int i = 0; i < Sale_id.size(); i++)
         { // For loop, running through recordId, compare to resultset.next(), if it matches write new sale with info else do nothing and loop over for next one
           	
       	  
       	  /*while (SaleItemSet.next()) 
           	{
           		
           		String Barcode = SaleItemSet.getString("item_barcode");  
           		int Amount = SaleItemSet.getInt("item_quantity"); 
           		double Price = SaleItemSet.getDouble("item_price");
           	
           		//String miname = "isthisitemname?"; // Will 
					//String Sale_id = SaleItemSet.getString("Sale_Code");
					//if(RecordId.get(i).contains(Sale_id))
				//	{
					Sale newSale = new Sale(Barcode, miname , RecordId.get(i), Amount, DateTimeofSale.get(i));
					//Sale newSale = new Sale(Sale_id, miname , StaffId.get(i), Amount, DateTimeofSale.get(i));
           		//Sale newSale = new Sale(menuID, miname, saleid, itmsaleid, daysold);
           		theSales.add(newSale);
					//}
           	}
       	  Sale newSale = new Sale(Barcodes.get(i), Barcodes.get(i) , Sale_id.get(i), Quantities.get(i), ldt);
       	  theSales.add(newSale);
         }
         return theSales;
         
           
       	*/}
      
       catch (Exception e) { throw e; }    
       finally 
       	{ 	    	close();	     	
       	}
	 }
	
	
}
