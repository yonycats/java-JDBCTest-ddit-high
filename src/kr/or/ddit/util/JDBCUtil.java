package kr.or.ddit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JDBCUtil {
	
	static {
		// 옵션
		// 패키지명을 포함한 클래스명 불러오기
		try {
			// Properties 객체로 하드코딩 옮기기
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.println("드라이버 로딩 성공!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
			System.out.println("드라이버 로딩 실패!");
		}
	}
	
	
	public static Connection getConnection() {
		
		// 커넥션이 잘 맺어지면 커넥션 객체가 리턴되고, 안되면 널이 리턴됨
		try {
			// Properties 객체로 하드코딩 옮기기
			return 	DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"pc11_5", 
					"java");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
