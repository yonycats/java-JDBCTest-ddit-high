package kr.or.ddit.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*
 	db.Properties 파일의 내용으로 DB정보를 설정하는 방법
 	
 	방법 1) Properties 객체 이용하기
 */

public class JDBCUtil2 {
	
	static Properties prop;
	
	static {

		try {
			prop = new Properties();
			
			prop.load(new FileInputStream("./res/db.properties"));
			
			// db.properties에 저장한 데이터를 불러오기
			Class.forName(prop.getProperty("driver"));
			
			System.out.println("드라이버 로딩 성공!");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버 로딩 실패!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Connection getConnection() {
		
		// 커넥션이 잘 맺어지면 커넥션 객체가 리턴되고, 안되면 널이 리턴됨
		try {
			// db.properties에 저장한 데이터를 불러오기
			return 	DriverManager.getConnection(
					prop.getProperty("url"),
					prop.getProperty("username"),
					prop.getProperty("password"));
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static void close(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) {
		
		// 자원 반납
		if (rs != null) try {rs.close();} catch (SQLException e) {}
		if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
		if (stmt != null) try {stmt.close();} catch (SQLException e) {}
		if (conn != null) try {conn.close();} catch (SQLException e) {}		
	}
	
}
