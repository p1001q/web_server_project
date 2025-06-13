package com.babpool.dao;

import com.babpool.dto.BookmarkDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDAO {

    private Connection conn;

    public BookmarkDAO(Connection conn) {
        this.conn = conn;
    }

    // Create - 즐겨찾기 추가
    public boolean addBookmark(int userId, int storeId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO bookmark (user_id, store_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, storeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[BookmarkDAO] addBookmark() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[BookmarkDAO] addBookmark() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Delete - 즐겨찾기 삭제
    public boolean removeBookmark(int userId, int storeId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM bookmark WHERE user_id = ? AND store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, storeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[BookmarkDAO] removeBookmark() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[BookmarkDAO] removeBookmark() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Read - 특정 사용자가 해당 음식점을 찜했는지 확인
    public boolean isBookmarked(int userId, int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            String sql = "SELECT 1 FROM bookmark WHERE user_id = ? AND store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, storeId);
            rs = pstmt.executeQuery();
            result = rs.next();
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[BookmarkDAO] isBookmarked() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[BookmarkDAO] isBookmarked() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return result;
    }

    // Read - 사용자 ID로 즐겨찾기한 음식점 목록 조회
    public List<BookmarkDTO> getBookmarkedStoresByUserId(int userId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BookmarkDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM bookmark WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookmarkDTO dto = new BookmarkDTO();
                dto.setUserId(rs.getInt("user_id"));
                dto.setStoreId(rs.getInt("store_id"));
                list.add(dto);
            }

        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[BookmarkDAO] getBookmarkedStoresByUserId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[BookmarkDAO] getBookmarkedStoresByUserId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // 수연📌 특정 음식점의 전체 즐겨찾기 개수 조회 (placeDetail 페이지에서 사용)
    public int getBookmarkCountByStoreId(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM bookmark WHERE store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[BookmarkDAO] getBookmarkCountByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[BookmarkDAO] getBookmarkCountByStoreId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return count;
    }
}