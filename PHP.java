package gui;

/*
 *Based on Tabbed Pane Demo by Oracle
Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.*/
import java.util.*;
import inventory.Inventory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import java.text.NumberFormat;

import java.awt.GridLayout;
public class PHP extends JPanel {
	private Map<String, Order> allOrders;
	private static JFrame frame;
	private static OrderHandler OH;
	ListSelectionModel listSelectionModel;
	ListSelectionModel invSelectModel;
	JTable output;
	
	static String[][] itemsInOrder = {{"#", "#", "#", "#", "#"}};
	static String[] theItemColum = { "Item", "Count", "Price", "Stock", "Category" };
	static DefaultTableModel data = new DefaultTableModel(itemsInOrder, theItemColum);
	JTable reviewOutput = new JTable(data);
	
	JTable saleDisplay;
	private static int lastPage = 0;
	private JPanel panel_1;

	private int revOrder = 0;
	private int reviewSize = 0;
	
	private int invOrder = 0;
	private int invSize = 0;
	
	private static String currentOrderID = "default";
	private static String inventoryID = "default";
	private static String reviewOrderID = "default";
	
	private String[][] salesList = DataBaseHandler.getSaleIDTime();
	String[][] invTable = DataBaseHandler.getInventoryTable();
	
	public PHP() {
		super(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		
		allOrders = OH.getOrderList();
		
		
		JComponent OrdersPage = makeTextPanel(null);
		OrdersPage.setLayout(new BoxLayout(OrdersPage, BoxLayout.LINE_AXIS));
		tabbedPane.addTab("Orders", null, OrdersPage, "List of Table Orders");
		
		

		ArrayList<StockItem> theMenu = OH.getMenu();
		String[] menuTable = new String[theMenu.size()];
		for (int i = 0; i < theMenu.size(); i++) {
			menuTable[i] = theMenu.get(i).getName();
		}
		
		
		//preparing the first page's panels

		OrdersPage.setPreferredSize(new Dimension(1250, 800));
		
		JPanel currentOrderViewer = new JPanel();
		JPanel leftpanel = new JPanel();
		JPanel rightpanel = new JPanel();
		panel_1.add(leftpanel);
		panel_1.add(rightpanel);
		
		
		
		GridBagConstraints constrain = new GridBagConstraints();
		constrain.fill = GridBagConstraints.HORIZONTAL;
		constrain.anchor = GridBagConstraints.PAGE_START;
		
		
		rightpanel.add(currentOrderViewer, constrain);
		
		String orderText = "";
		if (currentOrderID.equals("default"))
		{
			orderText = "No Order";
		}
		else {
			orderText = currentOrderID.substring(0, 12) +" ...";		
		}
		
		JLabel lblNewLabel = new JLabel("ORDER ID: " + orderText);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		currentOrderViewer.add(lblNewLabel);
		currentOrderViewer.setPreferredSize(new Dimension(400, 20));
		
		leftpanel.setLayout(new GridLayout(5, 1, 0, 20));
		rightpanel.setLayout(new GridBagLayout());

		SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 30, 1);
		JSpinner spinner = new JSpinner(sModel);
		
		JButton b = new JButton("New Order");
		leftpanel.add(b);
		b.setHorizontalAlignment(SwingConstants.CENTER);
		b.setPreferredSize(new Dimension(300, 40));
		JButton button = new JButton("Add Item");
		leftpanel.add(button);
		
		//button to add items to an order
		button.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int itm = JOptionPane.showOptionDialog(OrdersPage, "Add Item", "Menu", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, menuTable, menuTable[0]);
				int theCount = 0;
				int option = JOptionPane.showOptionDialog(null, spinner, "Enter Item Count",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option == JOptionPane.CANCEL_OPTION) {
					// user hit cancel
				} else if (option == JOptionPane.OK_OPTION) {
					theCount = (Integer) spinner.getValue();
				}

				StockItem chosen = null;
				for (int z = 0; z < theMenu.size(); z++) {
					if (theMenu.get(z).getName() == menuTable[itm]) {
						chosen = theMenu.get(z);
					}
				}
				if (currentOrderID != "default") {
					for (int i = 0; i < theCount; i++) {
						OH.addItemToOrder(currentOrderID, chosen);
					}

					System.out.println("adding: " + chosen.getName() + " to " + currentOrderID);
				}

				// frame.add(new CuddlyWombat(), BorderLayout.CENTER);
				PHP CW = new PHP();
				frame.setContentPane(CW);
			}
		});

		JButton button_1 = new JButton("Edit Item Count");
		leftpanel.add(button_1);


		JButton processButton = new JButton("Process Order");
		leftpanel.add(processButton);
		
		JButton deleteButton = new JButton("Cancel Order");
		leftpanel.add(deleteButton);	
	
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			OH.removeOrder(currentOrderID);
			currentOrderID = "default";
			PHP CW = new PHP();
			frame.setContentPane(CW);
			}
		});
			
		//button to create a new order
		b.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// frame.add(new CuddlyWombat(), BorderLayout.CENTER);
				if (OH.getOrderList().isEmpty())
				{
					OH.addOrder();
					currentOrderID = OH.getFirstOrderID();
					lastPage = 0;
					PHP CW = new PHP();
					frame.setContentPane(CW);
					System.out.println("Order List: " + OH.getOrderList());
				}
				else
				{
					JOptionPane.showMessageDialog(OrdersPage, "Please resolve current order");
				}
				
			}
		});
		
		
		//button to edit the items already in the order
		button_1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				String[][] preTable = OH.getOrder(currentOrderID).getStockItemTable();
				String[] table = new String[preTable.length - 1];
				for (int i = 0; i < preTable.length - 1; i++) {
					table[i] = preTable[i][0];
				}
				int editItem = JOptionPane.showOptionDialog(OrdersPage, "Edit Item", "Menu", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, table, table[0]);
				int theCount = 0;
				int option = JOptionPane.showOptionDialog(null, spinner, "Enter Item Count",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option == JOptionPane.CANCEL_OPTION) {
					// user hit cancel
				} else if (option == JOptionPane.OK_OPTION) {
					theCount = (Integer) spinner.getValue();
				}

				StockItem chosen = null;
				for (int z = 0; z < theMenu.size(); z++) {
					if (theMenu.get(z).getName() == table[editItem]) {
						chosen = theMenu.get(z);
					}
				}

				OH.getOrder(currentOrderID).editItemCount(theCount, chosen);
				lastPage = 0;
				PHP CW = new PHP();
				frame.setContentPane(CW);
			}
		});

		if (!currentOrderID.equals("default")) {
			Order toCrOrder = OH.getOrder(currentOrderID);
			{
				String[][] table = toCrOrder.getStockItemTable();
				String[] menuItemColum = { "Item", "Count", "Price", "Stock", "Category" };

				output = new JTable(table, menuItemColum);
			}
		}
		
		//processes the order 
		processButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				new Invoice(frame,OH.getOrderList().get(currentOrderID), currentOrderID);
				lastPage = 0;
				OH.removeOrder(currentOrderID);
				currentOrderID = "default";
				PHP CW = new PHP();
				frame.setContentPane(CW);
			}
		});
		
		JScrollPane outputPane = new JScrollPane(output);
		outputPane.setBounds(0, 0, 400, 800);

		constrain.gridx =0;
		constrain.gridy =1;
		constrain.weightx = 1;
		constrain.weighty = 1;
		constrain.fill = GridBagConstraints.BOTH;

		rightpanel.add(outputPane, constrain);
		

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
		
		

		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		

		//Panel 2
		
		//setting up the chart's panel
		JComponent inventoryPane = makeTextPanel("Inventory Manager");
		tabbedPane.addTab("Inventory Manager", null, inventoryPane, "Manage Stock");
		inventoryPane.setPreferredSize(new Dimension(1200, 800));
		
		JPanel invView = new JPanel();
		invView.setLayout(new GridLayout(1, 2, 20, 20));
		invView.setPreferredSize(new Dimension(1200, 800));
		invView.setBounds(7, 7, 1200, 800);
		
		JPanel invLeftpanel = new JPanel();
		
		JPanel invRightpanel = new JPanel();
		

		
		invView.add(invLeftpanel, BorderLayout.WEST);
		invView.add(invRightpanel, BorderLayout.EAST);
		
		invLeftpanel.setLayout(new GridLayout(3, 1, 20, 20));
		
		JButton addItemButton = new JButton("Add Item");
		invLeftpanel.add(addItemButton);
		
		JButton editItemButton = new JButton("Edit Item");
		invLeftpanel.add(editItemButton);
		
		JButton removeItemButton = new JButton("Remove Item");
		invLeftpanel.add(removeItemButton);
		
		
		JTable allItems;		
		String[] inventoryColumn = {"Barcode","Description","Quantity","Price","Category","Obsolete", "Min"};
		invTable = DataBaseHandler.getInventoryTable();
		invSize = invTable.length;
		DefaultTableModel theInventory = new DefaultTableModel(invTable, inventoryColumn);
		JTable inventoryOutput = new JTable(theInventory);
		inventoryOutput.getColumnModel().getColumn(1).setPreferredWidth(200);
		inventoryOutput.getColumnModel().getColumn(6).setPreferredWidth(50);
		invSelectModel = inventoryOutput.getSelectionModel();			
	    invSelectModel.addListSelectionListener(new InventoryListSelectionHandler());
		JScrollPane inventoryScroll = new JScrollPane(inventoryOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		inventoryScroll.setBounds(7, 7, 400, 800);
		invRightpanel.add(inventoryScroll);
		inventoryPane.add(invView);
		
		System.out.println("current inventory id is " + inventoryID);
		
		//"Barcode","Description","Quantity","Price","Category","Obsolete"
		addItemButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name =null;
			    float price =0;
			    int quantity = 0;
			    String category = null;
			    Boolean obsolete = false;
			    int minimum = 0;
				
			    
			    String floatPattern = "^(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))((\\.\\d\\d)|(\\.\\d))?$";
			    Pattern p = Pattern.compile(floatPattern);

			    String[] categories = {"A", "B", "C", "D", "E", "F", "G", "H", "I", 
			    				  "J","K", "L","M","N","O","P","Q","R","S","T",
			    				  "U","V", "W", "X", "Y","Z"};
		        JComboBox<String> catCombo = new JComboBox<>(categories);
		        
		        JTextField namefield = new JTextField();
		        JTextField pricefield = new JTextField();	
		        
				SpinnerNumberModel qModel = new SpinnerNumberModel(30, 0, 999, 1);
				JSpinner qspinner = new JSpinner(qModel);
				SpinnerNumberModel mModel = new SpinnerNumberModel(5, 0, 999, 1);
				JSpinner mspinner = new JSpinner(mModel);

		        JPanel panel = new JPanel(new GridLayout(0, 1));
		        panel.add(new JLabel("Item Name:"));
		        panel.add(namefield);
		        panel.add(new JLabel("Item Price: $#.##"));
		        panel.add(pricefield);
		        panel.add(new JLabel("Item Quantity:"));
		        panel.add(qspinner);
		        panel.add(new JLabel("Item Category:"));
		        panel.add(catCombo);
		        panel.add(new JLabel("Minimum Stock:"));
		        panel.add(mspinner);
		        
		        
		        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        String nameIn = namefield.getText();
		        String priceIn = pricefield.getText();
		        category = (String) catCombo.getSelectedItem();
		        minimum = (int) mspinner.getValue();
	        	Matcher m = p.matcher(priceIn);
		        if (result == JOptionPane.OK_OPTION && m.matches() == true && nameIn.length()>0) {
		        	quantity = (int) qspinner.getValue();
		        	name = nameIn;
		        	price = Float.parseFloat(priceIn);
		            System.out.println(nameIn
		                + " " + priceIn
		                + " " + category
		                + " " + quantity
		                + " " + minimum);
		            DataBaseHandler.addStock(name, price, quantity, category, obsolete, minimum);
		        } else if (m.matches() == false || nameIn.length()==0){
		        	JOptionPane.showMessageDialog(OrdersPage, "Please add a name and valid price");
		        }else
		        {
		            System.out.println("Cancelled");
		        }

		        lastPage = 1;
				PHP CW = new PHP();
				frame.setContentPane(CW);
			}
		});
		
		//button to edit the items already in the order
		editItemButton.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						//inventoryID
						String name =null;
					    float price =0;
					    int quantity = 0;
					    String category = null;
					    boolean obsolete = false;
					    int minimum = 0;
					    
					    for (int i=0; i<invTable.length;i++)
					    {
					    	if(invTable[i][0]== inventoryID)
					    	{
					    		name = invTable[i][1];
							    price = Float.parseFloat(invTable[i][3]);
							    quantity = Integer.parseInt(invTable[i][2]);
							    category = invTable[i][4];
							    obsolete = Boolean.parseBoolean(invTable[i][5]);
							    minimum = Integer.parseInt(invTable[i][6]);
					    	}
					    }
					    
					    String floatPattern = "^(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))((\\.\\d\\d)|(\\.\\d))?$";
					    Pattern p = Pattern.compile(floatPattern);

					    String[] categories = {"A", "B", "C", "D", "E", "F", "G", "H", "I", 
					    				  "J","K", "L","M","N","O","P","Q","R","S","T",
					    				  "U","V", "W", "X", "Y","Z"};
				        JComboBox<String> catCombo = new JComboBox<>(categories);
				        
				        String[] obselecense = {"True", "False"};
				        JComboBox<String> obsCombo = new JComboBox<>(obselecense);
				        
				        catCombo.setSelectedItem(category);
				        if (obsolete == false)
				        {
					        obsCombo.setSelectedItem("False");
				        }else {
				        	obsCombo.setSelectedItem("True");
				        }
				        JTextField namefield = new JTextField(name);
				        JTextField pricefield = new JTextField(Float.toString(price));	
				        
						SpinnerNumberModel qModel = new SpinnerNumberModel(quantity, 0, 999, 1);
						JSpinner qspinner = new JSpinner(qModel);
						SpinnerNumberModel mModel = new SpinnerNumberModel(minimum, 0, 999, 1);
						JSpinner mspinner = new JSpinner(mModel);

				        JPanel panel = new JPanel(new GridLayout(0, 1));
				        panel.add(new JLabel("Item Name:"));
				        panel.add(namefield);
				        panel.add(new JLabel("Item Price: $#.##"));
				        panel.add(pricefield);
				        panel.add(new JLabel("Item Quantity:"));
				        panel.add(qspinner);
				        panel.add(new JLabel("Item Category:"));
				        panel.add(catCombo);
				        panel.add(new JLabel("Obselete:"));
				        panel.add(obsCombo);
				        panel.add(new JLabel("Minimum Stock:"));
				        panel.add(mspinner);
				        
				        
				        int result = JOptionPane.showConfirmDialog(null, panel, "Test",
				            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				        String nameIn = namefield.getText();
				        String priceIn = pricefield.getText();
				        category = (String) catCombo.getSelectedItem();
				        minimum = (int) mspinner.getValue();
			        	Matcher m = p.matcher(priceIn);
				        if (result == JOptionPane.OK_OPTION && m.matches() == true && nameIn.length()>0) {
				        	quantity = (int) qspinner.getValue();
				        	name = nameIn;
				        	price = Float.parseFloat(priceIn);
				            System.out.println(nameIn
				                + " " + price
				                + " " + category
				                + " " + quantity
				                + " " + minimum);
				            DataBaseHandler.editStockItem(Integer.parseInt(inventoryID),name, price, quantity, category, obsolete, minimum);
				        } else if (m.matches() == false || name.length()==0){
				        	JOptionPane.showMessageDialog(OrdersPage, "Please add a name and valid price, "+m.matches()+" "+name.length());
				        }else
				        {
				            System.out.println("Cancelled");
				        }

				        lastPage = 1;
						PHP CW = new PHP();
						frame.setContentPane(CW);
					}
				});
		
		removeItemButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//inventoryID
				DataBaseHandler.deleteStock(Integer.parseInt(inventoryID));

		        lastPage = 1;
				PHP CW = new PHP();
				frame.setContentPane(CW);
			}
		});
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//Panel 3
		
		JComponent salesRecords = makeTextPanel("Sales Records");
					
		
		tabbedPane.addTab("Sales Record", null, salesRecords, "View individual Sales");
		salesRecords.setPreferredSize(new Dimension(1200, 800));
		
		salesList = DataBaseHandler.getSaleIDTime();
		reviewSize = salesList.length;
		JPanel saleView = new JPanel();
		
		JPanel saleLeftpanel = new JPanel();
		
		JPanel saleRightpanel = new JPanel();
		
		JPanel salePreviewpanel = new JPanel();
		
		
		saleView.setPreferredSize(new Dimension(1200, 800));
		
		saleView.add(saleLeftpanel, BorderLayout.WEST);
		saleView.add(saleRightpanel, BorderLayout.CENTER);
		saleView.add(salePreviewpanel, BorderLayout.EAST);
		
				
		String[] saleColum = { "Sale ID", "Time of Sale"};
		
		saleDisplay = new JTable(salesList, saleColum);
		saleDisplay.setPreferredScrollableViewportSize(new Dimension(400, 800));
		saleDisplay.setFillsViewportHeight(true); 
		JScrollPane tablePane = new JScrollPane(saleDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listSelectionModel = saleDisplay.getSelectionModel();
		tablePane.setBounds(7, 7, 400, 800);
		saleRightpanel.add(tablePane);		
		
		saleLeftpanel.setLayout(new GridLayout(3, 1, 20, 20));
		
		JButton saleButton = new JButton("View Sale");
		saleLeftpanel.add(saleButton);
		
		JButton editButton = new JButton("Edit Sale");
		saleLeftpanel.add(editButton);
		
		JButton reportButton = new JButton("Generate Report");
		saleLeftpanel.add(reportButton);	
		
				
		listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
		
		System.out.println("current id is " + reviewOrderID);
		
		if (!reviewOrderID.equals("default")) {
			{
				String[][] itemsInOrder2;
				try {
					itemsInOrder2 = DataBaseHandler.getPastOrder(reviewOrderID);
					data.setDataVector(itemsInOrder2, theItemColum);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}
		}
		
		
		
		reviewOutput.setPreferredScrollableViewportSize(new Dimension(400, 800));
		reviewOutput.setFillsViewportHeight(true);
		JScrollPane reviewOrderPane = new JScrollPane(reviewOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		reviewOrderPane.setBounds(7, 7, 400, 800);
		salePreviewpanel.add(reviewOrderPane);

			
		saleButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				lastPage = 2;
				PHP CW = new PHP();	
			}
		});
		
		
		salesRecords.add(saleView);

		
		add(tabbedPane);
		// The following line enables to use scrolling tabs.
		tabbedPane.setSelectedIndex(lastPage); 

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		
		

		
		
		

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		ImageIcon img = new ImageIcon("C:\\Assignments\\Assignment 3\\Cuddly Wombat\\src\\rm.png");
		frame = new JFrame("PHP");
		OH = new OrderHandler();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		PHP CW = new PHP();
		CW.setOpaque(true);
		frame.setContentPane(CW);
		// frame.getContentPane().add(new CuddlyWombat(), BorderLayout.CENTER);
		frame.setIconImage(img.getImage());
		// Display the window.
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	protected JComponent makeTextPanel(String text) {
		panel_1 = new JPanel();
		return panel_1;

	}
	
	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				
				if (!Inventory.testInventoryAccess())
					JOptionPane.showMessageDialog(frame, "WARNING: Cannot Connect to SQL Server");
		
				
				File currentDir = new File("");
				System.out.println(currentDir.getAbsolutePath());
				UIManager.put("swing.boldMetal", Boolean.FALSE);				
				createAndShowGUI();
			}
		});
	}
	
	
	class SharedListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				if (lsm.isSelectionEmpty()) {
				} else {
					// Find out which indexes are selected.
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();
					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {
							System.out.println("Chosen: " + i);
							revOrder = i;
							if (reviewSize > 0) {
								reviewOrderID = salesList[i][0];
							}
							lastPage = 2;
							PHP CW = new PHP();
						}
					}
				}
			}
		}
	}
	
	class InventoryListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				if (lsm.isSelectionEmpty()) {
				} else {
					// Find out which indexes are selected.
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();
					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {
							System.out.println("Chosen: " + i);
							invOrder = i;
							if (invSize > 0) {
								inventoryID = invTable[i][0];
							}
							lastPage = 1;
							PHP CW = new PHP();
						}
					}
				}
			}
		}
	}
}
