
import java.sql.*;


public class DBHandler
{
  public static void main( String args[] )
  {
	  new DBHandler();
  }
  
  DBHandler()
  {
	  createUsersTable();
	  String newSecret = createNewUser("10053908", "jeremy", "mah");
	  System.out.println("created user");

	  System.out.println("QUERY OF USER which should suceed. ACCESS:\t" + checkUser("10053908", newSecret));
	  System.out.println("QUERY OF USER which should fail. ACCESS:\t" + checkUser("1005390", newSecret));
	  System.out.println("QUERY OF USER which should fail. ACCESS:\t" + checkUser("10053908", "blurb"));
  }

/**
   * Checks the student and secret against database of authorized users.
   * The secret and studentId are currently passed as plain text
   * @param studentId
   * @param secret
   * @return
   */
  public boolean checkUser(String studentId, String secret)
  {
	  Connection c = null;
	  try{
		  Class.forName("org.sqlite.JDBC");
		  c = DriverManager.getConnection("jdbc:sqlite:doorDB.db");
		  
		  String sql = String.format("SELECT * FROM USERS WHERE STUDENT_ID = '%s';", studentId);
		  
		  Statement stmt = c.createStatement();
		  ResultSet result = stmt.executeQuery(sql);
		  if (result.next())
		  {
			  System.out.println("has results");
			  result.getString("SECRET");
			  System.out.println(result.getString("SECRET"));
			  if (secret.compareTo(result.getString("SECRET")) == 0)
			  {
				  return true;
			  }
		  }
		  return false;
	  } catch (Exception e)
	  {
		  System.err.println(e.getClass().getName() + ": " + e.getMessage());
	  }
	  return false;
  }
  
  public String createNewUser(String studentId, String fname, String lname)
  {
	  Connection c = null;
	  String secret = generateSecret();
	  try {
		  Class.forName("org.sqlite.JDBC");
		  c = DriverManager.getConnection("jdbc:sqlite:doorDB.db");
		  String sql = String.format("INSERT INTO USERS " +
				  "(STUDENT_ID,FNAME,LNAME,SECRET) " +
				  "VALUES ('%s','%s','%s','%s')", studentId, fname, lname, secret);
		  
		  Statement stmt = c.createStatement();
		  stmt.executeUpdate(sql);
		  return secret;
	  } catch (Exception e)
	  {
		  System.err.println(e.getClass().getName() + ": " + e.getMessage());
	  }
	  return null;
  }
  
  private String generateSecret()
  {
	  return "imASecret";
  }

  private void createUsersTable()
  {
	  Connection c = null;
	  try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:doorDB.db");
	    System.out.println("Opened database successfully");
	
	    Statement stmt = c.createStatement();
	    String sql = "CREATE TABLE IF NOT EXISTS USERS " +
	                 " (STUDENT_ID      STRING PRIMARY KEY  NOT NULL, " +
	                 " FNAME           TEXT    NOT NULL, " + 
	                 " LNAME            TEXT    NOT NULL, " +
	                 " SECRET          TEXT      NOT NULL)"; 
	    stmt.executeUpdate(sql);
	    stmt.close();
	    c.close();
	  } catch ( Exception e ) {
	    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    System.exit(0);
	  }
	  System.out.println("Table created successfully");
  }
  
}