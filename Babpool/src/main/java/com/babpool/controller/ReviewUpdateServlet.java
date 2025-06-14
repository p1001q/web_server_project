package com.babpool.controller;

import com.babpool.dao.ReviewDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/ReviewUpdateServlet")
public class ReviewUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            String content = request.getParameter("content");

            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            ReviewDTO review = dao.getReviewById(reviewId);

            boolean result = false;
            if (review != null) {
                review.setContent(content);
                result = dao.updateReview(review);
            }
            request.setAttribute("reviewUpdateResult", result);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("reviewUpdateResult", false);
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
        request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
    }
}
