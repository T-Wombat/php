package gui;
import java.util.*;
/**
 * Write a description of class OrderHandler here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

//Order handler holds all of the orders, their status, a menu of all items available and a list of all tables in the restaurant
//accesses orders using their unique ID as a key
public class OrderHandler
{
    static Map <String, Order> Orders = new HashMap <>();
    static ArrayList <StockItem> Menu = new ArrayList <>();
	static Map<String,Boolean> OrderStatus = new HashMap <>();
   
    public void addOrder()
    {
        String orderId = UUID.randomUUID().toString();
        Order newOrder = new Order();
        Orders.put(orderId,newOrder);
    }  
    
    public boolean getStatus(String oID)
    {
        return OrderStatus.get(oID);
    }
    
    public void setStatusTrue(String oID)
    {
    	OrderStatus.put(oID, true);
    }
    
    
    public void addStatus(String oID)
    {
        if (!OrderStatus.containsKey(oID))
        {
        	OrderStatus.put(oID, false);
        }
    }
    
    public Map <String, Order> getOrderList()
    {
        return Orders;
    }
    
    public ArrayList <StockItem> getMenu()
    {
        return Menu;
    }
    
    
    public void addItemToOrder(String oID, StockItem item)
    {
        Order theOrder = Orders.get(oID);
        theOrder.addStockItem(item);
    }
    
    public  ArrayList <StockItem> getOrderMenu(String oID)
    {
        Order theOrder = Orders.get(oID);
        ArrayList <StockItem> toReturn = theOrder.getStockItems();
        return toReturn;      
    }
    
    public  Order getOrder(String oID)
    {
        Order theOrder = Orders.get(oID);
        return theOrder;      
    }
    
    public  String getFirstOrderID()
    {
        String theOrderID = (String) Orders.keySet().toArray()[0];
        return theOrderID;      
    }
    
    public  void removeOrder(String oID)
    {
        Orders.remove(oID);
    }
    
    public static void removeItemFromOrder(String oID, StockItem item)
    {
        Order theOrder = Orders.get(oID);
        theOrder.removeStockItem(item);
    }
    
    public static void changeItemCount(String oID, StockItem item, int count)
    {
        Order theOrder = Orders.get(oID);
        theOrder.changeItemCount(item, count);
    }
    
    public OrderHandler() {
        Menu = DataBaseHandler.retrieveMenu();                                    
    }
}
