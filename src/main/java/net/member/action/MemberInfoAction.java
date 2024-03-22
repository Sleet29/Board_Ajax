package net.member.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;
import net.member.db.Member;
import net.member.db.MemberDAO;

import java.io.IOException;

public class MemberInfoAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();

        MemberDAO mdao = new MemberDAO();
        String id = request.getParameter("id");
        Member m = mdao.member_info(id);

        if (m == null) {
            forward.setPath("error/error.jsp");
            request.setAttribute("message", "아이디에 해당하는 정보가 없습니다.");
            return forward;
        } else {
            forward.setPath("member/memberInfo.jsp");
            request.setAttribute("memberinfo", m);
        }
        forward.setRedirect(false);
        return forward;
    }
}
