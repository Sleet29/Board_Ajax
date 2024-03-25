package net.comment.action;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;

public class CommentDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int num = Integer.parseInt(request.getParameter("num"));
		
		net.comment.db.CommentDAO dao = new net.comment.db.CommentDAO();
		
		int result = dao.commentsDelete(num);
		PrintWriter out = response.getWriter();
		out.print(result);
		
		return null;
	}

}
