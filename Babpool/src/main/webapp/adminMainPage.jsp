<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%-- <%
    // 이미 기본으로 제공되는 session 객체 사용
    String role = (String) session.getAttribute("userRole");

    // 세션이 없거나 role이 'admin'이 아닌 경우
    if (role == null || !role.equals("admin")) {
        // 관리자 권한이 없는 사용자라면 로그인 페이지로 리다이렉트
        response.sendRedirect("mainPage.jsp");
        return;
    }
%> --%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/admin.css" />
</head>
<body>
    <div class="admin-container">
        <div class="admin-logo">
            <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
            <span class="logo-title">BabPool</span>
        </div>

        <div class="admin-menu">
            <button onclick="location.href='manageStorePage.jsp'">음식점 관리</button>
            <button onclick="location.href='manageReviewPage.jsp'">리뷰 관리</button>
            <button onclick="location.href='manageUserPage.jsp'">회원 관리</button>
        </div>
    </div>
</body>
</html>
