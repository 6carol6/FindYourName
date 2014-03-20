package org.Handler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBLinker {
	public Connection getConnect() {
		String url = "jdbc:mysql://localhost:3306/newwords?user=root&password=Pass&useUnicode=true&characterEncoding=UTF-8";
		String username = "root";  
      	String password = "password";
		Connection conn = null;
		
        try {
        	Class.forName("com.mysql.jdbc.Driver" );  
        	conn = (Connection)DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("can't get " + e);
		}  
        return conn;
	}
	public void executeSQL(Connection conn, String sql){//不带结果的语句执行
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ResultSet search(Connection conn, String sql){
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}
	
}
