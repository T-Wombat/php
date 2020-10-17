package inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import gui.StockItem;
import dataBaseAccess.Login;
/////////
/*
 * YET TO BE IMPLEMENTED
 * 
 * -- Search by item name, barcode, price, category
 * 
 * 
///////////////////////////////////////////////////////
/*
 *IMPLEMENTED 
 *
 *Add Functions, Delete by Barcode identifier or Name(caution)
 * Edit Functions
 */
public class Inventory
{
	public static String USRN = "phpadmin";
	public static String USRP = "phpadminpw";
	public static String Database = "dp2pharm"; 
	 protected Connection connect = null;
	 protected Statement statement = null;
	 protected PreparedStatement preparedStatement = null;
	 protected ResultSet resultSet = null;
	 //public String Login = "will"; // will be removed after GUI implemented
	 //public String Pass = "willpass"; // set externally by GUI login process global Var
	 // default values if they are not passed along. 
	 protected int amount = 0;
	 protected int bar = 0;
	 protected int obsolescence = 0; // 0 = not discontinues, 1 = discontinued
	 protected double price = 0; // adds zero price
	 protected String name = "";
	 protected String category = "";
	 
/////////////////////////////////////////////////////////////////////////////////////////
/*			Test connection to inventory
1. Returns a boolean result of whether connection to the database is a success
*/
/////////////////////////////////////////////////////////////////////////////////////////


//1
	public static boolean testInventoryAccess()
	{
		Connection testConn = null;
		boolean testConnSuccess = true;
	
		try {
			testConn = DriverManager.getConnection("jdbc:mysql://localhost/"+Database+"?", Login.USRN, Login.USRP);
			testConn.close();
		}
		catch (SQLException ex)
		{
			testConnSuccess = false;
		}
	
		return testConnSuccess;
	}
	 
	 
	 
/////////////////////////////////////////////////////////////////////////////////////////
	 /*
	 			Add Item to inventory Methods (barcodes must be unique, names do not need to be)
	 			
	 			1. Preferred method, adds item_desc, quantity and category, next available Barcode assigned by the database. 
	 			2. Adds empty barcode entry -- Care must be taken not to use an existing barcode -- will reject attempts
	 			3. Adds an item_desc, auto assigns a unique barcode -- items can have duplicate names.
	 			4. Adds an item, user specifies Barcode in addtion to name,category,quantity.
	 			5. Add an item, follows method 1, also assigns a minimum quantity before re-order
	 			
	 
*///////////////////////////////////////////////////////////////////////////////////////
	 
//1
	 public void Add(String Name,  int Amount, double Price, String Category, int Obsolescence) throws SQLException  //1
		{	/// Adds an Item to inventory Specifying all values///
		try {
			// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
			// Statements allow SQL queries to the database
			statement = connect.createStatement();
			String sql = "INSERT INTO inventory Values ('" + bar +"','" + Name + "','" + Amount + "','" + Price + "','"+ Category + "','" + Obsolescence + "')";
			statement.executeUpdate(sql);
			} 
        catch (Exception e) { throw e; }    
        finally { close();  }
		}
//2	 
	public void Add(int Barcode) throws SQLException//2
		{	/// Adds a user specified barcode entry, all other fields will be left empty///
		try {
				// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
				// Statements allow SQL queries to the database
				statement = connect.createStatement();
				String sql = "INSERT INTO inventory Values ('" + Barcode +"','" + name + "','" + amount + "','" + price + "','" + category + "','" + obsolescence + "')";
				statement.executeUpdate(sql);
			} 
        catch (Exception e) { throw e; }    
        finally { close();  }	
		}
//3	
	public void Add(String Name) throws SQLException//3
		{	/// Adds an Item name only, Assigned Barcode by database, all other values blank///
		try {
				// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
				// Statements allow SQL queries to the database
				statement = connect.createStatement();
				String sql = "INSERT INTO inventory Values ('" + bar +"','" + Name + "','" + amount + "','" + price + "','" + category + "','" + obsolescence + "')";
				statement.executeUpdate(sql);
			}
		 
		catch (Exception e) { throw e; }    
		finally { close();  }
		}
//4	
	public void Add(int Barcode, String Name, int Amount, double Price, String Category, int Obsolescence) throws SQLException//4
		{	/// Adds an Item to inventory Specifying all values///
		try {
				// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
				// Statements allow SQL queries to the database
				statement = connect.createStatement();
				String sql = "INSERT INTO inventory Values ('" + Barcode +"','" + Name + "','" + Amount + "','" + Price + "','"+ Category + "','" + Obsolescence + "')";
				statement.executeUpdate(sql);
			} 
        catch (Exception e) { throw e; }    
        finally { close();  }
		}
	
//5
	 public void Add(String Name,  int Amount, double Price, String Category, int Obsolescence, int Minimum) throws SQLException  //5
		{	/// Adds an Item to inventory Specifying all values///
		try {
			// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
			// Statements allow SQL queries to the database
			statement = connect.createStatement();
			String sql = "INSERT INTO inventory Values ('" + bar +"','" + Name + "','" + Amount + "','" + Price + "','"+ Category + "','" + Obsolescence + "','" + Minimum + "')";
			statement.executeUpdate(sql);
			} 
     catch (Exception e) { throw e; }    
     finally { close();  }
		}

	
/////////////////////////////////////////////////////////////////////////////////////////
/*
* 					Edit Items Existing in Inventory
* 
* 			1. Update by Barcode (Preferred Method)
* 				1.1 All Fields
* 				1.2 Only Amount
* 			2. Update by Name -- may have overlapping names
* 			3. SetMinimum will define a re-order bound, can be checked for current <= minimum stock when generating order report.
* 
* 
* 			Will be Implemented with LOW_PRIORITY Tag, which will update only after others stop using that particular table
* 			Sale records will be implemented with HIGH priority and will update immediately 
* 	
*////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void EditName(int Barcode, String Name) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_desc = ('" + Name + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditCode(int Barcode, String Name) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_barcode = ('" + Barcode + "')  WHERE item_desc = ('"+ Name +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditQuantity(int Barcode, int Amount) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_quantity = ('" + Amount + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditMinimum(int Barcode, int Amount) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET minimum_stock = ('" + Amount + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditPrice(int Barcode, double Price) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_price = ('" + Price + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditCategory(int Barcode, String Category) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_category = ('" + Category + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditObsolescence(int Barcode, int Obsolescence) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_obsolete = ('" + Obsolescence + "')  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditItemMinusOne(int Barcode) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_quantity = item_quantity-1  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditItemMinusAmt(int Barcode, int Reduction) throws SQLException 
	{
		try {
			// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
			// Statements allow SQL queries to the database
			statement = connect.createStatement();
			String sql = "UPDATE inventory  SET item_quantity = item_quantity-"+Reduction+"  WHERE item_barcode = ('"+ Barcode +"')";
			statement.executeUpdate(sql);
			} 
	    catch (Exception e) { throw e; }    
	    finally { close();  }
	}	
	public void EditItemPlusOne(int Barcode) throws SQLException
	{	/// Adds an Item to inventory Specifying all values///
	try {
		// Setup the connection with the DB//
		connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
		// Statements allow SQL queries to the database
		statement = connect.createStatement();
		String sql = "UPDATE inventory  SET item_quantity = item_quantity+1  WHERE item_barcode = ('"+ Barcode +"')";
		statement.executeUpdate(sql);
		} 
    catch (Exception e) { throw e; }    
    finally { close();  }
	}
	
