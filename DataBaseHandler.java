package gui;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import dataBaseAccess.Login;
import inventory.Inventory;
import inventory.SalesDB;

public class DataBaseHandler {

	/**
	 * Reads the file that has been provided to the program. Information is stored
	 * in a 2D array that represents the floor Static ensures that only one file can
	 * be read.
	 * 
	 * @param fileName - The name of the file to read from
	 * @return - The 2D array with the floor information
	 */
	
	//Database Handler accesses files on behalf of the program, reading, formatting and writing the information as needed.
	//Original intention was to use a database, but SQL will not work on my PC
	// Creates a new instance of Inventory.inventory to enact changes, this instance closes after use to prevent memory leaks -JH
	public static ArrayList<StockItem> retrieveMenu() {
		try {
			// create file reading objects
			ArrayList<StockItem> menu = new ArrayList<>();
			FileReader reader = new FileReader("Inventory.txt");
			BufferedReader theFile = new BufferedReader(reader);
			String stringLine = "";
			theFile.readLine();
			/*while ((stringLine = theFile.readLine()) != null) {
				String[] lineSplit = stringLine.split(",");
				String miname = lineSplit[1].replaceAll("\"", "");
				float price = Float.parseFloat(lineSplit[3].replaceAll("[\\$]", ""));
				String menuID = lineSplit[0];
				int quantity = Integer.parseInt(lineSplit[2]);
				String category = lineSplit[4];
				Boolean obsolete = Boolean.parseBoolean(lineSplit[5]);
				StockItem StockItem = new StockItem(miname, price, menuID, quantity, category, obsolete);
				menu.add(StockItem);
			}
*/			
			 
		
			try {
				
				Inventory phpStock = new Inventory();		// New Instance of java Connection		
				phpStock.ReadDB(menu); // Updates menu obj with current Database Information
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
			// Close the resource, and return the array
			theFile.close();
			return menu;
		} catch (FileNotFoundException ex) {
			// The file didn't exist, show an error
			System.out.println("Error: Menu File not found.");
			System.out.println("Please check the path to the file.");
			System.exit(1);
		} catch (IOException ex) {
			// There was an IO error, show and error message
			System.out.println("Error in reading file. Try closing it and programs that may be accessing it.");
			System.out.println("If you're accessing this file over a network, try making a local copy.");
			System.exit(1);
		}

		return null;
	}
	
	
	//get 2d array of all inventory items
	public static String[][] getInventoryTable() {
		ArrayList<StockItem> allInventory = retrieveMenu();
		
		Collections.sort(allInventory, new invNameComp());
		
		String[][] allInvTable = new String[allInventory.size()][7];
		
		//"Barcode","Description","Quantity","Price","Category","Obsolete"
		for (int i=0; i<allInventory.size();i++) {
			allInvTable[i][0] = allInventory.get(i).barcode;
			allInvTable[i][1] = allInventory.get(i).name;
			allInvTable[i][2] = Integer.toString(allInventory.get(i).quantity);
			allInvTable[i][3] = Float.toString(allInventory.get(i).price);
			allInvTable[i][4] = allInventory.get(i).category;
			allInvTable[i][5] = Boolean.toString(allInventory.get(i).obsolete);
			allInvTable[i][6] = Integer.toString(allInventory.get(i).minStock);
		}
		return allInvTable;
	}
	
	
	//Sales of each item
	public static Map<String, Integer> getSalesTable() {
		ArrayList<Sale> allSales = retrieveSales();
		Map<String, Integer> counts = new HashMap<String, Integer>();

		for (int i=0; i<allSales.size();i++) {
			if (counts.containsKey(allSales.get(i).getMIDName())) {
				counts.put(allSales.get(i).getMIDName(), counts.get(allSales.get(i).getMIDName()) + 1);
			} else {
				counts.put(allSales.get(i).getMIDName(), 1);
			}
		}
		return counts;
	}
	
	//each seperate sale ID and time
	public static String[][] getSaleIDTime() {
		ArrayList<Sale> allSales = retrieveSales();
		Map<String, LocalDateTime> uniqueSales = new HashMap<String, LocalDateTime>();
		
		for (int i=0; i<allSales.size();i++) {
			if (!uniqueSales.containsKey(allSales.get(i).getSaleID()))
			{
				uniqueSales.put(allSales.get(i).getSaleID(), allSales.get(i).getDaySold());
			} 
		}
		
		String[][] exportSales = new String[uniqueSales.size()][2];
		
		Set sales = uniqueSales.entrySet();		
		Iterator salesIterator = sales.iterator();
		
		
		for (int k=0; k<uniqueSales.size();k++) {
			Map.Entry mapping = (Map.Entry) salesIterator.next();
			exportSales[k][0]= mapping.getKey().toString();
			exportSales[k][1]= mapping.getValue().toString();
		}
		return exportSales;
	}
	//"Item", "Count", "Price", "Stock", "Category"
	
	public static String[][] getPastOrder(String orderID) throws Exception
	{
		ArrayList<StockItem> allInventory = retrieveMenu();
		ArrayList<Sale> solditems = new ArrayList<Sale>();
		SalesDB Listitemsold = new SalesDB(); // Initialise interface instance
		
		int itemCount = 0;
		Listitemsold.RetrieveSoldItems(solditems);
		String[][] orderDetails = new String[solditems.size()][5];
		//Collections.sort(allInventory, new invNameComp());
		//String[][] allInvTable = new String[allInventory.size()()][7];
		
		for (int i=0;i<solditems.size();i++)
		{//TESTING// System.out.print("I made it to the For loop!");
		
			if(solditems.get(i).getSaleID().contains(orderID))
				
			{	//TESTING//System.out.println("Success! I made it into the if loop");
				//orderDetails[itemCount][0] = allInventory.get(i).getName();
				int temp = Integer.valueOf(solditems.get(i).getMenuID());
				String itemName = "No Matches";
				String itemcategory = "Z";
				//System.out.println("The value of temp is:" +temp);
				//System.out.println("Value of menuID is : "+solditems.get(i).getMenuID());
				//System.out.println("Value of All inventory is: " +allInventory.get(temp).getName());
				for(int j =0; j<allInventory.size(); j++)
				{
					int tempbarcode = Integer.valueOf(allInventory.get(j).getbc());
					if(tempbarcode == temp )
					{
						itemName = allInventory.get(j).getName();
						itemcategory = allInventory.get(j).getCategory();
					}
				}
				//System.out.println("Value of All inventory Containing temp is:" +allInventory.contains(temp));
				//System.out.println("name of the item is: " +itemName);
				//orderDetails[itemCount][0] = allInventory.get(temp).getName();
				//setup that it checks allinventory.contains barcode = solditem.barcode
				////
				String q = String.valueOf(solditems.get(i).getAmountSold());
				orderDetails[itemCount][0] = itemName;
				orderDetails[itemCount][1] = q;
				String s = String.valueOf(solditems.get(i).getItemSalePrice() );//+ " ("+solditems.get(i).getItemSalePrice()+"*"+ q+")");
				//String total = s + q;
				orderDetails[itemCount][2] = s;
				//orderDetails[itemCount][4] = allInventory.get(temp).getCategory();
				orderDetails[itemCount][4] = itemcategory;
				itemCount++;
			}
		
		}
		return orderDetails;
			
		
		
		
	}
	
	public static ArrayList<Sale> retrieveSales()
	{
		SalesDB ListAllSales = new SalesDB();
		ArrayList<Sale> theSales = new ArrayList<>();
		//ListAllSales.RetrieveSaleRec();
		//ListAllSales.RetrieveSaleItem();
		try {
		ListAllSales.RetrieveSalesRecord(theSales);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {}
		return theSales;
	}
	
	public static void exportSales(ArrayList<Sale> theSales) 
	{
		SalesDB ThisSale = new SalesDB();
		String Staff_notes = "";
		//int check = 0; // Sale record product flag.
		String WriteOnce;
		String PreviousWrite = null;
		for(int i = 0; i < theSales.size(); i++)
			{
			Sale curSale = theSales.get(i);
			WriteOnce = curSale.getSaleID();		
			String finalCheck = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(curSale.getDaySold());
			try {
				// Adds a Single Entry Per Item 
				ThisSale.AddSaleItem(curSale.getSaleID(), Integer.parseInt(curSale.getMenuID()), curSale.getAmountSold(), curSale.getItemSalePrice());
			    } 
			catch (Exception e) 
			{			
			e.printStackTrace();
			}
					
			//if(check == 0)
			if(WriteOnce != PreviousWrite)
			{
			try {
				ThisSale.AddSaleRec(curSale.getSaleID(), Login.USRN, curSale.getDaySold(), Staff_notes);
				//check = 1; // disables second record being written.
				}
				catch (Exception e)
				{						
					e.printStackTrace();
				}
			}
				PreviousWrite = WriteOnce;	
		}
		
	}
	//Implemented - JH
	public static void updateStock(String[][] table) {
		ArrayList<StockItem> oldStock = retrieveMenu();
		Inventory phpStock = new Inventory();		// New Instance of java Connection		
		
		for (int j=0;j<oldStock.size();j++)
		{
			for (int i=0;i<table.length-1;i++)
			{
				if (Integer.valueOf(table[i][5])==Integer.valueOf(oldStock.get(j).getbc()))
				{
					oldStock.get(j).reduceQuantity(Integer.parseInt(table[i][1]));
					try {
						// are you attempting to decrement the item quantity multiple times? item barcode being j? -JH
						//phpStock.EditItemMinusOne(Integer.parseInt(oldStock.get(j).getbc()));
						phpStock.EditItemMinusAmt(Integer.parseInt(oldStock.get(j).getbc()), Integer.parseInt(table[i][1]));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(oldStock.get(j).getName());
					System.out.println(oldStock.get(j).getQuantity());
					//TESTING//System.out.println(" The current i value is:" +Integer.parseInt(table[i][1]));
				}				
			}
		}
		
		try {
			// create file reading objects
			FileWriter fw = new FileWriter("Inventory.txt");
			BufferedWriter theFile = new BufferedWriter(fw);

			theFile.write("\"Barcode\",\"Description\",\"Quantity\",\"Price\",\"Category\",\"Obsolete\"\n");
			for (int j=0;j<oldStock.size();j++)
			{
				float thePriceF = oldStock.get(j).getPrice();
				String outputLine = oldStock.get(j).getbc() +",\"" + 
						oldStock.get(j).getName() +",\""+
						oldStock.get(j).getQuantity() +",\""+
						"$"+String.format("%.2f", thePriceF) +",\""+
						oldStock.get(j).getCategory() +",\""+
						Boolean.toString(oldStock.get(j).getObsolete())+"\"\n";
				theFile.write(outputLine);
			}
								
			// Close the resource, and return the array
			theFile.close();
		} catch (FileNotFoundException ex) {
			// The file didn't exist, show an error
			System.out.println("Error: Menu File not found.");
			System.out.println("Please check the path to the file.");
			System.exit(1);
		} catch (IOException ex) {
			// There was an IO error, show and error message
			System.out.println("Error in reading file. Try closing it and programs that may be accessing it.");
			System.out.println("If you're accessing this file over a network, try making a local copy.");
			System.exit(1);
		}
	}

	public static void addStock(String name, float price, int quantity, String category, Boolean obsolete, int Minimum) {
		Inventory phpStock = new Inventory();		// New Instance of java Connection		
		int val = (obsolete) ? 1 : 0;
		try {
			// adding a new line
			phpStock.Add(name, quantity, price, category, val, Minimum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void deleteStock(int code) {
		Inventory phpStock = new Inventory();		// New Instance of java Connection		
		// adding a new line
		try {
			phpStock.Delete(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void editStockItem(int code, String name, float price, int quantity, String category, Boolean obsolete, int Minimum) {
		Inventory phpStock = new Inventory();		// New Instance of java Connection		
		int val = (obsolete) ? 1 : 0;
		try {
			// adding a new line
			phpStock.EditName(code, name);
			phpStock.EditPrice(code, price);
			phpStock.EditQuantity(code, quantity);
			phpStock.EditCategory(code, category);
			phpStock.EditObsolescence(code, val);
			phpStock.EditMinimum(code, Minimum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}

class invNameComp implements Comparator<StockItem> {
    public int compare(StockItem item1, StockItem item2) {
        return item1.getName().compareTo(item2.name);
    }
}
