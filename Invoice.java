package gui;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

//invoice dialog that displays the final order, tallies the cost, applies discounts and sends the result to the payment provider

class Invoice extends JDialog {
	float totalPrice;
	String sentToBank="";
	//ArrayList<Sale> currentSales = DataBaseHandler.retrieveSales(); 
	ArrayList<Sale> currentSales = new ArrayList<Sale>(); // Loads ONLY the current sale into the array, rather than all previous (SQL ACCESS VIOLATIONS)
    public Invoice( JFrame frame, Order theOrder, String theCurrentOrderID) {
        super( frame, "Invoice", true );

        String[][] menuTable = theOrder.getStockItemTable();
        String[] StockItemColum = { "Item", "Count", "Price"};
        JTable table = new JTable( menuTable, StockItemColum );
        Container c = getContentPane();
        c.setLayout( new FlowLayout() );
        c.setPreferredSize(new Dimension(300,300));
        c.add( table );
        totalPrice = Float.parseFloat(menuTable[menuTable.length-1][menuTable[0].length-4].replaceAll("[\\$]", ""));
        
		
		JButton submitButton = new JButton("Submit Transaction");
		c.add(submitButton);
        
		
		//button to submit the order payment
		submitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.out.println("Transaction approved: "+sentToBank);
				 for (int i=0;i<theOrder.getStockItems().size();i++)
			        {
			        	String menuID = theOrder.getStockItems().get(i).getbc();
			        	String menuname = theOrder.getStockItems().get(i).getName();
			        	//int QTY = theOrder.getStockItems().get(i).getQuantity();
			        	String saleid = theCurrentOrderID;
			        	int itemSaleID = currentSales.size()+1+i;
			        	double price = theOrder.getStockItems().get(i).getPrice();
			        	int quantity = 1;
			        	LocalDateTime theTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
			        	//System.out.println("Final Output: "+menuID + " "+menuname+ " "+saleid+ " "+itemSaleID+ " "+theTime);
						Sale newSale = new Sale(menuID,menuname,saleid,itemSaleID,theTime,price,quantity);
						currentSales.add(newSale);
			        }
				 DataBaseHandler.exportSales(currentSales);
				 DataBaseHandler.updateStock(menuTable);
				 Invoice.this.setVisible(false);
			}
		});
		
        this.pack();
        this.show();
    }
}