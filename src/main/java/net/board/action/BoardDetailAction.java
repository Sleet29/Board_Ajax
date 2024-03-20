package net.board.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.board.db.BoardBean;
import net.board.db.BoardDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class BoardDetailAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		
		// 글번호 파라미터 값을 num 변수에 저장합니다.
		int num=Integer.parseInt(request.getParameter("num"));
		
		// 특정 주소로부터의 이동을 확인하는 데 사용할 수 있는 정보는 요청 헤더인 "Referer"에 이씃ㅂ니다.
		String referer = request.getHeader("Referer");
		if (referer !=null && referer.contains("BoardList.bo")) {
			// 내용을 확인할 글의 조회수를 증가시킵니다.
			boarddao.setReadCountUpdate(num);
		}
		
		// 글의 내용을 DAO에서 읽은 후 결과를 boarddata 객체에 저장합니다.
		boarddata = boarddao.getDetail(num);
		
		// boarddata=null;// error 테스트를 위한 값 설정
		// DAO에서 글의 내용을 읽지 못했을 경우 null을 반환합니다.
		if(boarddata==null) {
			System.out.println("상세보기 실패");
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "데이터를 읽지 못했습니다.");
			forward.setPath("error/error.jsp");
			return forward;
		}
		System.out.println("상세보기 성공");
		
		// boarddata 객체를 request 객체에 저장합니다.
		request.setAttribute("boarddata", boarddata);
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("board/boardView.jsp"); // 글 내용 보기 페이지로 이동하기 위해 경로를 설정합니다.
		return forward;
	}

}
