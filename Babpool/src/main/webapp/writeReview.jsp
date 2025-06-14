<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.Connection, com.babpool.dto.UserDTO, com.babpool.dao.StoreDAO, com.babpool.dto.StoreDTO, com.babpool.utils.DBUtil" %>

<%
    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
    if (loginUser == null) {
%>
    <script>
        alert("리뷰 작성은 로그인 후 이용 가능합니다.");
        location.href = "login.jsp";
    </script>
<%
        return;
    }

    int userId = loginUser.getUserId();
    String userNickname = loginUser.getNickname();

    int storeId = Integer.parseInt(request.getParameter("storeId"));
    String storeName = "";

    Connection conn = null;
    try {
        conn = DBUtil.getConnection();
        StoreDAO storeDAO = new StoreDAO(conn);
        StoreDTO storeDTO = storeDAO.getStoreById(storeId);
        if (storeDTO != null) {
            storeName = storeDTO.getName();
        } else {
            storeName = "가게 정보 없음";
        }
    } catch(Exception e) {
        storeName = "가게 정보 조회 실패";
    } finally {
        try { if (conn != null) conn.close(); } catch(Exception ex) {}
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>리뷰 작성</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/join.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/writeReview.css">
</head>

<body>

<%@ include file="/headerFooter.jsp" %>

<div class="review-container">
    <form id="reviewForm" action="SubmitReviewServlet" method="post" enctype="multipart/form-data">
        <input type="hidden" name="userId" value="<%= userId %>">
        <input type="hidden" name="userNickname" value="<%= userNickname %>">
        <input type="hidden" name="storeId" value="<%= storeId %>">

        <h3><%= storeName %></h3>

	    <div class="star-rating">
		    <div class="stars-line">
		        <span class="star" data-value="1">☆</span>
		        <span class="star" data-value="2">☆</span>
		        <span class="star" data-value="3">☆</span>
		        <span class="star" data-value="4">☆</span>
		        <span class="star" data-value="5">☆</span>
		        <span class="rating-value" id="ratingValue">5.0</span>
		    </div>
		    <input type="hidden" name="rating" id="ratingInput" value="5.0">
		</div>
	

        <textarea name="reviewText" maxlength="1000" placeholder="건강한 리뷰를 작성해주세요!" oninput="updateCharCount()"></textarea>
        <div class="char-count" id="charCount">0 / 1000</div>

        <div class="image-upload">
            <label>리뷰 사진 (한 장만 골라 첨부해주세요.)</label>
          <input type="file" id="imageInput" name="image" accept="image/*">
        </div>

        <div class="preview-area" id="previewArea"></div>

        <div class="button-row">
            <button type="submit" class="submit-btn"><span>작성</span></button>
            <button type="button" class="cancel-btn" onclick="history.back()"><span>취소</span></button>
        </div>
    </form>
</div>

<script>
// 글자수 카운팅
function updateCharCount() {
    const text = document.querySelector("textarea").value || "";
    document.getElementById("charCount").textContent = `${text.length} / 1000`;
}

//미리보기 (한 장 전용)
document.getElementById("imageInput").addEventListener("change", function(e) {
    const file = e.target.files[0];
    const previewArea = document.getElementById("previewArea");
    previewArea.innerHTML = "";
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement("img");
            img.src = e.target.result;
            previewArea.appendChild(img);
        }
        reader.readAsDataURL(file);
    }
});

// ⭐⭐ 별점 이벤트 (최종 안정화 포인트)
const stars = document.querySelectorAll(".star");
const ratingInput = document.getElementById("ratingInput");
const ratingValue = document.getElementById("ratingValue");
updateStars(parseFloat(ratingInput.value));

stars.forEach(star => {
    star.addEventListener("click", () => {
        const score = parseInt(star.getAttribute("data-value"));
        updateStars(score);
        ratingInput.value = score.toFixed(1);
        ratingValue.textContent = score.toFixed(1);
    });
});

function updateStars(score) {
    stars.forEach(star => {
        star.textContent = (parseInt(star.getAttribute("data-value")) <= score) ? "⭐" : "☆";
    });
}

</script>

</body>
</html>
