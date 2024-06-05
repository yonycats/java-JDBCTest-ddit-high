package homework;

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
		// TODO Auto-generated method stub
		
	}


	private void updateBoard() {

	}


	private void insertBoard() {

		String sql = " INSERT INTO JDBC_BOARD (BOARD_NO, BOARD_TITLE, BOARD_WRITER, BOARD_DATE, BOARD_CONTENT)\r\n" + 
				" VALUES ( (SELECT NVL(MAX(BOARD_NO),0)+1 FROM JDBC_BOARD) , ?, ?, SYSDATE, ?)";
		
		System.out.println("게시판에 추가할 내용을 작성하세요.");
		System.out.println();
		
		System.out.print(" 제목 : ");
		String boardTitle = sc.nextLine();
		
		System.out.print(" 작성자(닉네임) : ");
		String boardWriter = sc.nextLine();
		
		System.out.print(" 내용 : ");
		String boardContent = sc.nextLine();
		
		jdbc.insertBoard(sql, boardTitle, boardWriter, boardContent);
	}


	private void printAll() {
		String sql = "";
		

		System.out.println("< 이전 페이지 : 다음페이지 >");
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
		System.out.print("원하는 작업 선택 >> ");
	}
	
}
