package com.smartbuy.ocb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.smartbuy.ocb.exceptions.OcbException;




public class DAOFactory {

	private static Logger logger = Logger.getLogger(DAOFactory.class);
	public DAOFactory() {
		// TODO Auto-generated constructor stub
	}

	
	public Connection getDBConnection(int param) throws OcbException{
		 Connection con = null;
		 
			if(param == 0){
					con = getConnectionWithJDBC();
			 }else{
				 	con = getConnectionWithJNDI();
			 	  }
				return con;	
		
	}
	
	public Connection getConnectionWithJNDI() throws OcbException {
		DataSource dataSource = null;
		Connection result = null;
		
		try{
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			
			// Look for data source
			dataSource = (DataSource) envContext.lookup("jdbc/retail");
			if(dataSource != null){
				result = dataSource.getConnection();
			}
		}catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}
		return result;
	}

	public Connection getConnectionWithJDBC() throws OcbException {
		String url = "jdbc:mysql://localhost:3306/retail?useSSL=false";
		String user = "root";
		String password = "root";
		String driverClassName = "com.mysql.jdbc.Driver";
		Connection result = null;
		
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
			throw new OcbException(e.getMessage(), e);
		}
		try {
			result = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error("SQL Exception"+ e);
			throw new OcbException(e.getMessage(), e);
		}
		return result;
	}
	// close database connection
		public void closeConnection(Connection conn) throws OcbException {
			if (conn != null) {
				try {
					conn.close();
					} catch (Exception e) {
						throw new OcbException(e.getMessage(), e);
						}
				}
	
		}
}


