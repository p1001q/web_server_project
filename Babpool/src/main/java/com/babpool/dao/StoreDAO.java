package com.babpool.dao;

import com.babpool.dto.StoreDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreDAO {

    private Connection conn;

    public StoreDAO(Connection conn) {
        this.conn = conn;
    }

    // Create
    public boolean insertStore(StoreDTO store) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO store (name, address, phone, open_time, rating_avg, review_count, like_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, store.getName());
            pstmt.setString(2, store.getAddress());
            pstmt.setString(3, store.getPhone());
            pstmt.setString(4, store.getOpenTime());
            pstmt.setFloat(5, store.getRatingAvg());
            pstmt.setInt(6, store.getReviewCount());
            pstmt.setInt(7, store.getLikeCount());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreDAO] insertStore() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreDAO] insertStore() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }
    }

    // Read - 단일 조회
    public StoreDTO getStoreById(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StoreDTO store = null;

        try {
            String sql = "SELECT * FROM store WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                store = new StoreDTO();
                store.setStoreId(rs.getInt("store_id"));
                store.setName(rs.getString("name"));
                store.setAddress(rs.getString("address"));
                store.setPhone(rs.getString("phone"));
                store.setOpenTime(rs.getString("open_time"));
                store.setRatingAvg(rs.getFloat("rating_avg"));
                store.setReviewCount(rs.getInt("review_count"));
                store.setLikeCount(rs.getInt("like_count"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreDAO] getStoreById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreDAO] getStoreById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return store;
    }

    // Read - 전체 음식점 리스트
    public List<StoreDTO> getAllStores() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StoreDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM store ORDER BY store_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                StoreDTO store = new StoreDTO();
                store.setStoreId(rs.getInt("store_id"));
                store.setName(rs.getString("name"));
                store.setAddress(rs.getString("address"));
                store.setPhone(rs.getString("phone"));
                store.setOpenTime(rs.getString("open_time"));
                store.setRatingAvg(rs.getFloat("rating_avg"));
                store.setReviewCount(rs.getInt("review_count"));
                store.setLikeCount(rs.getInt("like_count"));
                list.add(store);
            }

        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreDAO] getAllStores() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreDAO] getAllStores() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // Update
    public boolean updateStore(StoreDTO store) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE store SET name = ?, address = ?, phone = ?, open_time = ?, rating_avg = ?, review_count = ?, like_count = ? WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, store.getName());
            pstmt.setString(2, store.getAddress());
            pstmt.setString(3, store.getPhone());
            pstmt.setString(4, store.getOpenTime());
            pstmt.setFloat(5, store.getRatingAvg());
            pstmt.setInt(6, store.getReviewCount());
            pstmt.setInt(7, store.getLikeCount());
            pstmt.setInt(8, store.getStoreId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreDAO] updateStore() UPDATE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreDAO] updateStore() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Delete
    public boolean deleteStore(int storeId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM store WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[StoreDAO] deleteStore() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[StoreDAO] deleteStore() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }
    
  //수연 ✅ likeCount 기준 Top3 조회 (category 이름까지 포함)
    public List<Map<String, Object>> getTop3StoresWithCategory() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT s.store_id, s.name, s.rating_avg, s.review_count, s.like_count, c.name AS category_name
            FROM store s
            JOIN store_category_map scm ON s.store_id = scm.store_id
            JOIN category c ON scm.category_id = c.category_id
            ORDER BY s.like_count DESC
            LIMIT 3
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("storeId", rs.getInt("store_id"));
                row.put("name", rs.getString("name"));
                row.put("ratingAvg", rs.getFloat("rating_avg"));
                row.put("reviewCount", rs.getInt("review_count"));
                row.put("likeCount", rs.getInt("like_count"));
                row.put("categoryName", rs.getString("category_name"));
                list.add(row);
            }
        } catch (SQLException e) {
            LogFileFilter.getWriter().println("getTop3StoresWithCategory 에러 : " + e.getMessage());
        }
        return list;
    }
    
   
}