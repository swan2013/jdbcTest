package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/*
	LPROD 테이블에 새로운 데이터 추가하기
	
	lprod_gu와 lprod_nm은 직접 입력받아 처리하고,
	lprod_id는 현재의 lprod_id들 중 제일 큰값보다 1 증가된 값으로 한다.
	(기타사항 : lprod_gu도 중복되는지 검사한다.)
*/
public class T04_JdbcTest {
	
	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.print("lprod_gu의 내용을 입력해주세요>");
		String lprod_gu = s.nextLine();
		System.out.print("lprod_nm의 내용을 입력해주세요>");
		String lprod_nm = s.nextLine();
		if(selectCheck(lprod_gu)) {
			insert(lprod_Id(), lprod_gu, lprod_nm);
			System.out.println("등록되었습니다.");
		}else {
			System.out.println("중복되어 등록할 수 없습니다.");
		}
		
	}
	
	static boolean selectCheck(String lprod_gu) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;	//쿼리문이 select인 경우에 필요.
		
		boolean check = true;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC07";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);
			   
			stmt = conn.createStatement();
			
			String sql = "SELECT lprod_gu FROM lprod WHERE lprod_gu IN ('" + lprod_gu + "')";
			rs = stmt.executeQuery(sql);
			   
			   
			while(rs.next()) {
				System.out.println(rs.getString("lprod_gu"));
				if(lprod_gu.equals(rs.getString("lprod_gu"))) {
					check = false;
					break;
				}
			}
			   
		}catch (SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException e) {};
			if(stmt != null)try {stmt.close();}catch(SQLException e) {};
			if(conn != null)try {conn.close();}catch(SQLException e) {};
		}
		return check;
	}
	
	static void insert(int lprod_id, String lprod_gu, String lprod_nm) {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@192.168.201.12:1521/xe";
			String userId = "PC07";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);

			String sql = "INSERT INTO lprod "
					+ " (lprod_id, lprod_gu, lprod_nm) "
					+ " values(?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lprod_id);
			pstmt.setString(2, lprod_gu);
			pstmt.setString(3, lprod_nm);
			
			int cnt = pstmt.executeUpdate();
			System.out.println("반환값 : " + cnt);
		}catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt != null)try {stmt.close();}catch(SQLException e) {};
			if(pstmt != null)try {pstmt.close();}catch(SQLException e) {};
			if(conn != null)try {conn.close();}catch(SQLException e) {};
		}
	}
	
	static int lprod_Id() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;	//쿼리문이 select인 경우에 필요.
		
		int temp = 0;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC07";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);
			   
			stmt = conn.createStatement();
			
			String sql = "SELECT MAX(lprod_id) max FROM lprod";
			
			rs = stmt.executeQuery(sql);
			   
			System.out.println("실행한 쿼리문 : " + sql);
			System.out.println("=== 쿼리문 실행결과 ===");
			   
			while(rs.next()) {
				temp = rs.getInt(1) + 1;
			}
			   
		}catch (SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException e) {};
			if(stmt != null)try {stmt.close();}catch(SQLException e) {};
			if(conn != null)try {conn.close();}catch(SQLException e) {};
		}
		return temp;
	}
}


