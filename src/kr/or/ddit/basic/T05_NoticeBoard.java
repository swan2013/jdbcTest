package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

public class T05_NoticeBoard {
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Scanner scan = new Scanner(System.in); 
	
	/**
	 * 메뉴를 출력하는 메서드
	 */
	public void displayMenu(){
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 작 업 선 택 ===");
		System.out.println("  1. 전체 목록 출력");
		System.out.println("  2. 새글작성");
		System.out.println("  3. 수정");
		System.out.println("  4. 삭제");
		System.out.println("  5. 검색");
		System.out.println("  6. 작업 끝.");
		System.out.println("----------------------");
		System.out.print("원하는 작업 선택 >> ");
	}
	
	/**
	 * 프로그램 시작메서드
	 */
	public void start(){
		int choice;
		do{
			displayMenu(); //메뉴 출력
			choice = scan.nextInt(); // 메뉴번호 입력받기
			switch(choice){
				case 1 :  // 전체 목록 출력
					selectAllNotice();
					break;
				case 2 :  // 새글작성
					insertNotice();
					break;
				case 3 :  // 수정
					updateNotice();
					break;
				case 4 :  // 삭제
					deleteNotice();
					break;
				case 5 :  // 검색
					selectNotice();
					break;
				case 6 :  // 작업 끝
					System.out.println("작업을 마칩니다.");
					break;
				default :
					System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
			}
		}while(choice!=6);
	}

	private void selectAllNotice() {
		try {
			conn = DBUtil.getConnection();
			
			String sql = "SELECT board_no, board_title, board_title, TO_CHAR(board_date, 'YYYY-MM-DD HH24:MI:SS') board_date FROM jdbc_board";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			System.out.println("======================================================");
			System.out.println("번호\t제목\t\t작성자\t작성일");
			System.out.println("======================================================");
			
			boolean check = false;
			
			while(rs.next()) {
				if(!check)check = true;
				System.out.println(rs.getInt("board_no")+"\t"
								 + rs.getString("board_title")+"\t\t"
								 + rs.getString("board_title")+"\t"
								 + rs.getString("board_date"));
			}
			if(!check) {
				System.out.println("등록된 글이 하나도 없습니다.");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}

	private void selectNotice() {
		boolean chk = false;
		int board_no;
		do {
			System.out.println();
			System.out.println("검색할 글 번호를 입력하세요.");
			System.out.print("글 번호 >> ");
			board_no = scan.nextInt();
			
			scan.nextLine();
			
			chk = getBoard(board_no);
			
			if(!chk) {
				System.out.println(board_no + "번 글은 존재하지 않습니다..");
				System.out.print("다시 입력하시려면 1번을, 나가시려면 0번을 입력하세요.>> ");
				if(scan.nextInt() == 0)return;
			}
		}while(!chk);
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "SELECT board_title, board_writer, TO_CHAR(board_date, 'YYYY-MM-DD HH24:MI:SS') board_date, board_content FROM jdbc_board"
					   + " WHERE board_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("======================================================");
				System.out.println("제목\t: " + rs.getString("board_title"));
				System.out.println("작성자\t: " + rs.getString("board_writer"));
				System.out.println("작성날짜\t: " + rs.getString("board_date"));
				System.out.println("내용\t: " + rs.getString("board_content"));
				System.out.println("======================================================");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
	}

	private void updateNotice() {
		boolean chk = false;
		int board_no;
		do {
			System.out.println();
			System.out.println("수정할 글 번호를 입력하세요.");
			System.out.print("글 번호 >> ");
			board_no = scan.nextInt();
			
			scan.nextLine();
			
			chk = getBoard(board_no);
			
			if(!chk) {
				System.out.println(board_no + "번 글은 존재하지 않습니다..");
				System.out.print("다시 입력하시려면 1번을, 나가시려면 0번을 입력하세요.>> ");
				if(scan.nextInt() == 0)return;
			}
		}while(!chk);
		
		System.out.print("수정할 제목을 입력해주세요. >> ");
		String title = scan.next();
		
		scan.nextLine();	//버퍼 비우기
		
		System.out.print("내용 >> ");
		String content = scan.nextLine();
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "UPDATE jdbc_board"
					   + " SET board_title = ?,"
					   + " board_content = ?"
					   + " WHERE board_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, board_no);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println(board_no + "번 글 수정 작업 성공...");
			}else {
				System.out.println(board_no + "번 글 수정 작업 실패!!!");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}

	private void deleteNotice() {
		boolean chk = false;
		int board_no;
		do {
			System.out.println();
			System.out.println("삭제할 글 번호를 입력하세요.");
			System.out.print("글 번호 >> ");
			board_no = scan.nextInt();
			
			scan.nextLine();
			
			chk = getBoard(board_no);
			
			if(!chk) {
				System.out.println(board_no + "번 글은 존재하지 않습니다..");
				System.out.print("다시 입력하시려면 1번을, 나가시려면 0번을 입력하세요.>> ");
				if(scan.nextInt() == 0)return;
			}
		}while(!chk);
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "DELETE FROM jdbc_board"
					   + " WHERE board_no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_no);

			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println(board_no + "번 글 삭제 작업 성공...");
			}else {
				System.out.println(board_no + "번 글 삭제 작업 실패!!!");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}
	
	private void insertNotice() {
		
		System.out.print("제목을 입력해주세요. >> ");
		String title = scan.next();
		
		System.out.print("작성자 >> ");
		String writer = scan.next();
		
		scan.nextLine();	//버퍼 비우기
		
		System.out.print("내용 >> ");
		String content = scan.nextLine();
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "INSERT INTO jdbc_board(board_no, board_title, board_writer, board_date, board_content)"
					   + " VALUES (board_seq.NEXTVAL, ?, ?, TO_DATE(?,'YYYYMMDDHH24MISS'), ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, writer);
			pstmt.setString(3, to_dt());
			pstmt.setString(4, content);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println("추가 작업 성공...");
			}else {
				System.out.println("추가 작업 실패!!!");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}
	
	private boolean getBoard(int board_no) {
		boolean check = false;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT COUNT(*) cnt FROM jdbc_board"
						+" WHERE board_no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
			if(cnt > 0) {
				check = true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();	//자원반납
		}
		return check;
	}
	
	private void disConnect() {
		//사용했던 자원 반납
		if(rs != null)try {rs.close();}catch(SQLException e) {};
		if(stmt != null)try {stmt.close();}catch(SQLException e) {};
		if(pstmt != null)try {pstmt.close();}catch(SQLException e) {};
		if(conn != null)try {conn.close();}catch(SQLException e) {};
		
	}

	public static void main(String[] args) {
		T05_NoticeBoard noticeBoard = new T05_NoticeBoard();
		noticeBoard.start();
	}
	
	private static String to_dt(){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(today);
	}
}