	public void EditItemPlusAmt(int Barcode, int Addition) throws SQLException 
	{
		try {
			// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
			// Statements allow SQL queries to the database
			statement = connect.createStatement();
			String sql = "UPDATE inventory  SET item_quantity = item_quantity+"+Addition+"  WHERE item_barcode = ('"+ Barcode +"')";
			statement.executeUpdate(sql);
			} 
	    catch (Exception e) { throw e; }    
	    finally { close();  }
	}	
	
	 public void SetMinimum(int Barcode, int Minimum) throws SQLException
		{	/// Adds an Item to inventory Specifying all values///
		try {
			// Setup the connection with the DB//
			connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
			// Statements allow SQL queries to the database
			statement = connect.createStatement();
			String sql = "UPDATE inventory  SET minimum_stock = ('" + Minimum + "')  WHERE item_barcode = ('"+ Barcode +"')";
			statement.executeUpdate(sql);
			} 
    catch (Exception e) { throw e; }    
    finally { close();  }
		}
	
	
/////////////////////////////////////////////////////////////////////////////////////////
/*
* 					Delete Items Existing in Inventory
* 
* 			1. Delete by Barcode (Preferred Method)
* 			
* 			2. Delete by Name -- may have duplicate names, will consequently delete multiple entries.
* 	
*////////////////////////////////////////////////////////////////////////////////////////

//1
	public void Delete(int Barcode) throws Exception
	{	// Deletes all Items from database that have a matching Barcode Identifier
		 try 
		 {
			 connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP);
			 statement = connect.createStatement();
			 String sql = "DELETE FROM inventory WHERE item_barcode = ('"+ Barcode + "')";
	         statement.executeUpdate(sql);
		 }
		 catch (Exception e) { throw e; }    
	        finally { close();  }
	 }
//2	
	public void Delete(String Name) throws Exception
	 {	//Deletes all database entries that have a matching name -- Use with Caution, 
		 try 
		 {
			 connect = DriverManager.getConnection("jdbc:mysql://localhost/"+ Login.Database +"?", Login.USRN, Login.USRP); 
			 statement = connect.createStatement();
			 String sql = "DELETE FROM inventory WHERE item_desc = ('"+ Name + "')";
	         statement.executeUpdate(sql);
		 }
		 catch (Exception e) { throw e; }    
	        finally { close();  }
	 }
	
	
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
/*
* 					Database inspection
* 				1,3 updated to return values to GUI
* 			1. External call to access and write all fields from Database (will write to terminal, can also write to CSV file with minor modification)
* 			
* 			2. Internal returns column names
* 
* 			3. Internal Prints Results to terminal called from 1.
* 
* 			4. Compares Current stock vs Hard minimum for re-order, returns list of items to reorder, returns false if unneeded.
* 	
*////////////////////////////////////////////////////////////////////////////////////////	
	
