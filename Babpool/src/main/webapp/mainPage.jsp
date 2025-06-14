<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.babpool.utils.DBUtil, com.babpool.utils.ApiKeyUtil" %>
<%@ page import="com.babpool.dao.CategoryDAO, com.babpool.dao.TagDAO, com.babpool.dao.MarkerDAO, com.babpool.dao.MarkerCategoryDAO, com.babpool.dao.MarkerTagDAO, com.babpool.dao.StoreDAO" %>  <!-- StoreDAO 추가 -->
<%@ page import="com.babpool.dto.CategoryDTO, com.babpool.dto.TagDTO, com.babpool.dto.MarkerDTO, com.babpool.dto.StoreDTO" %>  <!-- StoreDTO 추가 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    Locale locale = request.getLocale();
    String mapLang = locale.getLanguage();
    if (!mapLang.matches("ko|en|ja|zh")) mapLang = "ko";

    Connection conn = DBUtil.getConnection();

    CategoryDAO categoryDAO = new CategoryDAO(conn);
    TagDAO tagDAO = new TagDAO(conn);
    MarkerDAO markerDAO = new MarkerDAO(conn);
    MarkerCategoryDAO markerCategoryDAO = new MarkerCategoryDAO(conn);
    MarkerTagDAO markerTagDAO = new MarkerTagDAO(conn);

    List<CategoryDTO> categoryList = categoryDAO.getAllCategories();
    List<TagDTO> tagList = tagDAO.getAllTags();
    List<MarkerDTO> markerList = new ArrayList<>();

    String categoryIdParam = request.getParameter("categoryId");
    String tagIdParam = request.getParameter("tagId");

    if (categoryIdParam != null) {
        int categoryId = Integer.parseInt(categoryIdParam);
        List<Integer> markerIds = markerCategoryDAO.getMarkerIdsByCategoryId(categoryId);
        for (int markerId : markerIds) {
            MarkerDTO marker = markerDAO.getMarkerById(markerId);
            if (marker != null) markerList.add(marker);
        }
    } else if (tagIdParam != null) {
        int tagId = Integer.parseInt(tagIdParam);
        List<Integer> markerIds = markerTagDAO.getMarkerIdsByTagId(tagId);
        for (int markerId : markerIds) {
            MarkerDTO marker = markerDAO.getMarkerById(markerId);
            if (marker != null) markerList.add(marker);
        }
    } else {
        markerList = markerDAO.getAllMarkers();
    }

    request.setAttribute("categoryList", categoryList);
    request.setAttribute("tagList", tagList);
    request.setAttribute("markerList", markerList);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>BabPool - 메인 페이지</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/main.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/headerFooter.css">
</head>
<body>
<%@ include file="/headerFooter.jsp" %>
  <div class="main-container-wrapper">
    
    <!-- ✅ 카테고리 영역 (DB연동 기반) -->
    <aside class="category-list">
     <!-- ✅ 동국: 전체 버튼 반영 -->
      <br>
      <a href="mainPage.jsp" class="category-link">
        <div class="category-item">
          <img src="<%= request.getContextPath() %>/resource/images/reset.png" />
          <span>전체 선택</span>
        </div>
      </a>
    
      <div class="category-grid">
        <c:forEach var="cat" items="${categoryList}">
          <a href="mainPage.jsp?categoryId=${cat.categoryId}" class="category-link">
            <div class="category-item">
              <img src="<%= request.getContextPath() %>/resource/images/${cat.name}.png" />
              <span>${cat.name}</span>
            </div>
          </a>
        </c:forEach>
      </div>
      <hr class="category-divider">
      <div class="category-grid sub">
        <c:forEach var="tag" items="${tagList}">
          <a href="mainPage.jsp?tagId=${tag.tagId}" class="category-link">
            <div class="category-item">
              <img src="<%= request.getContextPath() %>/resource/images/${tag.name}.png" />
              <span>${tag.name}</span>
            </div>
          </a>
        </c:forEach>
      </div>
    </aside>

