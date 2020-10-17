package gui;
import java.util.*;

/**
 * Write a description of class Order here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

//Order class, contains a list of StockItems and a unique ID so that it can be accessed by the Order Handler.
//Processes its menu in various ways when other methods need to access the order's information
class Order {
	ArrayList<StockItem> Menu = new ArrayList<>();
	String uniqueID;

	/**
	 * Constructor for objects of class Order
	 */
	public Order() {
	}

	public void addStockItem(StockItem item) {
		Menu.add(item);
	}

	public void removeStockItem(StockItem item) {
		ArrayList<StockItem> toRemove = new ArrayList<>();
		toRemove.add(item);
		Menu.removeAll(toRemove);
	}

	public void changeItemCount(StockItem item, int count) {
		removeStockItem(item);
		for (int i = 0; i < count; i++) {
			Menu.add(item);
		}
	}

	public ArrayList<StockItem> getStockItems() {
		return Menu;
	}

	public void editItemCount(int newCount, StockItem editItem) {
		ArrayList<StockItem> alMenu = Menu;
		Map<StockItem, Integer> counts = new HashMap<StockItem, Integer>();

		for (StockItem item : alMenu) {
			if (counts.containsKey(item)) {
				counts.put(item, counts.get(item) + 1);
			} else {
				counts.put(item, 1);
			}
		}
		Menu.clear();
		for (StockItem key : counts.keySet()) {
			if (!editItem.equals(key)) {
				for (int i = 0; i < counts.get(key); i++) {
					Menu.add(key);
				}
			} else {
				for (int i = 0; i < newCount; i++) {
					Menu.add(key);
				}
			}
		}
	}

	//Generates a table of menu items after counting the duplicates, then it calculates the total cost
	public String[][] getStockItemTable() {
		ArrayList<StockItem> alMenu = Menu;
		Map<StockItem, Integer> counts = new HashMap<StockItem, Integer>();

		for (StockItem item : alMenu) {
			if (counts.containsKey(item)) {
				counts.put(item, counts.get(item) + 1);
			} else {
				counts.put(item, 1);
			}
		}

		String[][] finalOutput = new String[counts.size() + 1][6];

		
		//"Item", "Stock", "Category", "Count", "Price"
		int itemCount = 0;
		float totalCost = 0;
		for (StockItem key : counts.keySet()) {
			finalOutput[itemCount][0] = key.getName();
			finalOutput[itemCount][3] = Integer.toString(key.getQuantity());
			finalOutput[itemCount][4] = key.getCategory();
			finalOutput[itemCount][1] = String.valueOf(counts.get(key));
			finalOutput[itemCount][5] = key.getbc();
			finalOutput[itemCount][2] = "$" + String.valueOf(key.getPrice() * counts.get(key)) + " (" + counts.get(key)
					+ " x $" + String.valueOf(key.getPrice() + ")");

			totalCost = totalCost + counts.get(key) * key.getPrice();
			itemCount++;
		}
		int theSize = 0;
		if (counts != null) {
			theSize = counts.size();
		}
		
		
		finalOutput[theSize][0] = " ";
		finalOutput[theSize][1] = "Total";
		finalOutput[theSize][2] = "$" + String.valueOf(totalCost);
		finalOutput[theSize][3] = "";
		finalOutput[theSize][4] = "";
		finalOutput[theSize][5] = "";
		return finalOutput;
	}
	
	
}
