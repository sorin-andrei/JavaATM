package atm;

import java.util.ArrayList;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/* We re-use the keypad throughout the application */

public class Keypad {
	
	public JPanel keypad;
	public ArrayList<JButton> numberButton = new ArrayList<JButton>();
	
	public Keypad()
	{
		keypad = new JPanel();
		keypad.setLayout(new GridLayout(4, 3));
		
		//Add number buttons to button List
		for(int i=0;i<9;i++)
		numberButton.add(new JButton(i+1+""));
        //Add delete, zero and clear buttons
		numberButton.add(new JButton("DEL"));
		numberButton.add(new JButton("0"));
		numberButton.add(new JButton("CLR"));
		
		//Loop through all keypad buttons
		for(JButton b : numberButton)
		{
		keypad.add(b);
		}
		keypad.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
	}
	
	public JPanel getPanel()
	{
		return keypad;
	}
	public ArrayList<JButton> getButtons()
	{
		return numberButton;
	}

}
