package atm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.sql.Connection;

import atm.DBConnection;
import atm.InsertPIN;

public class CardRequestScreen implements ActionListener {

	private JFrame frame;
	private JPanel welcomePanel;
	private JPanel inputPanel;
	private JLabel lblWelcome;
	private JLabel lblInstruction;
	private JTextField tfAccount;
	private Connection con;
	

	
	public CardRequestScreen() throws Exception
	{
		con = DBConnection.createConnection("atm");
		
		frame = new JFrame();
		
		//Initialize Welcome Label
		lblWelcome = new JLabel("Welcome to BDR banking!");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font(lblWelcome.getName(), Font.PLAIN, 30));
		
		//Initialize Instruction Label
		lblInstruction = new JLabel("Please insert your card.");
		lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Initialize Textfield for CardNumber/IBAN/BankAccount
		tfAccount = new JTextField(0);
		tfAccount.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton button = new JButton("Insert");
		button.addActionListener(this);
		
		//Setup the welcome panel
		welcomePanel = new JPanel();
		welcomePanel.setLayout(new GridLayout(0, 1));
		welcomePanel.add(lblWelcome);
		welcomePanel.add(lblInstruction);
		
		//Setup the input panel
		inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createEmptyBorder(100, 15, 100, 15));
		inputPanel.setLayout(new GridLayout(1, 0));
		inputPanel.add(tfAccount);
		inputPanel.add(button);
		
		//Setup the frame: Puts the panels together
		GridLayout frameLayout = new GridLayout(0,1);
		frame.setLayout(frameLayout);
		frame.add(welcomePanel, BorderLayout.CENTER);
		frame.add(inputPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ATM Java");
		frame.setSize(500,500);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
    	new CardRequestScreen();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String account = tfAccount.getText();
		
		//SQL Statement to see if the bankaccount exists in the table
		String selectStatement = "select * from bankaccount where accountnumber = \"" + account +"\"";
		
		//If it does, we send the user to the PIN GUI
		if(DBConnection.queryResult(con,selectStatement)!=null)
		{
			lblInstruction.setText("Success!");
	        lblInstruction.setForeground(new Color(0,100,0));
			System.out.println("Good");
			//Send user to PIN
			frame.setVisible(false);
			new InsertPIN(con,account);
			
		}
		//If not, then we send the error
		else {
	        lblInstruction.setText("Card not found in database! Please insert another one!");
	        lblInstruction.setForeground(new Color(255,0,0));
            System.out.println("Bad");
		}
	}
	
    
	
	

}
