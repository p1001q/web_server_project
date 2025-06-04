<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>BobPool - 로그인</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/bootstrap.min.css" />
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/login.css" />
</head>

<body>
  <!-- ✅ 헤더 -->
  <header class="main-header">
    <div class="logo-area">
      <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
      <span class="logo-title">BobPool</span>
    </div>
    <div class="header-icons">
      <div class="header-icon-item">
        <span>🏠</span>
        <span><a href="<%= request.getContextPath() %>/main.jsp">홈</a></span>
      </div>
    </div>
  </header>

  <!-- ✅ 로그인 박스 -->
  <div class="login-wrapper">
    <form action="<%= request.getContextPath() %>/LoginServlet" method="post" class="login-box">
      <!-- 입력창 영역 -->
      <div class="input-fields">
        <input type="email" name="email" placeholder="이메일" required />
        <input type="password" name="password" placeholder="비밀번호" required />
        <div class="links">
          <a href="<%= request.getContextPath() %>/join.jsp">회원가입</a> /
          <a href="<%= request.getContextPath() %>/changePw.jsp">비밀번호 변경</a>
        </div>
      </div>

      <!-- 로그인 버튼 영역 -->
      <div class="login-btn-area">
        <button type="submit" class="login-btn">로그인</button>
      </div>
    </form>
  </div>

  <!-- ✅ 푸터 -->
  <footer class="page-footer">
    ⓒ 김동국. 박수연
  </footer>
</body>
</html>
