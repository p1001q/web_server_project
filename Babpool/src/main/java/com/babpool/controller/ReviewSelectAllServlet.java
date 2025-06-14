package com.babpool.controller;

import com.babpool.dao.ReviewDAO;
import com.babpool.dto.ReviewDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/ReviewSelectAllServlet")
public class ReviewSelectAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            ReviewDAO dao = new ReviewDAO(conn);
            List<ReviewDTO> list = dao.getAllReviews();
            request.setAttribute("reviewList", list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
        request.getRequestDispatcher("manageReviewPage.jsp").forward(request, response);
    }
}
