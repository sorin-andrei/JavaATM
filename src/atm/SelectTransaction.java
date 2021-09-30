package atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import atm.DBConnection;
import atm.CardRequestScreen;
import atm.Withdraw;
import atm.TransactionHistory;
import atm.ChangePIN;

public class SelectTransaction  implements ActionListener {
	
	private JFrame frame;
	
	private JPanel buttonPanel;
	private JPanel welcomePanel;
	
	private JButton withdraw;
	private JButton history;
	private JButton changePIN;
	private JButton quit;
	private JLabel balanceLabel;
	private JLabel welcome;
	
	private String balance;
	
	private String account;
	
	private Connection con;
	
	public SelectTransaction(Connection con, String account)
	{
		this.account=account;
		this.con=con;
		
		/* Every button in this panel sends us to a submenu*/
		buttonPanel = new JPanel();
		
		withdraw = new JButton("Withdraw");
		history = new JButton("Transaction history");
		changePIN = new JButton("Change PIN");
		quit = new JButton("Quit");
		
		withdraw.addActionListener(this);
		history.addActionListener(this);
		changePIN.addActionListener(this);
		quit.addActionListener(this);
		
		buttonPanel.add(withdraw);
		buttonPanel.add(history);
		buttonPanel.add(changePIN);
		buttonPanel.add(quit);
		
		buttonPanel.setLayout(new GridLayout(2,2));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		
		/* Welcome the user and show MONEY 
		 * Let's find out the user's balance*/
		String balanceQuery = "select balance from bankaccount where accountnumber = \""+account+"\"";
		ResultSet rs = DBConnection.queryResult(con, balanceQuery);
		try {
		balance = rs.getString("Balance");
		}
		catch(SQLException sqle)
		{
			balance="Whoops :(";
		}
		
		balanceLabel = new JLabel(balance);
	
		
		balanceLabel.setForeground(new Color(40,150,100));
		balanceLabel.setFont(new Font(balanceLabel.getName(), Font.PLAIN, 80));
		
		welcomePanel = new JPanel();
		welcome = new JLabel("Welcome user! Your current balance is:");
		welcomePanel.add(welcome);
		welcomePanel.add(balanceLabel);
		welcomePanel.setLayout(new GridLayout(0,1));
		welcomePanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		
		
		
		/* Frame that includes all panels */
		frame = new JFrame();
		GridLayout frameLayout = new GridLayout(0,1);
		frame.setLayout(frameLayout);
		
		frame.add(welcomePanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ATM Java");
		frame.setSize(500,500);
		frame.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 String buttonPressed = e.getActionCommand().toString();
		 switch(buttonPressed) 
		 {
		 case("Quit"):
			 try 
		     {	 
				new CardRequestScreen();
			 } 
		     catch (Exception e1) 
		     {
				e1.printStackTrace();
			 }
		     frame.dispose();
		     break;
		 case("Withdraw"):
			 new Withdraw(con,account,balance);
		     frame.dispose();
		     break;
		 case("Transaction history"):
			 new TransactionHistory(con,account);
		     frame.dispose();
		     break;
		 case("Change PIN"):
			 new ChangePIN(con,account);
		     frame.dispose();
		     break;
		 }
		  
		 }
	}


