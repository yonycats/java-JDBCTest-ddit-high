package kr.or.ddit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtilHw08 {
	
	private static JDBCUtilHw08 instance = null;

	private JDBCUtilHw08() {} 
	public static JDBCUtilHw08 getInstance() {
		if(instance == null) 
			instance = new JDBCUtilHw08();
		return instance;
	}
	

	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "pc11_5";
	private String password = "java";
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	public void close(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) {
		
		// 자원 반납
		if (rs != null) try {rs.close();} catch (SQLException e) {}
		if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
		if (stmt != null) try {stmt.close();} catch (SQLException e) {}
		if (conn != null) try {conn.close();} catch (SQLException e) {}		
	}
	
	
	public void searchBoard() {
		// TODO Auto-generated method stub
		
	}


	public void deleteBoard() {
		// TODO Auto-generated method stub
		
	}


	public void updateBoard() {
		// TODO Auto-generated method stub
		
	}


	public void insertBoard(String sql, String boardTitle, String boardWriter, String boardContent) {
		
		List<Map<String, Object>> result = null;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardWriter);
			pstmt.setString(3, boardContent);
			
			int cnt = pstmt.executeUpdate();
			
			if (cnt > 0) {
				System.out.println("등록 성공!");
			} else {
				System.out.println("등록 실패!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, pstmt, rs);
		}
		
	}


	public void printList() {
		// TODO Auto-generated method stub
		
	}
	
	
}
