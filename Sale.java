package gui;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sale {
	String menuID;
	String mIDName;
	String saleID;
	String EmployeeID;
	int itemSaleID;
	double itemSalePrice;
	int quantity;
	LocalDateTime daySold;
	
	//Sale struct, making up for no queries
	public Sale(String mID, String mIDN, String sID, int isID, LocalDateTime dS, double price) {
		menuID=mID;
		mIDName =mIDN;
		saleID=sID;
		itemSaleID=isID;
		daySold=dS;
		itemSalePrice = price;
	}
	
	public Sale(String mID, String mIDN, String sID, int isID, LocalDateTime dS, double price, int QTY) {
		menuID=mID;
		mIDName =mIDN;
		saleID=sID;
		itemSaleID=isID;
		daySold=dS;
		itemSalePrice = price;
		quantity = QTY;
	}

	public Sale(String sale_id, String barcode, int QTYSold, double pricePerItem)
	{
		saleID = sale_id;
		menuID = barcode;
		itemSalePrice = pricePerItem;
		quantity = QTYSold;
	}

	public Sale(String recordId, String staffId, LocalDateTime dateTimeofSale)
	{
		saleID = recordId;
		EmployeeID = staffId;
		daySold = dateTimeofSale;
	}

	public String getMenuID() {
		return menuID;
	}

	public String getMIDName() {
		return mIDName;
	}

	public String getSaleID() {
		return saleID;
	}

	public int getItemSaleID() {
		return itemSaleID;
	}

	public LocalDateTime getDaySold() {
		return daySold;
	}

	public double getItemSalePrice() {
		// TODO Auto-generated method stub
		return itemSalePrice;
	}

	public int getAmountSold() 
	{
		
		return quantity;
	}


}
