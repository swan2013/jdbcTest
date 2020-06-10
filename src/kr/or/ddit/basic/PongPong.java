package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PongPong {
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@192.168.201.22:1521/xe";
			String userId = "PONGPONG";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);
			   
			stmt = conn.createStatement();
			
			String sql = "INSERT INTO lprod "
					+ " (lprod_id, lprod_gu, lprod_nm) "
					+ " values(104, 'N104', '퐁퐁이')";	
			//SQL문이 select문이 아닐 경우에는 executeUpdate()메서드를 사용한다.
			//executeUpdate()메서드는 실행에 성공한 레코드 수를 반환한다.
			int cnt = stmt.executeUpdate(sql);
			System.out.println("첫번째 반환값 : " + cnt);
		}catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt != null)try {stmt.close();}catch(SQLException e) {};
			if(conn != null)try {conn.close();}catch(SQLException e) {};
		}
	}
}
