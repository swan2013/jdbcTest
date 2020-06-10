package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Select 예제
 */
public class T02_JdbcTest {
	/*
	 문제1) 사용자로부터 lprod_id값을 입력받아 입력한 값보다 lprod_id가 큰 자료들을 출력하시오.
	 
	 문제2) lprod_id값을 2개 입력받아 두 값중 작은 값부터 큰값 사이의 자료를 출력하시오.
	*/
	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.print("문제1) 사용자로부터 lprod_id값을 입력받아 입력한 값보다 lprod_id가 큰 자료들을 출력하시오.>");
		int id = s.nextInt();
		selectPrint("SELECT * FROM lprod WHERE lprod_id > " + id);
		
		System.out.print("문제2) lprod_id값을 2개 입력받아 두 값중 작은 값부터 큰값 사이의 자료를 출력하시오.");
		int min = s.nextInt();
		int max = s.nextInt();
		selectPrint("SELECT * FROM lprod WHERE lprod_id BETWEEN " + min + " AND " + max);
	}
	
	static void selectPrint(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;	//쿼리문이 select인 경우에 필요.
		   
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			   
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC07";
			String password = "java";
			   
			conn = DriverManager.getConnection(url, userId, password);
			   
			stmt = conn.createStatement();
			 
			rs = stmt.executeQuery(sql);
			   
			System.out.println("실행한 쿼리문 : " + sql);
			System.out.println("=== 쿼리문 실행결과 ===");
			   
			while(rs.next()) {
				System.out.println("lopro_id : " + rs.getInt("lprod_id"));
				System.out.println("lopro_gu : " + rs.getString("lprod_gu"));
				System.out.println("lopro_nm : " + rs.getString("lprod_nm"));
				System.out.println("--------------------------------------");
			}
			System.out.println("출력 끝...");
			   
		}catch (SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException e) {};
			if(stmt != null)try {stmt.close();}catch(SQLException e) {};
			if(conn != null)try {conn.close();}catch(SQLException e) {};
		}
	}
}
