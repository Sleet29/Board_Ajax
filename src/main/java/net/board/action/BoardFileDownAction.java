package net.board.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.common.action.Action;
import net.common.action.ActionForward;


public class BoardFileDownAction implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileName = request.getParameter("filename");
		System.out.println("filename = " + fileName);
		
		String savePath = "boardupload";
		
		// 서블릿의 실행 환경 정보를 담고 있는 객체를 리턴합니다.
		ServletContext context = request.getServletContext();
		String sDownloadPath = context.getRealPath(savePath);
		
		String sFilePath = sDownloadPath + File.separator + fileName;
		System.out.println(sFilePath);
		
		byte b[] = new byte[4096];
		
		// sFilePath에 있는 파일의 MimeType을 구해옵니다.
		String sMimeType = context.getMimeType(sFilePath);
		System.out.println("sMimeType>>>" + sMimeType);
		
		if (sMimeType == null)
			sMimeType = "application/octet-stream";
		
		response.setContentType(sMimeType);
		
		 // - 이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.
		String sEncoding = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
		System.out.println(sEncoding);
		
		/*
		 * Content-Disposition: attachment: 브라우저에서 다운로드하기 위해 사용됩니다.
		 */
		response.setHeader("Content-Disposition", "attachment; filename=" + sEncoding);
		
		try (
		 // 웹 브라우저로의 출력 스트림 생산합니다.
			BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
				
		 // sFile
		 BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));
		)
		
		{
			int numRead;
			// read(b, 0, b.length) : 바이트 배열 b의 0번 부터 b.length 크기 만큼 읽어옵니다.
			while ((numRead = in.read(b, 0, b.length)) != -1) {
				// 바이트 배열 b의 0번 부터 numRead크기 만큼 브라우저로 출력
				out2.write(b, 0, numRead);
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return null;
	}

}
