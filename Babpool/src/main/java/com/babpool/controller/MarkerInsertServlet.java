package com.babpool.controller;


import com.babpool.dao.MarkerDAO;
import com.babpool.dto.MarkerDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/MarkerInsertServlet")
public class MarkerInsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            request.setCharacterEncoding("UTF-8");

            MarkerDTO dto = new MarkerDTO();
            dto.setStoreId(Integer.parseInt(request.getParameter("storeId")));
            dto.setStoreName(request.getParameter("storeName"));
            dto.setWgsX(Double.parseDouble(request.getParameter("wgsX")));
            dto.setWgsY(Double.parseDouble(request.getParameter("wgsY")));
            dto.setTmX(Double.parseDouble(request.getParameter("tmX")));
            dto.setTmY(Double.parseDouble(request.getParameter("tmY")));
            dto.setUrl(request.getParameter("url"));
            dto.setUnicodeName(request.getParameter("unicode"));

            conn = DBUtil.getConnection();
            MarkerDAO dao = new MarkerDAO(conn);
            dao.insertMarker(dto);

            response.sendRedirect("manageStorePage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
