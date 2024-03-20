package net.board.action;

import java.io.IOException;
import java.io.PrintWriter;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class BoardReplyProcessAction implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		int result = 0;
		
		// 파라미터로 넘어온 값들을 boarddata 객체에 저장합니다.
		BoardBean boarddata = new BoardBean();
		boarddata.setBoard_name(request.getParameter("board_name"));
		boarddata.setBoard_pass(request.getParameter("board_pass"));
		boarddata.setBoard_subject(request.getParameter("board_subject"));
		boarddata.setBoard_content(request.getParameter("board_content"));
		boarddata.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
		boarddata.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
		boarddata.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));
		
		// 답변을 DB에 저장하기위해 boarddata 객체를 파라미터로 전달하고
		// DAO의 메서드 boardReply를 호출합니다.
		result = boarddao.boardReply(boarddata);
		
		// 답변 저장에 실패한 경우
		if (result == 0) {
			System.out.println("답장 저장 실패");
			forward.setRedirect(false);
			request.setAttribute("message", "답장 저장 실패입니다.");
			forward.setPath("error/error.jsp");
		} else {
			
			// 답변 저장이 제대로 된 경우
			System.out.println("답장 완료");
			forward.setRedirect(true);
			
			// 답변 글 내용을 확인하기 위해 글 내용 보기 페이지를 경로로 설정합니다.
			forward.setPath("BoardDetail.bo?num=" + result);
		}
		
		return forward;
	}
}
