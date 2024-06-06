package homework;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import kr.or.ddit.util.JDBCUtilHw08;

/*
위의 테이블을 작성하고 게시판을 관리하는
다음 기능들을 구현하시오.

기능 구현하기 ==> 전체 목록 출력, 새글작성, 수정, 삭제, 검색 
 
------------------------------------------------------------

게시판 테이블 구조 및 시퀀스

create table jdbc_board(
    board_no number not null,  -- 번호(자동증가)
    board_title varchar2(100) not null, -- 제목
    board_writer varchar2(50) not null, -- 작성자
    board_date date not null,   -- 작성날짜
    board_content clob,     -- 내용
    constraint pk_jdbc_board primary key (board_no)
);
create sequence board_seq
    start with 1   -- 시작번호
    increment by 1; -- 증가값
		
----------------------------------------------------------

// 시퀀스의 다음 값 구하기
//  시퀀스이름.nextVal

 */


public class Homework08_0605 {
	
	JDBCUtilHw08 jdbc = JDBCUtilHw08.getInstance();
	
	private Scanner sc = new Scanner(System.in);
	final int pageSize = 5;
	
	public static void main(String[] args) {
		Homework08_0605 obj = new Homework08_0605();
		obj.start();
	}

	
	public void start() {		
		int num;

		do {
			displayMenu();
			num = sc.nextInt();
			sc.nextLine();
			switch (num) {
				case 1:
					printAll();
					break;
				case 2:
					insertBoard();
					break;
				case 3:
					updateBoard();
					break;
				case 4:
					deleteBoard();
					break;
				case 5:
					searchBoard();
					break;
				case 6:
					close();
					break;
				default:
					System.out.println("메뉴를 잘못 입력했습니다. 다시입력하세요");
			}
		} while (num != 6);
	}


	private void close() {
		System.out.println("접속을 종료합니다.");
	}


	private void searchBoard() {
		// TODO Auto-generated method stub
		
	}


	private void deleteBoard() {

		String sql = " DELETE FROM JDBC_BOARD\r\n" + 
					 " WHERE BOARD_NO = ?";

		System.out.print("삭제할 게시글 번호를 선택하세요 : ");
		int num = sc.nextInt();
		sc.nextLine();
		
		System.out.println("정말로 삭제하시겠습니까?");
		System.out.println("1. 삭제\t2. 취소");
		int sel = sc.nextInt();
		sc.nextLine();
		
		if (sel==1) {
			List<Object> param = new ArrayList<Object>();
			
			param.add(num);
			
			jdbc.update(sql, param);
		} else if (sel==2) { 
			System.out.println("삭제가 취소되었습니다.");
		}
		
	}


	private void updateBoard() {

		String sql = " UPDATE JDBC_BOARD\r\n" + 
					 " SET BOARD_TITLE = ?, BOARD_WRITER = ?, \r\n" + 
					 "     BOARD_CONTENT = ?, BOARD_DATE = SYSDATE\r\n" + 
					 " WHERE BOARD_NO = ?";

		System.out.print("수정할 게시글 번호를 선택하세요 : ");
		int num = sc.nextInt();
		sc.nextLine();
		
		System.out.print(" 제목 : ");
		String title = sc.nextLine();
		
		System.out.print(" 내용 : ");
		String content = sc.nextLine();
		
		System.out.print(" 작성자(닉네임) : ");
		String writer = sc.nextLine();
		
		List<Object> param = new ArrayList<Object>();
		
		param.add(title);
		param.add(writer);
		param.add(content);
		param.add(num);
		
		jdbc.update(sql, param);
	}


	private void insertBoard() {

		String sql = " INSERT INTO JDBC_BOARD (BOARD_NO, BOARD_TITLE, BOARD_WRITER, BOARD_DATE, BOARD_CONTENT)\r\n" + 
				" VALUES ( (SELECT NVL(MAX(BOARD_NO),0)+1 FROM JDBC_BOARD) , ?, ?, SYSDATE, ?)";
		
		System.out.println("게시판에 추가할 내용을 작성하세요.");
		System.out.println();
		
		System.out.print(" 제목 : ");
		String title = sc.nextLine();
		
		System.out.print(" 내용 : ");
		String content = sc.nextLine();
		
		System.out.print(" 작성자(닉네임) : ");
		String writer = sc.nextLine();
		
		List<Object> param = new ArrayList<Object>();
		param.add(title);
		param.add(writer);
		param.add(content);
		
		jdbc.update(sql, param);
	}


	private void printAll() {

		int page = 1;

		while (true) {
			String sql = "SELECT *\r\n" + 
						 "FROM (SELECT ROWNUM RN, B.*\r\n" + 
						 "      FROM (SELECT BOARD_NO, TO_CHAR(BOARD_DATE) BOARD_DATE, BOARD_WRITER, BOARD_TITLE, BOARD_CONTENT\r\n" + 
						 "            FROM JDBC_BOARD ORDER BY BOARD_NO) B)\r\n" + 
						 "WHERE RN BETWEEN ? AND ?";

			int prePage = (page-1) * pageSize +1;
			int nextPage = page * pageSize;

			List<Object> param = new ArrayList<Object>();
			param.add(prePage);
			param.add(nextPage);

			List<Map<String, Object>> printList =  jdbc.SelectList(sql, param);

			if (printList.isEmpty()) {
				System.out.println("마지막 페이지입니다.");
				page--;
				printAll();
			}
			
			for (Map<String, Object> map : printList) {
				BigDecimal no = (BigDecimal) map.get("BOARD_NO");
				String date = (String) map.get("BOARD_DATE");
				String writter = (String) map.get("BOARD_WRITER");
				String title = (String) map.get("BOARD_TITLE");
				String content = (String) map.get("BOARD_CONTENT");
				
				System.out.println("No." + no + " [" + date + "] [작성자] " + writter + "\t[제목] " + title + "\t[내용] " + content);
			}
			System.out.println();
			System.out.println(page + " 페이지");
			System.out.println();

			System.out.println("< 이전 페이지 : 다음페이지 >");
			System.out.println("1. 종료");
			String sel = sc.nextLine();

			switch (sel) {
				case "<":
						page--;
					break;
				case ">":
						page++;
					break;
				case "1":
					return;
				default:
					System.out.println("잘못 입력했습니다. 다시 입력하세요.");
					break;
			}
		}

	}


	public void displayMenu() {
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 메 뉴 선 택 ===");
		System.out.println("  1. 전체 목록 출력");
		System.out.println("  2. 게시글 작성");
		System.out.println("  3. 게시글 수정");
		System.out.println("  4. 게시글 삭제");
		System.out.println("  5. 게시글 검색 ");
		System.out.println("  6. 종료");
		System.out.println("----------------------");
		System.out.print("원하는 메뉴를 선택하세요 >> ");
		System.out.println();
	}
	
}
