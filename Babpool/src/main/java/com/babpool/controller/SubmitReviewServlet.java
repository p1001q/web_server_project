package com.babpool.controller;

import com.babpool.dao.ReviewDAO;
import com.babpool.dao.ReviewImageDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.dto.ReviewImageDTO;
import com.babpool.utils.DBUtil;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/SubmitReviewServlet")
public class SubmitReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        try {
            // ✅ 업로드 경로 생성
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            int maxSize = 50 * 1024 * 1024;  // 50MB

            // ✅ MultipartRequest 생성 (파일+폼 한번에 받음)
            MultipartRequest multi = new MultipartRequest(
                    request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy());

            // ✅ 폼 데이터 파싱
            int userId = Integer.parseInt(multi.getParameter("userId"));
            String userNickname = multi.getParameter("userNickname");
            int storeId = Integer.parseInt(multi.getParameter("storeId"));
            double rating = Double.parseDouble(multi.getParameter("rating"));
            String reviewText = multi.getParameter("reviewText");

            conn = DBUtil.getConnection();

            // ✅ 리뷰 본문 저장
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            ReviewDTO review = new ReviewDTO();
            review.setUserId(userId);
            review.setNickname(userNickname);
            review.setStoreId(storeId);
            review.setRating(rating);
            review.setContent(reviewText);

            boolean insertResult = reviewDAO.insertReview(review);
            if (!insertResult) {
                throw new Exception("리뷰 저장 실패");
            }

            // ✅ 단일 이미지 파일 저장 (한장 전용)
            File file = multi.getFile("image");
            if (file != null) {
                String fileName = multi.getFilesystemName("image");

                ReviewImageDTO image = new ReviewImageDTO();
                image.setReviewId(review.getReviewId());
                image.setImagePath("uploads/" + fileName);
                image.setImageOrder(1); // 항상 1번으로 저장

                ReviewImageDAO imageDAO = new ReviewImageDAO(conn);
                imageDAO.insertReviewImage(image);
            }

            // ✅ 등록 완료 후 상세페이지 이동
            response.sendRedirect("placeDetail?storeId=" + storeId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
}
