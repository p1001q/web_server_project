package com.babpool.dao;

import com.babpool.dto.MarkerCategoryMapDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarkerCategoryDAO {

    private Connection conn;

    public MarkerCategoryDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. 마커-카테고리 매핑 추가
    public boolean insertMarkerCategory(int markerId, int categoryId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO marker_category_map (marker_id, category_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            pstmt.setInt(2, categoryId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerCategoryDAO] insertMarkerCategory() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerCategoryDAO] insertMarkerCategory() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 2. 마커 ID로 카테고리들 조회
    public List<MarkerCategoryMapDTO> getCategoriesByMarkerId(int markerId) {
        List<MarkerCategoryMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM marker_category_map WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MarkerCategoryMapDTO dto = new MarkerCategoryMapDTO();
                dto.setMarkerId(rs.getInt("marker_id"));
                dto.setCategoryId(rs.getInt("category_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerCategoryDAO] getCategoriesByMarkerId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());


        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerCategoryDAO] getCategoriesByMarkerId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // ✅ 3. 카테고리 ID로 마커들 조회
    public List<Integer> getMarkerIdsByCategoryId(int categoryId) {
        List<Integer> markerIds = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT marker_id FROM marker_category_map WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                markerIds.add(rs.getInt("marker_id"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerCategoryDAO] getMarkerIdsByCategoryId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerCategoryDAO] getMarkerIdsByCategoryId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return markerIds;
    }

    // 4. 삭제
    public boolean deleteByMarkerId(int markerId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM marker_category_map WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerCategoryDAO] deleteByMarkerId() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerCategoryDAO] deleteByMarkerId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }
}