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
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;


import java.util.ArrayList;
import atm.CardRequestScreen;
import atm.DBConnection;
import atm.SelectTransaction;
import atm.Keypad;


public class InsertPIN implements ActionListener {
	
	private JFrame frame;
	
	// Keypad panel
	private Keypad keypad;
	private JPanel keypadPanel;
	
	//private ArrayList<JButton> numberButton = new ArrayList<JButton>();
	private JPasswordField pinField;
	
	/*Upper panel contains text box
	 * Lower panel contains confirm and cancel buttons
	 */
	private JPanel upperPanel;
	private JPanel lowerPanel;
	
	private JLabel warning;
	
	private JLabel instructions;
	
	private JButton confirm;
	private JButton cancel;
	
	private String PIN="";
	
	private Connection con;
	private String account;
	
	private int tries=3;
		
	public InsertPIN(Connection con,String account)
	{
		this.con=con;
		this.account=account;
	
		frame = new JFrame();
		
		/* Keypad Panel 
	     * See Keypad.java */
		keypad = new Keypad();
		keypadPanel = keypad.getPanel();
		for(JButton b : keypad.getButtons())
			b.addActionListener(this);
		
		
		/* Text Box Panel */
		instructions = new JLabel("Enter PIN Code", SwingConstants.CENTER);
		instructions.setFont(new Font(instructions.getName(), Font.PLAIN, 30));
		
		pinField = new JPasswordField(0);
		pinField.setHorizontalAlignment(SwingConstants.CENTER);
		pinField.setEditable(false);
		pinField.setFont(new Font(instructions.getName(), Font.PLAIN, 30));
		
		warning = new JLabel("");
		warning.setForeground(new Color(255,0,0));
		
		upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(0,1));
		upperPanel.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
		upperPanel.add(instructions);
		upperPanel.add(pinField);
		upperPanel.add(warning);
		
		/*Lower Panel : Confirm and Cancel buttons */
		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(1,0));
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 100, 50));
		lowerPanel.add(cancel);
		lowerPanel.add(confirm);
			
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
	
	/* Checks if PIN is correct
	 * If it is, then we go to the bank account screen
	 * If not, then we ask the user to try again
	 * After 3 tries, we cancel the operation */
	public void checkPIN()
	{
		String selectQuery = "select * from bankaccount where accountnumber = \""+
	                         account+"\" and accountpassword = \""+PIN+"\"";
		// if PIN is correct
		if(DBConnection.queryResult(con,selectQuery)!=null)
		{
			new SelectTransaction(con,account);
			frame.dispose();
		}
		else
		{
			tries--;
			if(tries==0)
			{
				frame.dispose();
			}
			warning.setText("Incorrect PIN! Tries remaining: "+tries);
			PIN="";
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	    String buttonPressed = e.getActionCommand().toString();
	    try
	    {
	    	int nr = Integer.parseInt(buttonPressed);
	    	if(PIN.length()<4)
	    	PIN+=nr;
	    }
	    catch(NumberFormatException nfe)
	    {
	    	switch(buttonPressed)
	    	{
	    	//Clears the PIN input
	    	case("CLR"):
	    		PIN="";
	    	    break;
	    	//DEL Buttons deletes the last character
	    	case("DEL"):
	    		if(PIN!="")
	    		PIN = PIN.substring(0, PIN.length() - 1);	
	    	    break;
	    	//Confirm -> Check PIN
	    	case("Confirm"):
	    		checkPIN();
	    		break;
	    	//Cancel -> Insert New Card
	    	case("Cancel"):
	    		frame.setVisible(false);
	    	    try {
	    	    new CardRequestScreen();
	    	    }catch(Exception e1){
	    	    	break;
	    	    }
	    	    break;
	    	default:
	    	    break;
	    	}
	    }
	    pinField.setText(PIN);
	   
		 }

}
