package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

public class T05_Hotel {
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Scanner scan = new Scanner(System.in); 
	
	public void displayMenu() {
		System.out.println();
		System.out.println("*******************************************");
		System.out.println("어떤 업무를 하시겠습니까?");
		System.out.println("1.체크인  2.체크아웃 3.객실상태 4.업무종료");
		System.out.println("*******************************************");
		System.out.print("메뉴선택 => ");
		
	}
	
	public void start() {
		System.out.println("**************************");
		System.out.println("호텔 문을 열었습니다.");
		System.out.println("**************************");
		
		while(true) {
			
			displayMenu();
			
			int menuNum = scan.nextInt();   // 메뉴 번호 입력
			
			switch(menuNum){
			case 1 : checkIn();		//체크인
				break;
			case 2 : checkOut();	//체크아웃
				break;
			case 3 : displayAll();	//객실상태
				break;
			case 4 :
				System.out.println("**************************");
				System.out.println("호텔 문을 닫았습니다.");
				System.out.println("**************************");
				return;
			default :
				System.out.println("잘못 입력했습니다. 다시입력하세요.");
			}
		}
	}
	
	private void displayAll() {
		try {
			conn = DBUtil.getConnection();
			
			String sql = "SELECT * FROM hotel_mng";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			System.out.println("======================================================");
			System.out.println("방번호\t투숙객");
			System.out.println("======================================================");
			
			boolean check = false;
			
			while(rs.next()) {
				if(!check)check = true;
				System.out.println(rs.getInt("room_num")+"\t"
								 + rs.getString("guest_name"));
			}
			if(!check) {
				System.out.println("체크인된 방이 없습니다.");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}

	private void checkOut() {
		boolean chk = false;
		int roomId;
		do {
			System.out.println();
			System.out.println("어느방을 체크아웃 하시겠습니까?");
			System.out.print("방번호 입력 >> ");
			roomId = scan.nextInt();
			
			scan.nextLine();
			
			chk = getMember(roomId);
			
			if(!chk) {
				System.out.println(roomId + "방에는 체크인한 사람이 없습니다.");
				System.out.print("다시 입력하시려면 1번을, 나가시려면 0번을 입력하세요.>> ");
				if(scan.nextInt() == 0)return;
			}
		}while(!chk);
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "DELETE FROM hotel_mng"
					   + " WHERE room_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, roomId);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println("체크아웃 되었습니다.");
			}else {
				System.out.println("체크아웃을 실패하였습니다.");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
	}

	private void checkIn() {
		boolean chk = false;
		int roomId;
		do {
			System.out.println();
			System.out.println("어느방에 체크인 하시겠습니까?");
			System.out.print("방번호 입력 >> ");
			roomId = scan.nextInt();
			
			scan.nextLine();
			
			chk = getMember(roomId);
			
			if(chk) {
				System.out.println("방번호가  " + roomId + "은 이미 체크인 되었습니다.");
				System.out.print("다시 입력하시려면 1번을, 나가시려면 0번을 입력하세요.>> ");
				if(scan.nextInt() == 0)return;
			}
		}while(chk);
		
		System.out.println("누구를 체크인 하시겠습니까?");
		System.out.print("이름 입력 >> ");
		String Name = scan.nextLine();		
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "INSERT INTO hotel_mng(room_num, guest_name)"
					   + " VALUES (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, roomId);
			pstmt.setString(2, Name);
			
			int cnt = pstmt.executeUpdate();
			
			if(cnt > 0) {
				System.out.println("체크인 되었습니다.");
			}else {
				System.out.println("체크인에 실패하였습니다.");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		
	}

	private boolean getMember(int room_num) {
		boolean check = false;
		
		try {
			conn = DBUtil.getConnection();
			String sql = "SELECT COUNT(*) cnt FROM hotel_mng"
						+" WHERE room_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, room_num);
			
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
		T05_Hotel memObj = new T05_Hotel();
		memObj.start();
	}
}
