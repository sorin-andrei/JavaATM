package atm;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import atm.DBConnection;

/*
 * Because the interface will look very similar to Withdraw 
 * We will inherit it
 */
public class ChangePIN extends Withdraw {

	
	public ChangePIN(Connection con, String account)
	{
		super(con,account,"0");
		instructions.setText("Enter your new PIN");
		rule.setText("");
		
	}
	
	/*
	 * Check if the PIN has four numbers
	 * And if the PIN is different than our current one
	 */
	private boolean tryChangePIN(String input)
	{
		System.out.println("validation for "+input);
		if(input.length()<4)
		{
			rule.setForeground(new Color(255,0,0));
	    	rule.setText("The PIN needs to contain four numbers!");
	    	return false;
		}
		String pinQuery = "select * from bankaccount where accountnumber = \""+account+"\" and accountpassword = \""+input+"\"";
	    ResultSet rs = DBConnection.queryResult(con,pinQuery);
	    String res;
	    
	    try
	    {
	        res = rs.getString("AccountNumber");
	    }
	    catch(Exception sqle)
	    {
	    	res=null;
	    }
	    
	    if(res!=null)
	    {
	    	rule.setForeground(new Color(255,0,0));
		    rule.setText("The PIN needs to be a different from your current one!");
		    return false;
	    }
	    
	    String updateString = "update bankaccount set accountpassword = \""+input+"\" where accountnumber = \""+account+"\"";
	    DBConnection.executeOperation(con, updateString);
		
		return true;
	}
	
	protected void enterImplementation()
	{
		if(tryChangePIN(input))
	    {
	      new SelectTransaction(con, account);
          frame.dispose();
	    }
		else
		input="";
	}
	
	

}
