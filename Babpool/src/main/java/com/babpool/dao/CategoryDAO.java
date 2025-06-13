package com.babpool.dao;

import com.babpool.dto.CategoryDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. 카테고리 추가
    public boolean insertCategory(String name) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO category (name) VALUES (?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] insertCategory() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] insertCategory() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 2. 전체 카테고리 조회
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM category ORDER BY category_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CategoryDTO dto = new CategoryDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setName(rs.getString("name"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] getAllCategories() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] getAllCategories() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // 3. 단일 카테고리 조회
    public CategoryDTO getCategoryById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CategoryDTO dto = null;

        try {
            String sql = "SELECT * FROM category WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new CategoryDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setName(rs.getString("name"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] getCategoryById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] getCategoryById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return dto;
    }

    // 4. 카테고리 이름 수정
    public boolean updateCategory(int id, String newName) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE category SET name = ? WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] updateCategory() UPDATE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] updateCategory() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 5. 카테고리 삭제
    public boolean deleteCategory(int id) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM category WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] deleteCategory() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] deleteCategory() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 수연 ⭐ 매핑된 카테고리 조회 (store_category_map 조인)
    public List<CategoryDTO> getCategoriesByStoreId(int storeId) {
        List<CategoryDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = """
                SELECT c.category_id, c.name
                FROM store_category_map scm
                JOIN category c ON scm.category_id = c.category_id
                WHERE scm.store_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CategoryDTO dto = new CategoryDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setName(rs.getString("name"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[CategoryDAO] getCategoriesByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[CategoryDAO] getCategoriesByStoreId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }
}