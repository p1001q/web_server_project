<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>공통 헤더 푸터 미리보기</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/common.css">
</head>
<body>

<!-- 헤더 시작 -->
<header class="main-header">
  <div class="logo-area">
    <img src="<%= request.getContextPath() %>/resource/images/sk_logo.png" alt="서경대학교 로고">
    <span class="logo-title">BobPool</span>
    <span class="clock" id="clock">--:--</span>
  </div>

  <div class="search-bar">
    <input type="text" placeholder="검색어 입력">
    <select id="searchType">
      <option value="food" selected>음식명</option>
      <option value="store">상호명</option>
    </select>
    <button type="button">🔍</button>
  </div>

  <div class="header-icons">
    <div class="header-icon-item">
      <span>🔑</span>
      <span>로그인</span>
    </div>
    <div class="header-icon-item">
      <span>🏠</span>
      <span>홈</span>
    </div>
  </div>
</header>
<!-- 헤더 끝 -->

<!-- 시계 스크립트 -->
<script>
  function pad2(n) {
    return (n < 10 ? '0' + n : '' + n);
  }

  function updateClock() {
    const now = new Date();
    const month = pad2(now.getMonth() + 1);
    const date = pad2(now.getDate());
    const hours = pad2(now.getHours());
    const minutes = pad2(now.getMinutes());
    const clockText = month + '/' + date + '<br>' + hours + ':' + minutes;
    const clockElement = document.getElementById('clock');
    if (clockElement) {
      clockElement.innerHTML = clockText;
    }
  }

  window.onload = function () {
    updateClock();
    setInterval(updateClock, 1000);
  };
</script>

<!-- (본문 없음: 바디 테스트용) -->

<!-- 푸터 시작 -->
<footer class="page-footer">
  ⓒ 김동국. 박수연
</footer>
<!-- 푸터 끝 -->

</body>
</html>
