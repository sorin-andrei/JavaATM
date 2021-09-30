package atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import atm.Keypad;
import atm.SelectTransaction;

public class Withdraw implements ActionListener {
	
	protected JFrame frame;
	
	private Keypad keypad;
	// input = amount we'd like to withdraw
	protected JTextField inputField;
	protected String input="";
	
	protected JLabel instructions;
	protected JLabel rule;
	
	protected JPanel upperPanel;
	protected JPanel keypadPanel;
	protected JPanel lowerPanel;
	
	protected JButton enter;
	protected JButton cancel;
	
	protected String account;
	private String balance;
	protected Connection con;
	
	private float sumFloat;
    private float balanceFloat;
	
	
	public Withdraw(Connection con, String account, String balance)
	{
		this.con = con;
		this.account = account;
		this.balance = balance;
		
		/* Keypad Panel 
	     * See Keypad.java */
		keypad = new Keypad();
		keypadPanel = keypad.getPanel();
		for(JButton b : keypad.getButtons())
			b.addActionListener(this);
		
		/*
		 * Upper panel contains the instructions
		 */
		upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(0,1));
		upperPanel.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		
		inputField = new JTextField();
		inputField.setHorizontalAlignment(SwingConstants.CENTER);
		inputField.setEditable(false);
		inputField.setFont(new Font(inputField.getName(), Font.PLAIN, 30));
		
		instructions = new JLabel("Please enter the ammount you would like.",SwingConstants.CENTER);
		instructions.setFont(new Font(instructions.getName(), Font.PLAIN, 20));
		
		rule = new JLabel("The machine can provide only multiples of 10!",SwingConstants.CENTER);
		
		upperPanel.add(instructions);
		upperPanel.add(rule);
		upperPanel.add(inputField);
		
		/*
		 * Lower panel contains the confirm or cancel buttons
		 */
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(1,0));
		cancel = new JButton("Cancel");
		enter = new JButton("Enter");
		
		cancel.addActionListener(this);
		enter.addActionListener(this);
		
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		
		lowerPanel.add(cancel);
		lowerPanel.add(enter);
		
		
		/* 
		 * Frame 
		 * We put all panels togheter
		 */
		frame = new JFrame();
		GridLayout frameLayout = new GridLayout(0,1);
		frame.setLayout(frameLayout);
		
		frame.add(upperPanel, BorderLayout.CENTER);
		frame.add(keypadPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ATM Java");
		frame.setSize(500,500);
		frame.setVisible(true);
		
	}
	
	private boolean tryWithdraw(String sum)
	{
	     try {
	    	 sumFloat = Float.parseFloat(sum);
	    	 balanceFloat = Float.parseFloat(balance);
	     }
	     catch(Exception e)
	     {
	    	 System.out.println("can't parse :(");
	    	 sumFloat = 0;
	    	 balanceFloat = 0;
	    	 return false;
	     }
	     if(sumFloat>balanceFloat)
	     {
	    	 System.out.println("Insufficient funds");
	    	 rule.setForeground(new Color(255,0,0));
	    	 rule.setText("Insufficient funds! :-(");
	    	 return false;
	     }
	     if(sumFloat%10!=0)
	     {
	    	 rule.setForeground(new Color(255,0,0));
	    	 rule.setText("This machine can only give multiples of 10!");
	    	 return false;
	     }
	     float newBalance = balanceFloat - sumFloat;
	     String updateString = "update bankaccount set balance = "+newBalance+" where accountnumber = \""+account+"\";";
	     
	     String addToHistory="insert into transactionhistory(idbankaccount,transactiontype,amount)"
	     		+ "values((select idbankaccount from bankaccount where accountnumber=\""+account+"\"), \"Withdraw\", "+sum+");";
	     		     
	     DBConnection.executeOperation(con, updateString);
	     DBConnection.executeOperation(con, addToHistory);
	     return true;
	    
	}
	
	protected void enterImplementation()
	{
		if(tryWithdraw(input))
	    {
	      new SelectTransaction(con, account);
          frame.dispose();
	    }
		else
		input="";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 String buttonPressed = e.getActionCommand().toString();
		 try
		    {
		    	int nr = Integer.parseInt(buttonPressed);
		    	if(input.length()<4)
		    	input+=nr;
		    }
		    catch(NumberFormatException nfe)
		    {
		    	switch(buttonPressed)
		    	{
		    	//Clears the PIN input
		    	case("CLR"):
		    		input="";
		    	    break;
		    	//DEL Buttons deletes the last character
		    	case("DEL"):
		    		if(input!="")
		    		input = input.substring(0, input.length() - 1);	
		    	    break;
		    	default:
		    		break;
		    	case("Cancel"):
		    		new SelectTransaction(con,account);
		    		frame.dispose();
		    		break;
		    	case("Enter"):
		    		enterImplementation();
		            break;
		    	}
		    }
		 inputField.setText(input);
	}

}
