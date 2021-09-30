package atm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection 
{
	
	public static Connection createConnection(String database)
	{
		// First we try to connect to localhost database
		try 
		{
		// Connection to MySQL Database **
		String url = "jdbc:mysql://localhost:3306/"+database;
		Connection con = DriverManager.getConnection(url,"root","1234");
		    
		// Let user know connection is successful **
		System.out.println("Connection Successful to '"+database+"' Database");
		
		return con;
		}
		
		// If we can't find the database, print exception message
		catch(Exception e)
		{
		System.out.println(e);
		}
        return null;
	}
	
	public static ResultSet queryResult(Connection con, String query)
	{
		try {
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			return rs;
		}
		catch(Exception e){
			System.out.println("Connection Invalid!\n Exception: "+e);
			return null;
		}
		return null;
	}
	
	public static void executeOperation(Connection con, String query)
	{
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(Exception e){
			System.out.println("Connection Invalid!\n Exception: "+e);
		}
	}

}
