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

    String storeIdStr = request.getParameter("storeId");
    if (storeIdStr == null || storeIdStr.isEmpty()) {
        response.sendRedirect("errorPage.jsp?errorType=missingStoreId");
        return;
    }
    int storeId = Integer.parseInt(storeIdStr);

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

        <textarea name="reviewText" maxlength="1000" placeholder="건강한 리뷰를 작성해주세요!"></textarea>

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
document.addEventListener("DOMContentLoaded", function() {
    // 별점 처리
    const stars = document.querySelectorAll(".star");
    const ratingInput = document.getElementById("ratingInput");
    const ratingValue = document.getElementById("ratingValue");

    function updateStars(score) {
        stars.forEach(star => {
            star.textContent = (parseInt(star.getAttribute("data-value")) <= score) ? "⭐" : "☆";
        });
    }
    updateStars(parseFloat(ratingInput.value));

    stars.forEach(star => {
        star.addEventListener("click", () => {
            const score = parseInt(star.getAttribute("data-value"));
            updateStars(score);
            ratingInput.value = score.toFixed(1);
            ratingValue.textContent = score.toFixed(1);
        });
    });

    // 이미지 미리보기 + 삭제버튼
    document.getElementById("imageInput").addEventListener("change", function(e) {
        const file = e.target.files[0];
        const previewArea = document.getElementById("previewArea");
        previewArea.innerHTML = "";
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imgContainer = document.createElement("div");
                imgContainer.classList.add("preview-container");

                const img = document.createElement("img");
                img.src = e.target.result;
                imgContainer.appendChild(img);

                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "X";
                deleteBtn.classList.add("delete-btn");

                deleteBtn.addEventListener("click", function() {
                    document.getElementById("imageInput").value = "";
                    previewArea.innerHTML = "";
                });

                imgContainer.appendChild(deleteBtn);
                previewArea.appendChild(imgContainer);
            }
            reader.readAsDataURL(file);
        }
    });

    // 클라이언트 검증
    document.getElementById("reviewForm").addEventListener("submit", function(e) {
        const reviewText = document.querySelector("textarea").value.trim();
        const rating = parseFloat(ratingInput.value);

        if (reviewText === "") {
            alert("리뷰 내용을 작성해주세요.");
            e.preventDefault();
            return;
        }
        if (isNaN(rating) || rating === 0) {
            alert("별점을 선택해주세요.");
            e.preventDefault();
            return;
        }
    });
});
</script>

</body>
</html>
