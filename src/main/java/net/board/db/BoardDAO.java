package net.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
    private DataSource ds;

    public BoardDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception ex) {
            System.out.println("DB 연결 실패 : " + ex);
        }
    }

    public int getListCount() {
        String sql = "select count(*) from board";
        int count = 0;
        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("getListCount() 에러: " + ex);
        }
        return count;
    }

    public List<BoardBean> getBoardList(int page, int limit) {
        String board_list_sql = "select * " +
                "from (select rownum rnum, j.* " +
                "      from (select board.*, nvl(cnt,0) cnt " +
                "            from board left outer join (select comment_board_num,count(*) cnt " +
                "                                        from comm " +
                "                                        group by comment_board_num) j " +
                "                          on board_num = comment_board_num " +
                "            order by BOARD_RE_REF desc, BOARD_RE_SEQ asc) j " +
                "      where rownum <= ?) " +
                "where rnum >= ?";

        List<BoardBean> list = new ArrayList<>();
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;

        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(board_list_sql)) {
            pstmt.setInt(1, endrow);
            pstmt.setInt(2, startrow);
            try (ResultSet rs = pstmt.executeQuery()) {
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
                    list.add(board);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("getBoardList() 에러 :" + e);
        }
        return list;
    }
    
    public boolean boardInsert(BoardBean board) {
    	int result=0;
    	String max_sql = "(select nvl(max(board_num),0)+1 from board)";

    	// 원문글의 BOARD_RE_REF 필드는 자신의 글번호 입니다.
    	String sql = "INSERT INTO board "
    			+ "(BOARD_NUM, BOARD_NAME, BOARD_PASS, BOARD_SUBJECT,"
    			+ " BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF,"
    			+ " BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT)"
    			+ " VALUES (" + max_sql + ",?, ?, ?,"
    			+ "			 ?, ?," + max_sql + ","
    			+ "			 ?,?,?)";
        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
        	
        	// 새로운 글을 등록하는 부분입니다.
            pstmt.setString(1, board.getBoard_name());
            pstmt.setString(2, board.getBoard_pass());
            pstmt.setString(3, board.getBoard_subject());
            pstmt.setString(4, board.getBoard_content());
            pstmt.setString(5, board.getBoard_file());
            
            // 원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0입니다.
            pstmt.setInt(6, 0); // BOARD_RE_LEV 필드
            pstmt.setInt(7, 0); // BOARD_RE_SEQ 필드
            pstmt.setInt(8, 0); // BOARD_READCOUNT 필드
            
            result = pstmt.executeUpdate();
            if (result == 1) {
            	System.out.println("데이터 삽입이 모두 완료되었습니다.");
            	return true;
            }
        } catch (Exception ex) {
        	System.out.println("boardInsert() 에러: " + ex);
        	ex.printStackTrace();
        }
        return false;
    }// boardInsert()메서드 end
    
    public BoardBean getDetail(int num) {
    	BoardBean board = null;
        String sql = "SELECT * FROM board WHERE BOARD_NUM=?";
        
        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, num);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    board = new BoardBean();
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
                }
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        } catch (Exception ex) {
            	System.out.println("getDetail() 에러: " + ex);
        }
        return board;
    } // getDetail()메서드 end
    
    
    public boolean isBoardWriter(int num, String pass) {
    	boolean result = false;
    	String board_sql = "select BOARD_PASS from board where BOARD_NUM=?";
    	 try (Connection con = ds.getConnection();
    		  PreparedStatement pstmt = con.prepareStatement(board_sql);) {
    		  pstmt.setInt(1, num);
    		 try (ResultSet rs = pstmt.executeQuery()) {
    			 if(rs.next()) {
    				 if (pass.equals(rs.getString("BOARD_PASS"))) {
    					 result = true;
    				 }
    			 }
    		 } catch (SQLException e) {
    			 e.printStackTrace();
    		 }
    	 } catch (SQLException ex) {
    		 System.out.println("isBoardWriter() 에러 : " + ex);
    	 }
    	 return result;
    } // isBoardWriter end

	public boolean boardModify(BoardBean modifyboard) {
		String sql = "update board "
				+ "set BOARD_SUBJECT=?, BOARD_CONTENT=?, BOARD_FILE=? "
				+ "WHERE BOARD_NUM=? ";
   	 try (Connection con = ds.getConnection();
   		  PreparedStatement pstmt = con.prepareStatement(sql);) {
   		  pstmt.setString(1, modifyboard.getBoard_subject());
   		  pstmt.setString(2, modifyboard.getBoard_content());
   		  pstmt.setString(3, modifyboard.getBoard_file());
   		  pstmt.setInt(4, modifyboard.getBoard_num());
   		  int result = pstmt.executeUpdate();
          if(result == 1) {
        	  System.out.println("성공 업데이트");
        	  return true;
          }
   	 } catch (Exception ex) {
   		 System.out.println("boardModify() 에러 : " + ex);
   	 }
   	 return false;
   }
}// class end

