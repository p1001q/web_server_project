<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${store.name} 상세 정보</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/resource/css/placeDetail.css">
</head>
<body>

<%@ include file="headerFooter.jsp" %>

<section class="store-detail">

  <!-- 📸 사진 섹션 -->
  <section class="photo-section">
    <div class="photo-slider">
      <div class="photo-slider">
		  <img src="/Babpool/resource/images/cafe.png" alt="사진1">
		  <img src="/Babpool/resource/images/asian.png" alt="사진2">
		  <img src="/Babpool/resource/images/bunsik.png" alt="사진3">
		  <img src="/Babpool/resource/images/japanese.png" alt="사진4">
		  <img src="/Babpool/resource/images/western.png" alt="사진5">
		</div>
    </div>
  </section>

  <!-- 🏠 기본 정보 + 찜/카테고리 -->
  <section class="info-section">
    <div class="left-box">
      <div class="title-rating-box">
        <h2 class="store-name">${store.name}</h2>
        <div class="rating-box">
        
          <!-- ⭐ 이모티콘 조건문 삽입 -->
		  <span class="rating-stars">
		    <c:choose>
		      <c:when test="${ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
		      <c:when test="${ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
		      <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
		    </c:choose>
            <fmt:formatNumber value="${ratingAvg}" type="number" maxFractionDigits="1" />
          </span>
          
          <span class="rating-count">(${totalCount}명)</span>
        </div>
      </div>
      <div class="store-meta">
        <p>주소: ${store.address}</p>
        <p>운영시간: ${store.openTime}</p>
        <p>연락처: ${store.phone}</p>
      </div>

      <div class="menu-box">
        <h3>대표 메뉴</h3>
        <ul>
          <c:forEach var="menu" items="${menus}">
            <li>${menu.name} - ${menu.price}원</li>
          </c:forEach>
        </ul>
      </div>

      <div class="left-bottom-btn">
        <a class="direction-btn circle" href="${marker.url}" target="_blank">도보<br>길찾기</a>
      </div>
    </div>

    <div class="right-box">
		<div class="like-container">
		  <div class="like-box ${isBookmarked ? 'active' : ''}">
		    <c:choose>
			  <c:when test="${isBookmarked == true}"><%= "❤️" %></c:when>
			  <c:otherwise><%= "🤍" %></c:otherwise>
			</c:choose>
		  </div>
		  <span class="like-count">${likeCount}</span>
		</div>

	 <div class="category-box">
	  <c:forEach var="category" items="${categories}">
	    <span>#${category.name}</span>
	  </c:forEach>
	</div>
	
	<div class="tag-box">
	  <c:forEach var="tag" items="${tags}">
	    <span>#${tag.name}</span>
	  </c:forEach>
	</div>

    </div>
  </section>

  <!-- 📊 리뷰 요약 -->
  <section class="review-summary-section">
    <h3>풀잎이들의 후기</h3>
    <div class="review-summary-box">
    
    <!-- 별점 바 -->
      <div class="score-bar">
     	<c:forEach var="score" begin="1" end="5" step="1">
		  <c:set var="realScore" value="${6 - score}" />
		  <c:forEach var="entry" items="${scoreMap}">
		    <c:if test="${entry.key == realScore}">
		      <c:set var="count" value="${entry.value}" />
		    </c:if>
		  </c:forEach>
		  <div>${realScore}점 
		    <div class="bar" style="width:${totalCount > 0 ? (count * 80) / totalCount : 0}%"></div> 
		    <c:out value='${count}' default='0' />명
		  </div>
		</c:forEach>
      </div>
      
       <!-- 오른쪽 별점 평균 영역 -->
    <div class="review-average">
      <div class="stars">
        <c:choose>
          <c:when test="${ratingAvg <= 1.5}">⭐ ☆ ☆ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 2.5}">⭐⭐ ☆ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 3.5}">⭐⭐⭐ ☆ ☆</c:when>
          <c:when test="${ratingAvg <= 4.5}">⭐⭐⭐⭐ ☆</c:when>
          <c:otherwise>⭐⭐⭐⭐⭐</c:otherwise>
        </c:choose>
      </div>
      <div class="score">
        <fmt:formatNumber value="${ratingAvg}" type="number" maxFractionDigits="1" />
      </div>
    </div>
      
    </div>
  </section>

  <!-- 🗣 리뷰 리스트 -->
  <section class="review-section">
    <div class="review-top-layout">
      <div class="review-row1">
        <p class="review-count-text">${totalCount}명이 후기를 남겨주었어요!</p>
      </div>
      <div class="review-row2">
        <form method="get" action="placeDetail">
          <input type="hidden" name="storeId" value="${store.storeId}">
          <select name="sortType" onchange="this.form.submit()">
            <option value="recent" ${sortType == 'recent' ? 'selected' : ''}>최신순</option>
            <option value="high" ${sortType == 'high' ? 'selected' : ''}>평점 높은 순</option>
            <option value="low" ${sortType == 'low' ? 'selected' : ''}>평점 낮은 순</option>
          </select>
        </form>
        <button class="write-review-btn" onclick="location.href='writeReview.jsp?storeId=${store.storeId}'">✏️ 나도 후기 쓰기</button>
      </div>
    </div>

    <div class="review-list">
      <c:forEach var="review" items="${reviews}">
      
        <div class="review-item review-card">
          <div class="review-user">
            <img src="${pageContext.request.contextPath}${review.profileImagePath}" style="width:40px;height:40px;border-radius:50%;"> ${review.nickname}
          </div>
          <div class="review-date">${review.createdAt}</div>
          <div class="review-stars">${review.rating}점</div>
          <p class="review-text">${review.content}</p>
          
       	  <c:if test="${not empty review.images}">
        	<div class="review-images">
				<c:forEach var="img" items="${review.images}">
			      <img src="${pageContext.request.contextPath}/${img.imagePath}" />
			   	</c:forEach>
			</div>
		  </c:if>         
        </div>
        
      </c:forEach>
    </div>
  </section>

</section>

</body>
</html>