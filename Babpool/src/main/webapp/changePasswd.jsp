<%-- <%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	
	//테스트용 세션 값 (로그인 안 된 상태에서도 동작 확인용)
	session.setAttribute("email", "test@example.com");
	session.setAttribute("password", "test1234");
	
    String userEmail = (String) session.getAttribute("email");
    String userPassword = (String) session.getAttribute("password"); // 로그인 시 세션에 저장된 현재 비밀번호
    if (userEmail == null) {
%>
    <script>
        alert("로그인이 필요합니다.");
        location.href = "login.jsp";
    </script>
<%
        return;
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>비밀번호 변경</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/join.css">
    <script src="<%=request.getContextPath()%>/resource/js/changePassword.js" defer></script>
</head>
<body>

<header class="main-header">
    <div class="logo-area">
        <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="로고">
        <div class="logo-title">BabPool</div>
    </div>
    <div class="header-icons">
        <div class="header-icon-item" onclick="location.href='mainPage.jsp'">
            <span>🏠</span><span>홈</span>
        </div>
    </div>
</header>

<main class="signup-container">
    <h2>비밀번호 변경</h2>

    <form action="ChangePasswordServlet" method="post" onsubmit="return onSubmitForm();">
        <div class="form-group">
            <label for="currentPassword">현재 비밀번호</label>
            <input type="password" id="currentPassword" name="currentPassword" required>
            <span id="currentPwCheck" class="timer-text"></span>
        </div>

        <div class="form-group">
            <label for="newPassword">새 비밀번호 <span id="pw-warning"></span></label>
            <input type="password" id="newPassword" name="newPassword" required oninput="checkPasswordPattern()">
        </div>

        <div class="form-group">
            <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="submit-btn">비밀번호 변경</button>
    </form>
</main>

<footer class="page-footer">ⓒ 김동국. 박수연</footer>

<script>
    const sessionPw = "<%= userPassword %>";
    document.getElementById('confirmPassword').addEventListener('input', checkPasswordMatch);

    function onSubmitForm() {
        return validateChangePassword(sessionPw);
    }
</script>

</body>
</html> --%>

<%-- <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- ✅ Bootstrap + Custom CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">

<main class="signup-container">
    <h2>비밀번호 변경</h2>

    <form action="ChangePasswordServlet" method="post" onsubmit="return onSubmitForm();">
        <div class="form-group">
            <label for="newPassword">새 비밀번호 <span id="pw-warning"></span></label>
            <input type="password" id="newPassword" name="newPassword" required oninput="checkPasswordPattern()">
        </div>

        <div class="form-group">
            <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="submit-btn">비밀번호 변경</button>
    </form>
</main>

<script>
    function onSubmitForm() {
        // 비밀번호가 일치하는지 확인하는 함수
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        
        if (newPassword !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true;
    }
</script> --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- ✅ Bootstrap + Custom CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">

<main class="signup-container">
    <h2>비밀번호 변경</h2>

    <form action="ChangePasswordServlet" method="post">
        <div class="form-group">
            <label for="newPassword">새 비밀번호 <span id="pw-warning"></span></label>
            <input type="password" id="newPassword" name="newPassword" required oninput="checkPasswordPattern()">
        </div>

        <div class="form-group">
            <label for="confirmPassword">비밀번호 확인 <span id="pw-match-icon"></span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="submit-btn">비밀번호 변경</button>
    </form>
</main>