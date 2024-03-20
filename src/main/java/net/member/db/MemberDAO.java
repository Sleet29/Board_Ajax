package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource ds;
	
	public MemberDAO() {
		try {
			Context init = new InitialContext();
			this.ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
	}
	
	public int insert(Member m) {
		int result = 0; // 초기값 설정
		String sql = "INSERT INTO member "
				+ "(id, password, name, age, gender, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, m.getId());
			pstmt.setString(2, m.getPassword());
			pstmt.setString(3, m.getName());
			pstmt.setInt(4, m.getAge());
			pstmt.setString(5, m.getGender());
			pstmt.setString(6, m.getEmail());
			result = pstmt.executeUpdate(); // 삽입 성공시 result는 1
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}// insert end
	
	public int isId(String id) {
		int result = -1; // DB에 해당 id가 없습니다.
		String sql = "select id from member where id = ? ";
		
		try (Connection con = ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql);) {
			 pstmt.setString(1, id);
			 
			 try (ResultSet rs = pstmt.executeQuery()) {
				 if(rs.next()) {
					 result = 0; // DB에 해당 id가 있습니다.
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	} // isId end
	
	public int isId(String id, String pass) {
		int result = -1; // DB에 해당 id가 없습니다.
		String sql = "select id, password from member where id = ? ";
		try (Connection con = ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			 pstmt.setString(1, id);
			 try (ResultSet rs = pstmt.executeQuery()) {
				 if(rs.next()) {
					 if(rs.getString(2).equals(pass)) {
						 result = 1; // 아이디와 비밀번호 일치하는 경우
					 } else {
						 result = 0; // 비밀번호 일치하지 않는 경우
					 }
				 }
			 } catch (SQLException e) {
				 e.printStackTrace();
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	} // isId end
	
	public Member member_info(String id) {
		Member m = null;
		String sql = "select * from member where id = ? ";
		try (Connection con=ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					m = new Member();
					m.setId(rs.getString(1));
					m.setPassword(rs.getString(2));
					m.setName(rs.getString(3));
					m.setAge(rs.getInt(4));
					m.setGender(rs.getString(5));
					m.setEmail(rs.getString(6));
					m.setMemberfile(rs.getString(7));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
}