//1	
	public ArrayList<StockItem> ReadDB(ArrayList<StockItem> menu) throws Exception 
	 	{
		 
		 
	        try {
	             Class.forName("com.mysql.jdbc.Driver");
	            connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
	            statement = connect.createStatement();
	            // Result set receives the results of the SQL query
	            resultSet = statement.executeQuery("select * from "+Login.Database+".inventory");
	          //NotRequired- For Console Testing only//  
	           menu = writeResultSet(resultSet, menu);
	           // ResultSet returnable = resultSet;
	            
	        	}
	       
	        catch (Exception e) { throw e; }    
	        finally 
	        	{ 	    	close();	     	}
	        
	        return menu;
		 }
//2	
	 private void writeMetaData(ResultSet resultSet) throws SQLException {
		 			//Outputs the Database Column names

	        System.out.println("The columns in the table are: ");

	        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
	            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
	        }
	    }	
	
	
//3	
	 private ArrayList<StockItem> writeResultSet(ResultSet resultSet, ArrayList<StockItem> menu2) throws SQLException {
	        // ResultSet is initially before the first data set
		 
	        while (resultSet.next()) {
	            // It is possible to get the columns via name
	            // also possible to get the columns via the column number
		        // String ItemName = resultSet.getString(2); // can also call for items from the column name
		        // example using column (1) to pair a barcode number.
	           
	          //Not used with current DB  String ItemDescription = resultSet.getString("ItemDescription");
	        	boolean Discontinued = false;
	        	int ItemKey = resultSet.getInt("item_barcode");
	            String ItemName = resultSet.getString("item_desc");
	            int ItemQuantity = resultSet.getInt("item_quantity");
	            double ItemPrice = resultSet.getDouble("item_price");
	            String ItemCategory = resultSet.getString("item_category");
	            int ItemObsolete = resultSet.getInt("item_obsolete");
	            int ItemMinimum = resultSet.getInt("minimum_stock");
	            if(ItemObsolete == 1)
	            {
	            Discontinued = true;
	            }
	            else {}
	            String menuID = Integer.toString(ItemKey);
	            StockItem StockItem = new StockItem(ItemName, (float) ItemPrice, menuID, ItemQuantity, ItemCategory, Discontinued, ItemMinimum);
				menu2.add(StockItem);
	            
	           // String summary = resultSet.getString("summary");
	           // Date date = resultSet.getDate("datum");
	           // String **** = resultSet.getString("****");
	           // System.out.println("Barcode number: " + ItemKey);
	           // System.out.println("Product Name: " + ItemName);
	           // System.out.println("Remaining Stock: " + ItemQuantity);
	           // System.out.println("Price: $" + ItemPrice);
	           // System.out.println("Item Type: " + ItemCategory);
	           // System.out.println("Discontinued: " + Discontinued);
	           // System.out.println("\n");
	            //System.out.println("Date: " + date);
	            
	        }
	        System.out.println("End of printout\n");
			return menu2;
	    }
	
	
	
//4	
	public ArrayList<Integer> NeedsReorder() throws Exception
	{
		ArrayList<Integer> Reorder = new ArrayList<>();
		  try 
		  {
	            Class.forName("com.mysql.jdbc.Driver");
	            connect = DriverManager.getConnection("jdbc:mysql://localhost/"+Login.Database+"?", Login.USRN, Login.USRP);
	            statement = connect.createStatement();
	            // Result set receives the results of the SQL query
	            resultSet = statement.executeQuery("select * from "+Login.Database+".inventory");
	           
	            while (resultSet.next()) 
	            {   
	            	//Not used with current DB  String ItemDescription = resultSet.getString("ItemDescription");
		        	
		        	int ItemKey = resultSet.getInt("item_barcode");
		           
		            int ItemQuantity = resultSet.getInt("item_quantity");
		            int ItemMinimum  = resultSet.getInt("minimum_stock");
		            if(ItemQuantity <= ItemMinimum)
		            {
		             Reorder.add(ItemKey);
		            }
	            }
	           
	            	
	            	
		  }	  
		 
		  catch (Exception e) { throw e; }    
	        finally 
	        	{ 	    	close();	     	}
		  
		
      		return Reorder; // Returns Array of Barcodes(Want names instead) to order, if empty to be handles by menu
      	}
		
	
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
/*
* 					Database - End Threads
* 
* 			1. Closes all current threads
* 			
* 			
* 	
*////////////////////////////////////////////////////////////////////////////////////////	
	
	
	
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
	
		
	
}




