package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * insert 예제
 */
public class T03_JdbcTest {
	/*
		lprod_id : 101, lprod_gu : N101, lprod_nm : 농산물
		lprod_id : 102, lprod_gu : N102, lprod_nm : 수산물
		lprod_id : 103, lprod_gu : N103, lprod_nm : 축산물
		
		위 3개의 자료를 추가하기
	*/
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@192.168.201.12:1521/xe";
			String userId = "PC07";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);
			/*   
			stmt = conn.createStatement();
			
			String sql = "INSERT INTO lprod "
					+ " (lprod_id, lprod_gu, lprod_nm) "
					+ " values(103, 'N103', '축산물')";	
			//SQL문이 select문이 아닐 경우에는 executeUpdate()메서드를 사용한다.
			//executeUpdate()메서드는 실행에 성공한 레코드 수를 반환한다.
			int cnt = stmt.executeUpdate(sql);
			System.out.println("첫번째 반환값 : " + cnt);
			*/
			
			//3. PreparedStatement객체 생성 => Connection객체를 이용한다.
			String sql = "INSERT INTO lprod "
					+ " (lprod_id, lprod_gu, lprod_nm) "
					+ " values(?, ?, ?)";
			//PreparedStatement객체를 생성할 때 SQL문을 넣어서 생성한다.
			pstmt = conn.prepareStatement(sql);
			
			//쿼리문의 물음표(?)자리에 들어갈 데이터를 셋팅한다.
			//형식) pstmt.set자료형이름(물음표순번, 데이터);
			//	      물음표 순번은 1번부터 시작한다.
			pstmt.setInt(1, 101);
			pstmt.setString(2, "N101");
			pstmt.setString(3, "농산물");
			
			//데이터를 세팅한 후 쿼리문을 실행한다.
			int cnt = pstmt.executeUpdate();
			System.out.println("첫번째 반환값 : " + cnt);
			//-----------------------------------------
			pstmt.setInt(1, 101);
			pstmt.setString(2, "N102");
			pstmt.setString(3, "수산물");
			
			//데이터를 세팅한 후 쿼리문을 실행한다.
			cnt = pstmt.executeUpdate();
			System.out.println("두번째 반환값 : " + cnt);
			//-----------------------------------------
			pstmt.setInt(1, 101);
			pstmt.setString(2, "N103");
			pstmt.setString(3, "축산물");
			
			//데이터를 세팅한 후 쿼리문을 실행한다.
			cnt = pstmt.executeUpdate();
			System.out.println("세번째 반환값 : " + cnt);
			//-----------------------------------------
			
			
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
}
