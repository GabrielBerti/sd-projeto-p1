package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ConnectionSQLite {
    
    private static Connection connection;

    public static Connection getConnetion() {
        try {
            Class.forName("org.sqlite.JDBC");
             connection = DriverManager.getConnection("jdbc:sqlite:C:\\temp\\sqliteStudio\\dbescola");            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
	
	private Connection conexao;
	private Statement statement;
	
	public ConnectionSQLite() throws Exception {
		Class.forName("org.sqlite.JDBC");
                conexao = DriverManager.getConnection("jdbc:sqlite:C:\\temp\\sqliteStudio\\dbescola");
                //C:\temp\sqliteStudio
                
                //C:\Temp\Sqlite\SQLiteStudio
		statement = conexao.createStatement();
		conexao.setAutoCommit(true);
		System.out.println("Conectou!");
	}
        
   
	
	public void desconectar() {		
		try {
			conexao.close();
			System.out.println("Conexao fechada!");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	//INSERT / UPDATE / DELETE
	public void exec (String sql) throws SQLException {
		statement.execute(sql);
                desconectar();
	}
	
	//SELECT
	public ResultSet execComRetorno(String sql) throws SQLException {  

		return statement.executeQuery(sql);
	}
        
}