<!-- 수연 ✅ 디버깅용: storeId 확인 -->
<%/*<div>
  <h3>storeId 디버깅:</h3>
  <c:forEach var="m" items="${markerList}">
    storeId: ${m.storeId} <br/>
  </c:forEach>
</div>
*/ %>

    <!-- ✅ 지도 영역 (동국 마커 출력 포함) -->
    <section class="map-section">
      <div id="map" style="width: 100%; height: 100%; border-radius: 12px;"></div>
      <script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=<%= ApiKeyUtil.get("ncpId") %>&language=<%= mapLang %>"></script>

      <script>
        const markerList = [
          <c:forEach var="m" items="${markerList}" varStatus="loop">
            { 
              lat: ${m.tmY}, 
              lng: ${m.tmX}, 
              storeId: ${m.storeId}, 
              storeName: "${fn:replace(m.storeName, '\"', '\\\\"')}"
            }<c:if test="${!loop.last}">,</c:if>
          </c:forEach>
        ];

        let map;
        let markers = [];

        document.addEventListener("DOMContentLoaded", function () {
          map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(37.6154, 127.0115),
            zoom: 17
          });
          drawMarkers(markerList);
        });

        function drawMarkers(data) {
          clearMarkers();
          data.forEach(marker => {
            const m = new naver.maps.Marker({
              position: new naver.maps.LatLng(marker.lat, marker.lng),
              map: map
            });

            const infoWindow = new naver.maps.InfoWindow({ content: '<div style="padding:5px;">' + marker.storeName + '</div>' });
            
            naver.maps.Event.addListener(m, "mouseover", () => infoWindow.open(map, m));
            naver.maps.Event.addListener(m, "mouseout", () => infoWindow.close());
            naver.maps.Event.addListener(m, "click", function () {
                window.open('<%= request.getContextPath() %>/placeDetail?storeId=' + marker.storeId, '_blank');
            });


            markers.push(m);
          });
        }

        function clearMarkers() {
          markers.forEach(m => m.setMap(null));
          markers = [];
        }
      </script>
    </section>

    <!-- 수연 ✅ Top3 영역 (공통 유지) -->
    <%
    StoreDAO storeDAO = new StoreDAO(conn);
    List<Map<String, Object>> topStores = storeDAO.getTop3StoresWithCategory();
    request.setAttribute("topStores", topStores);
%>

<aside class="top-list">
  <h3>서경인들의 맛집 TOP 3</h3>
  <c:forEach var="store" items="${topStores}">
    <div class="top-card">
      <!-- 가게 상세 페이지로 이동할 수 있는 링크 추가 -->
      <a href="<%= request.getContextPath() %>/placeDetail?storeId=${store.storeId}" class="top-link">
        <div class="top-info">
          <!-- 수정된 부분: 아이콘을 왼쪽에 두고 텍스트는 오른쪽으로 배치 -->
          <div class="top-left">
            <img src="<%= request.getContextPath() %>/resource/images/${store.categoryName}.png" alt="${store.categoryName}">
          </div>
          <div class="top-right">
            <div class="top-name">${store.name}</div>
            <!-- 별점 출력 -->
            <div class="stars">
              <c:choose>
                <c:when test="${store.ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
                <c:when test="${store.ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
                <c:when test="${store.ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
                <c:when test="${store.ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
                <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
              </c:choose>
            </div>

            <!-- 찜 개수 출력 -->
            <div class="top-like">
              <c:choose>
                <c:when test="${isBookmarked}">
                  <span class="heart">❤️</span> <!-- 찜 표시 되어 있으면 빨간 하트 -->
                </c:when>
                <c:otherwise>
                  <span class="heart">🤍</span><!-- 찜 표시 안 되어 있으면 하얀 하트 -->
                </c:otherwise>
              </c:choose>
              <span>${store.likeCount}</span>
            </div>

            <!-- 카테고리 태그 출력 -->
            <div class="top-tag">#${store.categoryName}</div>
          </div>
        </div>
      </a>
    </div>
  </c:forEach>
</aside>




  <!--수연 ✅ 마스코트 영역 (공통 유지) -->
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

  <script>
    function toggleHeart(el) {
      const countSpan = el.nextElementSibling;
      let count = parseInt(countSpan.textContent);
      if (el.textContent === '🤍') {
        el.textContent = '❤️'; count++;
      } else {
        el.textContent = '🤍'; count--;
      }
      countSpan.textContent = count;
    }

    function openHelp() {
      document.getElementById('helpOverlay').style.display = 'flex';
    }

    function closeHelp() {
      document.getElementById('helpOverlay').style.display = 'none';
    }
  </script>

</body>
</html>


