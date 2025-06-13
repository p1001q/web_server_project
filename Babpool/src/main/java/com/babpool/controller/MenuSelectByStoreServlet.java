package com.babpool.controller;

import com.babpool.dao.MenuDAO;
import com.babpool.dto.MenuDTO;
import com.babpool.utils.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/MenuSelectByStoreServlet")
public class MenuSelectByStoreServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            int storeId = Integer.parseInt(request.getParameter("storeId"));
            conn = DBUtil.getConnection();
            MenuDAO dao = new MenuDAO(conn);
            List<MenuDTO> menuList = dao.getMenusByStoreId(storeId);
            request.setAttribute("menuList", menuList);
            request.getRequestDispatcher("manageStorePage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally { try { if (conn != null) conn.close(); } catch (Exception e) {} }
    }
}
