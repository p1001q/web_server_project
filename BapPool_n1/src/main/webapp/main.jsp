<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang != null ? sessionScope.lang : 'ko'}" scope="session" />
<fmt:setBundle basename="bundle.message" />
<!DOCTYPE html>
<html lang="${sessionScope.lang != null ? sessionScope.lang : 'ko'}">
<head>
  <meta charset="UTF-8">
  <title>BobPool - 메인 페이지</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/main.css">
</head>
<body>
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
      <button type="button">🔍</button> <!-- ✅ type 명시 -->
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

  <div class="main-container-wrapper">
    <aside class="category-list">
      <div class="category-grid">
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/han.png"><span>한식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/jung.png"><span>중식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/yang.png"><span>양식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/il.png"><span>일식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/bunsik.png"><span>분식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/healthy.png"><span>건강식</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/asian.png"><span>아시안</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/alcohol.png"><span>술집</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/cafe.png"><span>카페</span></div>
      </div>
      <hr class="category-divider">
      <div class="category-grid sub">
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/solo.png"><span>혼밥</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/group.png"><span>단체석</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/delivery.png"><span>배달가능</span></div>
        <div class="category-item"><img src="<%= request.getContextPath() %>/resource/images/night.png"><span>야간/24시</span></div>
      </div>
    </aside>

    <section class="map-section">
      <div class="map-placeholder">
        <p>📍 지도 API 삽입 예정</p>
      </div>
    </section>

    <aside class="top-list">
      <h3>서경인들의 맛집 TOP 3</h3>
      <div class="top-card">
        <img src="<%= request.getContextPath() %>/resource/images/sample1.jpg" alt="식당1">
        <div class="top-info">
          <div class="top-name">A식당</div>
          <div class="top-stars"><span class="star">★</span><span class="star">★</span><span class="star">★</span><span class="star">☆</span><span class="star">☆</span></div>
          <div class="top-like"><span class="heart" onclick="toggleHeart(this)">🤍</span><span>14</span></div>
          <div class="top-tag">#중식</div>
        </div>
      </div>
      <div class="top-card">
        <img src="<%= request.getContextPath() %>/resource/images/sample2.jpg" alt="식당2">
        <div class="top-info">
          <div class="top-name">B식당</div>
          <div class="top-stars"><span class="star">★</span><span class="star">★</span><span class="star">★</span><span class="star">★</span><span class="star">☆</span></div>
          <div class="top-like"><span class="heart" onclick="toggleHeart(this)">🤍</span><span>21</span></div>
          <div class="top-tag">#한식</div>
        </div>
      </div>
      <div class="top-card">
        <img src="<%= request.getContextPath() %>/resource/images/sample3.jpg" alt="식당3">
        <div class="top-info">
          <div class="top-name">C식당</div>
          <div class="top-stars"><span class="star">★</span><span class="star">★</span><span class="star">★</span><span class="star">★</span><span class="star">★</span></div>
          <div class="top-like"><span class="heart" onclick="toggleHeart(this)">🤍</span><span>33</span></div>
          <div class="top-tag">#일식</div>
        </div>
      </div>
    </aside>
  </div>

  <div class="mascot-area">
    <img src="<%= request.getContextPath() %>/resource/images/mascot_close.png" alt="마스코트" onclick="openHelp()">
  </div>

  <div id="helpOverlay">
    <div class="help-box">
      <p>어떻게 도와줄까?</p>
      <button onclick="alert('주변 밥집 추천 중...'); closeHelp();">주변 밥집 추천해줘</button>
      <button onclick="alert('랜덤 메뉴 추천 중...'); closeHelp();">메뉴만 추천해줘</button>
      <button onclick="closeHelp()">괜찮아</button>
    </div>
  </div>

  <footer class="page-footer">
    ⓒ 김동국. 박수연
  </footer>
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
      console.log("⏰ 시계 텍스트:", clockText); // 디버깅용 출력
    } else {
      console.error("❌ 시계 요소 (#clock) 없음");
    }
  }

  window.onload = function () {
    updateClock(); // 초기 실행
    setInterval(updateClock, 1000); // 매초 갱신
  };
   
    function openHelp() {
      document.getElementById('helpOverlay').style.display = 'flex';
    }
    function closeHelp() {
      document.getElementById('helpOverlay').style.display = 'none';
    }

    function toggleHeart(el) {
      const countSpan = el.nextElementSibling;
      let count = parseInt(countSpan.textContent);
      if (el.textContent === '🤍') {
        el.textContent = '❤️';
        count++;
      } else {
        el.textContent = '🤍';
        count--;
      }
      countSpan.textContent = count;
    }
  </script>
 
</body>
</html>
