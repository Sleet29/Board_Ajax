package net.comment.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CommentDAO {
	private DataSource ds;
	
	public CommentDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception ex) {
            System.out.println("DB 연결 실패 : " + ex);
        }
    }
	
	public int commentsInsert(Comment c) {
		int result = 0;
		String sql = "insert into comm "
				+ " values(com_seq.nextval, ?, ?, sysdate, ?,?,?,com_seq.nextval)";
		try (Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);) {
			
			// 새로운 글을 등록하는 부분입니다.
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getContent());
			pstmt.setInt(3, c.getComment_board_num());
			pstmt.setInt(4, c.getComment_re_lev());
			pstmt.setInt(5, c.getComment_re_seq());
			result = pstmt.executeUpdate();
			if (result == 1)
					System.out.println("데이터 삽입 완료되었습니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} // commentsInsert()메서드 end
	
	 // 글의 갯수 구하기
	 public int getListCount(int comment_board_num) {
		 	int x=0;
	        String sql = "select count(*) "
	        		+ " from comm "
	        		+ "	where comment_board_num = ?";
	        try (Connection con = ds.getConnection();
	             PreparedStatement pstmt = con.prepareStatement(sql);) {
	        	
	        	pstmt.setInt(1, comment_board_num);
	        		try(ResultSet rs = pstmt.executeQuery()) {
	        			if (rs.next()) {
	        				x = rs.getInt(1);
	        			}
	        		}
	        	} catch (SQLException ex) {
	        		ex.printStackTrace();
	        		System.out.println("getListCount() 에러: " + ex);
	        }
	        return x;
	 }// getListCount() end

	 
	 public JsonArray getCommentList( int comment_board_num, int state) {
			String sort="asc"; //등록순
			if(state==2) {
				sort="desc";
			}
			String sql = "select num, comm.id, content, reg_date, comment_re_lev, "
					+ "			 comment_re_seq, "
					+ "			 comment_re_ref, member.memberfile "
					+ "	  from	 comm join member "
					+ "	  on	 comm.id=member.id "
					+ "   where	 comment_board_num = ? "
					+ "   order  by comment_re_ref " + sort + ", "
					+ "			 comment_re_seq asc";
			JsonArray array = new JsonArray();
			try (Connection con = ds.getConnection();
						PreparedStatement pstmt = con.prepareStatement(sql);) {
				
				pstmt.setInt(1, comment_board_num);
				try (ResultSet rs = pstmt.executeQuery()) {
					
					while (rs.next()) {
						JsonObject object = new JsonObject();
						object.addProperty("num", rs.getInt(1));
						object.addProperty("id", rs.getString(2));
						object.addProperty("content", rs.getString(3));
						object.addProperty("reg_date", rs.getString(4));
						object.addProperty("comment_re_lev", rs.getInt(5));
						object.addProperty("comment_re_seq", rs.getInt(6));
						object.addProperty("comment_re_ref", rs.getInt(7));
						object.addProperty("memberfile", rs.getString(8));
						array.add(object);
					}
				}
			} catch (Exception ex) {
				System.out.println("getCommentList() 에러 : " + ex);
			}
			return array;
		}// getCommentList()메서드 end
}
