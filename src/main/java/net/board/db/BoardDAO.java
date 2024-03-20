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
    
    // 글쓴이인지 확인 - 비밀번호로 확인합니다.
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
	
	public int boardReply(BoardBean board) {
		int num=0;
		
		try (Connection con = ds.getConnection(); ) {
   		  // 트랜잭션을 이용하기 위해서 setAutoCommit을 false로 설정합니다.
   		  con.setAutoCommit(false);
   			 
   		  try {
   			  reply_update(con, board.getBoard_re_ref(), board.getBoard_re_seq());
   			  
   			  num=reply_insert(con,board);
   			  con.commit();
   		  }
   		  catch(SQLException e) {
   			  e.printStackTrace();
   			  
   			  if (con != null) {
   				  try {
   					  con.rollback(); // rollback합니다.
   				  } catch (SQLException ex) {
   					  ex.printStackTrace();
   				  }
   			  }
   		  }
   		  con.setAutoCommit(true);
   	 } catch (Exception ex) {
   		 ex.printStackTrace();
   		 System.out.println("boardReply() 에러 : " + ex);
   	 }
   	 return num;
   }// boardReply()메서드 end
	
	public void reply_update(Connection con, int re_ref, int re_seq) throws SQLException {
		// BOARD_RE_REF, BOARD_RE_SEQ 값을 확인하여 원문 글에 답글이 달려있다면
		// 달린 답글들의 BOARD_RE_SEQ값을 1씩 증가시킵니다.
		// 현재 글을 이미 달린 답글보다 앞에 출력되게 하기 위해서 입니다.
		
		String sql = "update board "
				+ "set 		BOARD_RE_SEQ=BOARD_RE_SEQ + 1 "
				+ "where 	BOARD_RE_REF = ? "
				+ "and 		BOARD_RE_SEQ > ? ";
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
		}
	}
	
	public int reply_insert(Connection con,BoardBean board) throws SQLException {
		int num=0;
		String board_max_sql = "(select max(board_num)+1 from board)";
		try(PreparedStatement pstmt = con.prepareStatement(board_max_sql);) {
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					num=rs.getInt(1);
				}
			}
		}
		
		String sql = "insert into board "
				+ "(BOARD_NUM, BOARD_NAME, BOARD_PASS, BOARD_SUBJECT,"
				+ " BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF,"
				+ " BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT) "
				+ " VALUES(?,?,?,?,"
				+ "		   ?,?,?,"
				+ "		   ?,?,?)";
			try(PreparedStatement pstmt = con.prepareStatement(sql);) {
				pstmt.setInt(1, num);
				pstmt.setString(2, board.getBoard_name());
				pstmt.setString(3, board.getBoard_pass());
				pstmt.setString(4, board.getBoard_subject());
				pstmt.setString(5, board.getBoard_content());
				pstmt.setString(6, ""); // 답변에는 파일을 업로드하지 않습니다.
				pstmt.setInt(7, board.getBoard_re_ref()); // 원문의 글번호
				pstmt.setInt(8, board.getBoard_re_lev() + 1);
				pstmt.setInt(9, board.getBoard_re_seq() + 1);
				pstmt.setInt(10, 0); // BOARD_READCOUNT(조회수)는 0
				pstmt.executeUpdate();
			}
				
		return num;
	} 
	
	public boolean boardDelete(int num) {
		String select_sql = "select BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ "
				+ "			 FROM BOARD"
				+ "			 WHERE BOARD_NUM=?";
		
		String board_delete_sql = "delete from board"
				+ "				   where BOARD_RE_REF = ?"
				+ "				   AND 	 BOARD_RE_LEV >=?"
				+ "				   AND 	 BOARD_RE_SEQ >=?"
				+ "				   AND 	 BOARD_RE_SEQ <=("
				+ "										NVL((SELECT MIN(BOARD_RE_SEQ)-1"
				+ "												FROM 	BOARD  "
				+ "												WHERE 	BOARD_RE_REF=?"
				+ "												AND 	BOARD_RE_LEV=?"
				+ "												AND 	BOARD_RE_SEQ>?) ,"
				+ "												(SELECT MAX(BOARD_RE_SEQ) "
				+ "												 FROM BOARD "
				+ "												 WHERE BOARD_RE_REF=? ))"
				+ "												)";
		boolean result_check = false;
		
		try (Connection con = ds.getConnection();
			 PreparedStatement pstmt = con.prepareStatement(select_sql);) {
		
				pstmt.setInt(1, num);
				try(ResultSet rs = pstmt.executeQuery();) {
					if(rs.next()) {
						try(PreparedStatement pstmt2 = con.prepareStatement(board_delete_sql)) {
							pstmt2.setInt(1, rs.getInt("BOARD_RE_REF"));
							pstmt2.setInt(2, rs.getInt("BOARD_RE_LEV"));
							pstmt2.setInt(3, rs.getInt("BOARD_RE_SEQ"));
							pstmt2.setInt(4, rs.getInt("BOARD_RE_REF"));
							pstmt2.setInt(5, rs.getInt("BOARD_RE_LEV"));
							pstmt2.setInt(6, rs.getInt("BOARD_RE_SEQ"));
							pstmt2.setInt(7, rs.getInt("BOARD_RE_REF"));
							int count=pstmt2.executeUpdate();
							if(count >=1)
								result_check = true; // 삭제가 안된 경우에는 false를 반환합니다.
							
						}// try 3
					}// if(rs.next()) {
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} catch (Exception ex) {
			System.out.println("boardDelte() 에러: " + ex);
			ex.printStackTrace();
		}
	
		return result_check;
	}// boardDelete()메서드 end

	public void setReadCountUpdate(int num) {
		String sql = "update board "
				+ "SET BOARD_READCOUNT = BOARD_READCOUNT+1 "
				+ "WHERE BOARD_NUM=?"; 
		try (Connection con = ds.getConnection();
		   	 PreparedStatement pstmt = con.prepareStatement(sql);) {
	            pstmt.setInt(1, num);
	            pstmt.executeQuery();
	        } catch (SQLException ex) {
	        	System.out.println("setReadCountUpdate() 에러: " + ex);
	        }
		
	}// setReadCountUpdate()메서드 end
}// class end

