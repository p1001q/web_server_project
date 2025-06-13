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
import java.util.Enumeration;

@WebServlet("/SubmitReviewServlet")
public class SubmitReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        try {
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            int maxSize = 50 * 1024 * 1024;

            MultipartRequest multi = new MultipartRequest(
                    request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy());

            // 폼 데이터 수신
            int userId = Integer.parseInt(multi.getParameter("userId"));
            String userNickname = multi.getParameter("userNickname");
            int storeId = Integer.parseInt(multi.getParameter("storeId"));
            String storeName = multi.getParameter("storeName");
            double rating = Double.parseDouble(multi.getParameter("rating"));
            String reviewText = multi.getParameter("reviewText");

            conn = DBUtil.getConnection();

            ReviewDAO reviewDAO = new ReviewDAO(conn);
            ReviewDTO review = new ReviewDTO();
            review.setUserId(userId);  // <-- ✅ 핵심
            review.setNickname(userNickname);
            review.setStoreId(storeId);
            review.setRating(rating);
            review.setContent(reviewText);
            boolean insertResult = reviewDAO.insertReview(review);

            if (!insertResult) {
                throw new Exception("리뷰 저장 실패");
            }

            Enumeration files = multi.getFileNames();
            int imageOrder = 1;
            ReviewImageDAO imageDAO = new ReviewImageDAO(conn);

            while (files.hasMoreElements()) {
                String fileInputName = (String) files.nextElement();
                File file = multi.getFile(fileInputName);
                if (file != null) {
                    String fileName = file.getName();

                    ReviewImageDTO image = new ReviewImageDTO();
                    image.setReviewId(review.getReviewId());
                    image.setImagePath("uploads/" + fileName);
                    image.setImageOrder(imageOrder++);

                    imageDAO.insertReviewImage(image);
                }
            }

            response.sendRedirect("placeDetail?storeId=" + storeId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
}
