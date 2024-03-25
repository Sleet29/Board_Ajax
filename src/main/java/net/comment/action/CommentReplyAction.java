package net.comment.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.comment.db.Comment;
import net.comment.db.CommentDAO;
import net.common.action.Action;
import net.common.action.ActionForward;

public class CommentReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CommentDAO dao = new CommentDAO();
		
		Comment co = new Comment();
		co.setId(request.getParameter("id"));
		co.setContent(request.getParameter("content"));
		co.setComment_re_lev(Integer.parseInt(request.getParameter("comment_re_lev")));
		co.setComment_board_num(Integer.parseInt(request.getParameter("comment_board_num")));
		co.setComment_re_seq(Integer.parseInt(request.getParameter("comment_re_seq")));
		co.setComment_re_ref(Integer.parseInt(request.getParameter("comment_re_ref")));
		
		int ok = dao.commentsReply(co);
		response.getWriter().print(ok);
		return null;
	}

}
