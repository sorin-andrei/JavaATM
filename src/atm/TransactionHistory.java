package atm;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import atm.DBConnection;
import atm.SelectTransaction;

public class TransactionHistory implements ActionListener {
	
	private JFrame frame;
	private JLabel description;
	private JButton backButton;
	private JList<String> transactionList;
	
	private JPanel lowerPanel;
	
	private Connection con;
	private String account;
	
	public TransactionHistory(Connection con, String account)
	{
		this.con=con;
		this.account=account;
		
		description = new JLabel("Transaction History", SwingConstants.CENTER);
		description.setFont(new Font(description.getName(), Font.PLAIN, 50));
		
	    DefaultListModel<String> transactions = new DefaultListModel<>();  
        
	    // Select all transactions that belong to a bank account
	    String transactionQuery = "select * from transactionhistory\r\n"
	    		+ "where idbankaccount = \r\n"
	    		+ "(select idbankaccount from bankaccount where accountnumber\r\n"
	    		+ "=\""+account+"\")";
	    ResultSet rs = DBConnection.queryResult(con, transactionQuery);
	    
	    try {
	    while(rs.next())
	    {
	    	String TransactionType = rs.getString("TransactionType");
	        float Amount = rs.getFloat("Amount");
	    	
	    	String element = TransactionType+" : "+Amount;
	    	transactions.addElement(element);
	    }
	    }
	    catch(SQLException sqle)
	    {
	     sqle.printStackTrace();  	
	    }
	    
        transactionList = new JList<>(transactions);  
		
        lowerPanel = new JPanel();
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		
		lowerPanel.add(backButton);
		
		/*
		 * Frame contains the information section
		 * The transaction list
		 * Back button
		 */
		
		frame = new JFrame();
		
		GridLayout frameLayout = new GridLayout(0,1);
		frame.setLayout(frameLayout);
		
		frame.add(description, BorderLayout.CENTER);
		frame.add(transactionList, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ATM Java");
		frame.setSize(500,500);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new SelectTransaction(con,account);
		frame.dispose();
		
	}


}
