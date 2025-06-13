/**
 * *


package com.babpool.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.babpool.dao.*;
import com.babpool.dto.*;
import com.babpool.filter.LogFileFilter;
import com.babpool.utils.DBUtil;

@WebServlet("/mainPage")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            CategoryDAO categoryDAO = new CategoryDAO(conn);
            TagDAO tagDAO = new TagDAO(conn);
            MarkerDAO markerDAO = new MarkerDAO(conn);
            MarkerCategoryDAO markerCategoryDAO = new MarkerCategoryDAO(conn);
            MarkerTagDAO markerTagDAO = new MarkerTagDAO(conn);
            StoreDAO storeDAO = new StoreDAO(conn);

            List<CategoryDTO> categoryList = categoryDAO.getAllCategories();
            List<TagDTO> tagList = tagDAO.getAllTags();
            List<MarkerDTO> markerList = markerDAO.getAllMarkers();
            List<Map<String, Object>> top3List = storeDAO.getTop3StoresWithCategory();

            request.setAttribute("categoryList", categoryList);
            request.setAttribute("tagList", tagList);
            request.setAttribute("markerList", markerList);
            request.setAttribute("top3List", top3List);

            request.getRequestDispatcher("/mainPage.jsp").forward(request, response);

        } catch (Exception e) {
            LogFileFilter.getWriter().println("MainPageServlet 에러 : " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }
}

*/
