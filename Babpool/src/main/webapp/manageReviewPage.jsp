<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.babpool.dto.ReviewDTO" %>
<%@ page import="com.babpool.dao.ReviewDAO" %>
<%@ page import="com.babpool.utils.DBUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>리뷰 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/manageReview.css" />
</head>
<body>
    <div class="manage-container">
        <h2>리뷰 관리</h2>

        <!-- 전체 조회 -->
        <form action="ReviewSelectAllServlet" method="get">
            <button type="submit" class="btn">전체 조회</button>
        </form>

        <!-- 단일 조회 -->
        <form action="ReviewSelectByIdServlet" method="get">
            <input type="number" name="reviewId" placeholder="리뷰 ID 입력" required />
            <button type="submit" class="btn">선택 조회</button>
        </form>

        <!-- 수정 -->
        <form action="ReviewUpdateServlet" method="post">
            <input type="number" name="reviewId" placeholder="수정할 리뷰 ID" required />
            <input type="text" name="content" placeholder="리뷰 내용 수정" />
            <button type="submit" class="btn">수정</button>
        </form>

        <c:if test="${not empty reviewUpdateResult}">
            <div class="result-message">
                <c:choose>
                    <c:when test="${reviewUpdateResult}">
                        ✅ 리뷰 수정 성공
                    </c:when>
                    <c:otherwise>
                        ❌ 리뷰 수정 실패
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 삭제 -->
        <form action="ReviewDeleteServlet" method="post">
            <input type="number" name="reviewId" placeholder="삭제할 리뷰 ID" required />
            <button type="submit" class="btn">삭제</button>
        </form>

        <c:if test="${not empty reviewDeleteResult}">
            <div class="result-message">
                <c:choose>
                    <c:when test="${reviewDeleteResult}">
                        ✅ 리뷰 삭제 성공
                    </c:when>
                    <c:otherwise>
                        ❌ 리뷰 삭제 실패
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 전체 조회 결과 출력 -->
        <c:if test="${not empty reviewList}">
            <h3>전체 리뷰 목록</h3>
            <table class="simple-table">
                <tr>
                    <th>리뷰 ID</th>
                    <th>유저 ID</th>
                    <th>닉네임</th>
                    <th>가게 ID</th>
                    <th>평점</th>
                    <th>작성일</th>
                    <th>내용</th>
                </tr>
                <c:forEach var="review" items="${reviewList}">
                    <tr>
                        <td>${review.reviewId}</td>
                        <td>${review.userId}</td>
                        <td>${review.nickname}</td>
                        <td>${review.storeId}</td>
                        <td>${review.rating}</td>
                        <td><fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td>${review.content}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <!-- 단일 조회 결과 출력 -->
        <c:if test="${not empty reviewOne}">
            <h3>리뷰 상세 정보</h3>
            <div class="simple-box">
                <p>리뷰 ID: ${reviewOne.reviewId}</p>
                <p>유저 ID: ${reviewOne.userId}</p>
                <p>닉네임: ${reviewOne.nickname}</p>
                <p>가게 ID: ${reviewOne.storeId}</p>
                <p>평점: ${reviewOne.rating}</p>
                <p>작성일: <fmt:formatDate value="${reviewOne.createdAt}" pattern="yyyy-MM-dd HH:mm" /></p>
                <p>내용: ${reviewOne.content}</p>
            </div>
        </c:if>

    </div>
</body>
</html>