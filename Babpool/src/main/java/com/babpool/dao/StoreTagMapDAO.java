package com.babpool.dao;

import com.babpool.dto.StoreTagMapDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreTagMapDAO {
    private Connection conn;

    public StoreTagMapDAO(Connection conn) {
        this.conn = conn;
    }

    // INSERT
    public boolean insertMapping(int storeId, int tagId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO store_tag_map (store_id, tag_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, tagId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] insertMapping() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // SELECT by store_id
    public List<StoreTagMapDTO> getTagsByStoreId(int storeId) {
        List<StoreTagMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_tag_map WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreTagMapDTO dto = new StoreTagMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setTagId(rs.getInt("tag_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] getTagsByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }

    // DELETE
    public boolean deleteMapping(int storeId, int tagId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM store_tag_map WHERE store_id = ? AND tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            pstmt.setInt(2, tagId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] deleteMapping() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }
    
    public List<StoreTagMapDTO> getStoresByTagId(int tagId) {
        List<StoreTagMapDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM store_tag_map WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreTagMapDTO dto = new StoreTagMapDTO();
                dto.setStoreId(rs.getInt("store_id"));
                dto.setTagId(rs.getInt("tag_id"));
                list.add(dto);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreTagMapDAO] getStoresByTagId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreTagMapDAO] 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }

}