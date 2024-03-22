package net.comment.action;

import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;


public class CommentListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		net.comment.db.CommentDAO dao = new net.comment.db.CommentDAO();
		
		//{"comment_board_num" : $("#comment_board_num").val(), state:state}, // state값이 1=>등록순, 2=>최신순
		int comment_board_num = Integer.parseInt(request.getParameter("comment_board_num"));
		System.out.println(comment_board_num);
		int state = Integer.parseInt(request.getParameter("state"));
		int listcount = dao.getListCount(comment_board_num);
		
		JsonObject object = new JsonObject();
		object.addProperty("listcount", listcount);
		
		JsonArray jarray = dao.getCommentList(comment_board_num, state);
		JsonElement je = new Gson().toJsonTree(jarray);
		object.add("commentlist", je);
		
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(object.toString());
		System.out.println(object.toString());
		return null;
	}
	

}
