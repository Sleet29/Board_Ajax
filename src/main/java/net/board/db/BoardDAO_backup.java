package net.board.db;
// SQL과 관련된 객체와 List 객체, JNDI 관련 객체를 사용하기 위해 import 합니다.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO_backup {
	/*private DataSource ds;
	
	// 생성자에서 JNDI 리소스를 참조하여 Connection 객체를 얻어옵니다.
	public BoardDAO_backup() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
		}
	}
	
	// 글의 갯수 구하기
	public int getListCount() {
		String sql = "select count(*) from board";
		int x = 0;
		 try (Connection con = ds.getConnection();
			  PreparedStatement pstmt = con.prepareStatement(sql);) {
			 
			 try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					x = rs.getInt(1);
				}
			 } catch (SQLException ex) {
				 ex.printStackTrace();
				 System.out.println("getListCount() 에러: " + ex);
			 }
			 return x;
		 	}
		 }// getListCount() end
		 
		 // 글 목록 보기
		 public List<BoardBean> getBoardList(int page, int limit) {
			 
			 	// page : 페이지
			 	// limit : 페이지 당 목록의 수
			 	// board_re_ref desc, board_re_seq asc에 의해 정렬한 것을
			 	// 조건절에 맞는 rnum의 범위 만큼 가져오는 쿼리문입니다.
			 
			 String board_list_sql = "select * "
			 					 + " from (select rownum rnum, j.* "
			 					 + "		from (select board.*, nvl(cnt,0) cnt "
			 					 + "			from board left outer join (select comment_board_num,count(*) cnt"
			 					 + "										from comm"
			 					 + "										group by comment_board_num)"
			 					 + "			on board_num = comment_board_num "
			 					 + "			order by BOARD_RE_REF desc,"
			 					 + "			BOARD_RE_SEQ asc) j "
			 					 + "		where rownum<= ? "
			 					 + "		) "
			 					 + " where rnum>=? and rnum<=?";
			 
			 List<BoardBean> list = new ArrayList<BoardBean>();
			 // 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
			 
			 int startrow = (page - 1) * limit + 1;
			 int endrow = startrow + limit - 1;
			 
			 try (Connection con = ds.getConnection();
					 PreparedStatement pstmt = con.prepareStatement(board_list_sql);) {
				 			pstmt.setInt(1, endrow);
				 			pstmt.setInt(2, startrow);
				 			pstmt.setInt(3, endrow);
				 			
				 			try (ResultSet rs = pstmt.executeQuery()) {
				 				
				 				// DB에서 가져온 데이터를 BoardBean에 담습니다.
				 				while (rs.next()) {
				 					BoardBean board = new BoardBean();
				 					board.setBoard_num(rs.getInt("BOARD_NUM"));
				 					board.setBoard_name(rs.getString("BOARD_NAME"));
				 					board.setBoard_subject(rs.getString("BOARD_SUBJECT"));
				 					board.setBoard_content(rs.getString("BOARD_CONTENT"));
				 					board.setBoard_file(rs.getString("BOARD_FILE"));
				 					board.setBoard_re_ref(rs.getInt("BOARD_RE_REF"));
				 					board.setBoard_re_lev(rs.getInt("BOARD_RE_LEV"));
				 					board.setBoard_re_seq(rs.getInt("BOARD_RE_SEQ"));
				 					board.setBoard_readcount(rs.getInt("BOARD_READCOUNT"));
				 					board.setBoard_date(rs.getString("BOARD_DATE"));
				 					board.setCnt(rs.getInt("cnt"));
				 					list.add(board); // 값을 담은 객체를 리스트에 저장합니다.
				 				}
				 			} catch (SQLException e) {
				 				e.printStackTrace();
				 			}
				 			
					 } catch (Exception ex) {
						 ex.printStackTrace();
						 System.out.println("getBoardList() 에러 :" + ex);
					 }
			 return list;
	} // getBoardList()메서드 end
	*/
}