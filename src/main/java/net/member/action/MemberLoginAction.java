package net.member.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;

public class MemberLoginAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = "";
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			 for(int i=0;i<cookies.length;i++) {
				 if(cookies[i].getName().equals("id")) {
					 id=cookies[i].getValue();
				 }
			 }
		 }
		 
		request.setAttribute("cookie_id", id);
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("member/loginForm.jsp");
		return forward;
	}
}
