<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>회원가입 페이지</title>
	<link rel="icon" href="image/orange.svg">
	<link href="${pageContext.request.contextPath}/css/join.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
<script>
	$(function() {
		let checkid = false;	// 아이디의 정규식 체크하기 위한 변수로 기본값은 false, 규칙에 맞게 입력하면 true 값을 갖습니다.
		let checkemail = false;  // 이메일의 정규식 체크하기 위한 변수로 기본값은 false, 규칙에 맞게 입력하면 true 값을 갖습니다.
		
		$("input[name=id]").on('keyup',
				function() {
			
					//[A-Za-z0-9_]의 의미가 \w
					const pattern = /^\w{5,12}$/;
					const id = $(this).val();
					if (!pattern.test(id)) {
						$("#id_message").css('color', 'red')
										.html("영문자 숫자 _로 5~12자 가능합니다.");
						
						checkid=false;
						return;
					}
					
					$.ajax({
						url : "idcheck.net",
						data : {"id" : id},
						success : function(resp) {
							if (resp == '-1') { // db에 해당 id가 없는 경우
								$("#id_message").css('color', 'green').html("사용 가능한 아이디 입니다.");	
								checkid=true;
							} else { // db에 해당 id가 있는 경우 ('0')
								$("#id_message").css('color', 'blue').html("사용중인 아이디 입니다.");
								checkid=false;
							}
						}
					}); // ajax end
		}) // id keyup end
		
		
		$("input[name=email]").on('keyup',
				function() {
					// [A-Za-z0-9_]와 동일한 것이 \w 입니다.
					// +는 1회 이상 반복을 의미하고 {1,}와 동일합니다.
					// \w+ 는 [A-Za-z0-9_]를 1개이상 사용하라는 의미입니다.
					const pattern = /^\w+@\w+[.][A-Za-z0-9]{3}$/;
					const email_value = $(this).val();
					
					if (!pattern.test(email_value)) {
						$("#email_message").css('color','red')
										   .html("이메일형식이 맞지 않습니다.");
						checkemail=false;
						
					} else {
						$("#email_message").css('color','green')
										   .html("이메일형식에 맞습니다.");
						checkemail=true;
					}
		}); // email keyup 이벤트 처리 끝
		
		$('form').submit(function() {
			if(!$.isNumeric($("input[name='age']").val())) {
				alert("나이는 숫자를 입력하세요");
				$("input[name='age']").val('').focus();
				return false;
			}
			
			if(!checkid) {
				alert("사용가능한 id로 입력하세요.");
				$("input[name=id]").val('').focus();
				$("#id_message").text('');
				return false;
			}
			
			if(!checkemail) {
				alert("email 형식을 확인하세요");
				$("input[name=email]").focus();
				return false;
			}
		}); // submit
	}) // ready
</script>
<style>
#opener_message {
	margin-top:-10px;
	margin-bottom:10px;
}
</style>
</head>

<body>
    <form name="joinform" action="joinProcess.net" method="post">
            <h1>회원 가입</h1>
            <hr>
            <b>아이디</b>
            <input type="text" placeholder="Enter id" name="id" maxLength="12" required>
            <span id="id_message"></span>

			<b>비밀번호</b><input
				 type="password" placeholder="Enter Password" name="pass" required>

			<b>이름</b><input
				 type="text" placeholder="Enter name" name="name" maxLength="5" required>

			<b>나이</b><input
				 type="text" placeholder="Enter age" name="age" maxLength="2" required>

            <b>성별</b>
            <div>
                <input type="radio" name="gender" value="남" checked><span>남자</span>
                <input type="radio" name="gender" value="여"><span>여자</span>
            </div>

			<b>이메일 주소</b>
			<input type= "text" name="email" placeholder="Enter email" maxLength="30" required>
			<span id="email_message"></span>
			<div class="clearfix">
				<button type="submit" class="submitbtn">회원가입</button>
				<button type="reset" class="cancelbtn">다시작성</button>
				
			</div>
            
    </form>
</body>
</html>
