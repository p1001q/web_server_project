<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.sql.*, com.babpool.dao.*, com.babpool.dto.*, com.babpool.utils.DBUtil, java.util.*" %>

<%
// ✅ 세션으로 로그인 여부 확인 (수연)
com.babpool.dto.UserDTO loginUser = (com.babpool.dto.UserDTO) session.getAttribute("loginUser");
if (loginUser == null) {
%>
    <script>
        alert("로그인이 필요한 페이지입니다.");
        location.href = "login.jsp";
    </script>
<%
    return;
}

// ✅ 추가로 찜 가게 / 내가 쓴 리뷰 / 프로필 이미지 조회 (동국)
Connection conn = null;
UserDAO userDAO = null;
ReviewDAO reviewDAO = null;
BookmarkDAO bookmarkDAO = null;
StoreDAO storeDAO = null;

UserDTO user = null;
List<ReviewDTO> reviews = null;
List<BookmarkDTO> bookmarks = null;
List<StoreDTO> stores = new ArrayList<>();

try {
    conn = DBUtil.getConnection();
    userDAO = new UserDAO(conn);
    reviewDAO = new ReviewDAO(conn);
    bookmarkDAO = new BookmarkDAO(conn);
    storeDAO = new StoreDAO(conn);

    // DB에서 최신 유저 정보 가져오기 (프로필 이미지 최신화용)
    user = userDAO.getUserByEmail(loginUser.getEmail());

    // 찜한 가게, 내가 쓴 리뷰 조회
    reviews = reviewDAO.getReviewsByUserId(user.getUserId());
    bookmarks = bookmarkDAO.getBookmarkedStoresByUserId(user.getUserId());

    for (BookmarkDTO bookmark : bookmarks) {
        StoreDTO store = storeDAO.getStoreById(bookmark.getStoreId());
        stores.add(store);
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    try {
        if (conn != null) conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/myPage.css">
</head>
<body>

<%@ include file="/headerFooter.jsp" %>

<main class="mypage-container">
    <section class="profile-section">
        <form action="UploadProfileServlet" method="post" enctype="multipart/form-data">
            <label for="profileImage"> <!-- 수연 경로 오류 수정 -->
            <img id="profilePreview" 
			     src="<%= request.getContextPath() + (user != null && user.getProfileImagePath() != null 
			         ? user.getProfileImagePath() 
			         : "/resource/images/profile/default_profile.png") %>" 
			     class="profile-img" alt="프로필 이미지">
            </label>
            <input type="file" id="profileImage" name="profileImage" accept="image/*" onchange="previewImage(event)">
        </form>
        <div class="user-info">
            <p class="nickname"><%= loginUser.getNickname() %></p>
            <p class="email"><%= loginUser.getEmail() %></p>
        </div>
        <div class="profile-buttons">
            <button type="button" onclick="location.href='changePasswd.jsp'">비밀번호 변경</button>
            <button type="button" onclick="location.href='<%= request.getContextPath() %>/LogoutServlet'">로그아웃</button>
            <button type="button" onclick="location.href='DeleteUserServlet'">탈퇴</button>
        </div>
    </section>

    <section class="favorite-section">
        <h3>찜한 가게</h3>
        <div class="favorite-list">
            <% for (StoreDTO store : stores) { %>
                <div class="favorite-item">
                    <p><%= store.getName() %></p>
                    <p>⭐⭐⭐⭐☆ (<%= store.getRatingAvg() %>)</p>
                </div>
            <% } %>
        </div>
    </section>

    <section class="review-section">
        <h3>내가 쓴 리뷰</h3>
        <% for (ReviewDTO review : reviews) { %>
            <div class="review-item">
                <p><%= review.getStoreId() %> - <span>★★★★★</span> <%= review.getContent() %></p>
                <a href="#">삭제</a>
            </div>
        <% } %>
    </section>
</main>

<script>
function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function(){
        document.getElementById('profilePreview').src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}
</script>

</body>
</html>
