package kr.or.ddit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * properties파일의 내용으로 DB정보를 설정하는 방법
 * 1)Properties객체 이동하기
 */
public class DBUtil2 {
	static Properties prop;	//Properties객체 변수 선언
	
	static {
		prop = new Properties();	//객체 생성
		
		File file = new File("res/db.properties");
		
		try {
			FileInputStream fin = new FileInputStream(file);
			
			prop.load(fin);
			
			Class.forName(prop.getProperty("driver"));
		}catch(IOException e) {
			System.out.println("파일이 없거나 입출력 오류입니다.");
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(prop.getProperty("url"),
											   prop.getProperty("user"),
											   prop.getProperty("pass"));
		}catch(SQLException e) {
			System.out.println("DB연결 실패!!!");
			e.printStackTrace();
			return null;
		}
	}
}
