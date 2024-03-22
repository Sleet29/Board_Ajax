<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/board/header.jsp"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
        th, td {
            text-align: center;
        }
        .container {
            width: 50%;
        }
    </style>
    <title>관리자 모드(회원 정보 보기)</title>
</head>
<body>
<div class="container">
    <table class="table table-bordered">
        <tbody>
        <tr>
            <th scope="row">아이디</th>
            <td>${memberinfo.id}</td>
        </tr>
        <tr>
            <th scope="row">비밀번호</th>
            <td>${memberinfo.password}</td>
        </tr>
        <tr>
            <th scope="row">이름</th>
            <td>${memberinfo.name}</td>
        </tr>
        <tr>
            <th scope="row">나이</th>
            <td>${memberinfo.age}</td>
        </tr>
        <tr>
            <th scope="row">성별</th>
            <td>${memberinfo.gender}</td>
        </tr>
        <tr>
            <th scope="row">이메일 주소</th>
            <td>${memberinfo.email}</td>
        </tr>
        <tr>
            <td colspan="2"><a href="memberList.net" style="text-decoration: none">리스트로 돌아가기</a></td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>
