package kr.or.ddit.basic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

import kr.or.ddit.util.JDBCUtil2;

// JDBC(Java Database Connectivity) => 데이터베이스 연결 자바 API

/*
	회원정보를 관리하는 프로그램을 작성하는데 
	아래의 메뉴를 모두 구현하시오. (CRUD기능 구현하기)
	(DB의 MYMEMBER테이블을 이용하여 작업한다.)
	
	* 자료 삭제는 회원ID를 입력 받아서 삭제한다.
	
	예시메뉴)
	----------------------
		== 작업 선택 ==
		1. 자료 입력			---> insert
		2. 자료 삭제			---> delete
		3. 자료 수정			---> update
		4. 전체 자료 출력			---> select
		5. 작업 끝.
	----------------------
	 
	   
// 회원관리 프로그램 테이블 생성 스크립트 
create table mymember(
    mem_id varchar2(8) not null,  -- 회원ID
    mem_name varchar2(100) not null, -- 이름
    mem_tel varchar2(50) not null, -- 전화번호
    mem_addr varchar2(128),    -- 주소
    reg_dt DATE DEFAULT sysdate, -- 등록일
    CONSTRAINT MYMEMBER_PK PRIMARY KEY (mem_id)
);

*/
public class T01MemberInfoTest {
	
	// java.sql 패키지에 있는 인터페이스 4가지
	// 1. Connection 작업 -> 잘 연결되면 객체가 반환됨
	// Connection 객체 없이 PreparedStatement 객체를 만들 수 없음
	// PreparedStatement를 가지고 쿼리를 실행함
	
	// executeQuery => select 쓸 때 사용
	// executeUpdate => DML문 쓸 때 사용, update delete insert
	
	// pstmt.executeUpdate(); => 성공한 쿼리의 행 수를 반환, 
	// (update 5줄 성공하면 5 반환, insert 0줄 성공하면 0 반환)
	
	// ResultSet 타입의 객체 => select 값을 가져오게 도와줌
	// 사용하고 나면 꼭 close()해줘야 함 (자원반납)
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Scanner scan = new Scanner(System.in); 
	
	
	public static void main(String[] args) {
		T01MemberInfoTest memObj = new T01MemberInfoTest();
		memObj.start();
	}
	
	
	/**
	 * 메뉴를 출력하는 메서드
	 */
	public void displayMenu(){
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 작 업 선 택 ===");
		System.out.println("  1. 자료 입력");
		System.out.println("  2. 자료 삭제");
		System.out.println("  3. 자료 수정");
		System.out.println("  4. 전체 자료 출력");
		System.out.println("  5. 작업 끝.");
		System.out.println("----------------------");
		System.out.print("원하는 작업 선택 >> ");
	}
	
	/**
	 * 프로그램 시작메서드
	 */
	public void start(){
		int choice;
		do {
			displayMenu(); //메뉴 출력
			choice = scan.nextInt(); // 메뉴번호 입력받기
			switch(choice){
				case 1 :  // 자료 입력
					insertMember();
					break;
				case 2 :  // 자료 삭제
					deleteMember();
					break;
				case 3 :  // 자료 수정
					updateMember();
					break;
				case 4 :  // 전체 자료 출력
					displayAllMember();
					break;
				case 5 :  // 작업 끝
					System.out.println("작업을 마칩니다.");
					break;
				default :
					System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
			}
		} while(choice!=5);
	}

	
	private void displayAllMember() {

		System.out.println();
		System.out.println("------------------------------------------");
		System.out.println(" ID\t생성일\t이 름\t전화번호\t주 소");
		System.out.println("------------------------------------------");
		
		try {
			conn = JDBCUtil2.getConnection();
			
			stmt = conn.createStatement();
			
			// stmt.executeQuery()는 실행하는 시점에 넣어주면 됨
			rs = stmt.executeQuery(" SELECT * FROM MYMEMBER ORDER BY MEM_ID");
			
			while (rs.next()) {
				String memId = rs.getNString("mem_id");
				String memName = rs.getNString("mem_name");
				String memTel = rs.getNString("mem_tel");
				String memAddr = rs.getNString("mem_addr");
			
				// LocalDate => 자바 8부터 지원
				// 시간까지 출력
//				LocalDateTime regDt = rs.getTimestamp("reg_dt").toLocalDateTime();
				// 날짜만 출력
				LocalDate regDt = rs.getTimestamp("reg_dt").toLocalDateTime().toLocalDate();
				
				System.out.println(memId + "\t" + regDt + "\t" + memName + "\t" + memTel +"\t" + memAddr);
			}
			System.out.println("------------------------------------------");
			System.out.println("출력 끝");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil2.close(conn, stmt, pstmt, rs);
		}
		
	}


