<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<html>
<head>
<title>로그인 페이지</title>
 <link rel="icon" href="image/orange.svg">
 <link href="css/login.css" type = "text/css" rel="stylesheet">
  <script src="<%=request.getContextPath()%>/js/jquery-3.7.1.min.js"></script>
<script>

	$(function(){ 
		$(".join").click(function() {
			location.href = "join.net";
		});
		
		const id = '${cookie_id}';
		if (id) {
			$("#id").val(id);
			$("#remember").prop('checked',true);
		}
	})
</script>
</head>
<body>
	<form action="loginProcess.net" method="post" class="border-light p-5" >
	 	<h1>로그인</h1>
	 	<hr>
		<b>아이디</b>
		<input type="text" name="id" placeholder="Enter id" id="id" required>

		<b>Password</b>
		<input type="password" name="pass" placeholder="Enter password" required> 
		<input type="checkbox" id="remember" name="remember" value="store">
		<span>remember</span>

		<div class="clearfix">
			<button type="submit" class="submitbtn">submit</button> 
			<button type="button" class="join">join us</button>
		</div>
	</form>
	 	
</body>
</html>