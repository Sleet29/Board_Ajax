package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;
import net.member.db.MemberDAO;

public class MemberDeleteAction implements Action {

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MemberDAO mdao = new MemberDAO(); 
		String id = request.getParameter("id");
		response.setContentType("text/html; charset=utf-8"); 
		PrintWriter out = response.getWriter();
		int result = mdao.delete(id);
		if (result == 1) {
			out.println("<script>");
			out.println("alert('44.');"); out.println("location.href='memberList.net'");
			out.println("</script>");
		} else {
			out.println("<script>");
			out.println("alert('.');");
			out.println("history.back()");
			out.println("</script>");
		}
		out.close();
		return null;
	}

}