	// 회원정보를 삭제하기 위한 메서드
	private void deleteMember() {

		System.out.println();
		System.out.println("삭제할 회원정보를 입력해주세요.");

		System.out.println("회원 ID >> ");
		String memId = scan.next();
			
		////////////////////////////////
		
		try {
			conn = JDBCUtil2.getConnection();
			
			String sql = " DELETE FROM MYMEMBER WHERE MEM_ID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memId);
			
			// 리턴값은 성공여부 반환함 (0, 1)
			int cnt = pstmt.executeUpdate();
			
			if (cnt > 0) {
				System.out.println(memId + "인 회원정보 삭제 성공!");
			} else {
				System.out.println(memId + "인 회원정보 삭제 실패!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil2.close(conn, stmt, pstmt, rs);
		}
		
	}


	// case 3 : 회원정보를 수정하기 위한 메서드
	private void updateMember() {

		// 아이디 중복 유무 확인용
		boolean isExist = false;
		
		String memId = "";
		
		do {
			System.out.println();
			System.out.println("수정할 회원정보를 입력해주세요.");
			
			System.out.println("회원 ID >> ");
			memId = scan.next();
			
			isExist = checkMember(memId);
			
			// 아이디가 있을 때까지 돌림
			if (!isExist) {
				System.out.println("회원 ID가 " + memId + "인 회원은 존재하지 않습니다.");
				System.out.println("다시 입력해주세요.");
			}
			
		// 아이디가 있을 때까지 돌림
		} while (!isExist);
		
		System.out.println("회원 이름 >> ");
		String memName = scan.next();
		
		System.out.println("회원 전화번호 >> ");
		String memTel = scan.next();
		
		scan.nextLine(); // 입력 버퍼에 남아있는 엔터키 제거용
		
		System.out.println("회원 주소 >> ");
		String memAddr = scan.nextLine();
		
		////////////////////////////////
		
		try {
			conn = JDBCUtil2.getConnection();
			
			String sql = " UPDATE MYMEMBER\r\n" + 
						 " SET MEM_NAME = ?, MEM_TEL = ?, MEM_ADDR = ?\r\n" + 
						 " WHERE MEM_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memName);
			pstmt.setString(2, memTel);
			pstmt.setString(3, memAddr);
			pstmt.setString(4, memId);
			
			int cnt = pstmt.executeUpdate();
			
			if (cnt > 0) {
				System.out.println(memId + "인 회원정보 수정 성공!");
			} else {
				System.out.println(memId + "인 회원정보 수정 실패!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil2.close(conn, stmt, pstmt, rs);
		}
		
	}


	// case 1 : 회원 정보 등록 메서드
	private void insertMember() {
		
		// 아이디 중복 유무 확인용
		boolean isExist = false;
		
		String memId = "";
		
		do {
			System.out.println();
			System.out.println("추가할 회원정보를 입력해주세요.");
			
			System.out.println("회원 ID >> ");
			memId = scan.next();
			
			isExist = checkMember(memId);
			
			if (isExist) {
				System.out.println("회원 ID가 " + memId + "인 회원은 이미 존재합니다.");
				System.out.println("다시 입력해주세요.");
			}
			
		} while (isExist);
		
		System.out.println("회원 이름 >> ");
		String memName = scan.next();
		
		System.out.println("회원 전화번호 >> ");
		String memTel = scan.next();
		
		scan.nextLine(); // 입력 버퍼에 남아있는 엔터키 제거용
		
		System.out.println("회원 주소 >> ");
		String memAddr = scan.nextLine();
		
		////////////////////////////////
		
		// JDBC 코딩
		try {
			
			// JDBCUtil2로 옮김
//			// 옵션
//			// 패키지명을 포함한 클래스명 불러오기
//			// ojdbc6.jar파일 추가하여 Build Path 추가
//			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
			// JDBCUtil2로 옮김
//			conn = DriverManager.getConnection(
//					"jdbc:oracle:thin:@localhost:1521:xe", 
//					"pc11_5", 
//					"java");
			
			conn = JDBCUtil2.getConnection();
			
			// 쿼리 맨 앞줄은 쿼리 오류 방지를 위해 한칸씩 띄워주는게 좋음
			String sql = " INSERT INTO MYMEMBER(MEM_ID, MEM_NAME, MEM_TEL, MEM_ADDR)" + 
						 "    VALUES (?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			// pstmt.setString(parameterIndex, x); => 인덱스가 1부터 시작함
			pstmt.setString(1, memId);
			pstmt.setString(2, memName);
			pstmt.setString(3, memTel);
			pstmt.setString(4, memAddr);
			
			// executeUpdate
			// executeQuery
			// 처리 결과에 따라서 0과 1을 반환함
			int cnt = pstmt.executeUpdate();
			
			if (cnt > 0) {
				System.out.println(memId + "인 회원정보 등록 성공!");
			} else {
				System.out.println(memId + "인 회원정보 등록 실패!");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
//			// 자원반납
//			if (rs != null) try {rs.close();} catch (SQLException e) {}
//			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
//			if (stmt != null) try {stmt.close();} catch (SQLException e) {}
//			if (conn != null) try {conn.close();} catch (SQLException e) {}			
			
			// JDBCUtil2로 옮김
			JDBCUtil2.close(conn, stmt, pstmt, rs);
		}
		
	}


	// 회원정보가 존재하는지 체크하기 위한 메서드
	private boolean checkMember(String memId) {
		
		boolean isExist = false;
		
		try {
			
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", 
					"pc11_5", 
					"java");
			
			// 쿼리 맨 앞줄은 쿼리 오류 방지를 위해 한칸씩 띄워주는게 좋음
			String sql = " SELECT COUNT(*) CNT FROM MYMEMBER WHERE MEM_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, memId);
			
			// 리턴값은 성공여부 반환함 (0, 1)
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			
			// executeQuery()에서 next로 하나씩 순회하며
			// 데이터가 있으면 true, 없으면 false 반환
			while(rs.next()) {
				// rs.getInt(0) => 0번째 컬럼 지정
				// rs.getInt("CNT") => CNT 컬럼 지정
				cnt = rs.getInt("CNT");
			}
			
			if (cnt > 0) {
				isExist = true;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (rs != null) try {rs.close();} catch (SQLException e) {}
			if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
			if (stmt != null) try {stmt.close();} catch (SQLException e) {}
			if (conn != null) try {conn.close();} catch (SQLException e) {}		
		}
		
		return isExist;
	}
	
	
}






