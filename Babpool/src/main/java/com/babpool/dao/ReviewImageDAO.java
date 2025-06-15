package com.babpool.dao;

import com.babpool.dto.ReviewImageDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewImageDAO {

    private Connection conn;

    public ReviewImageDAO(Connection conn) {
        this.conn = conn;
    }

    // Create
    public boolean insertReviewImage(ReviewImageDTO image) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO review_image (review_id, image_path, image_order) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, image.getReviewId());
            pstmt.setString(2, image.getImagePath());
            pstmt.setInt(3, image.getImageOrder());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] insertReviewImage() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] insertReviewImage() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Read - 리뷰 ID로 전체 이미지 조회
    public List<ReviewImageDTO> getImagesByReviewId(int reviewId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewImageDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM review_image WHERE review_id = ? ORDER BY image_order ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ReviewImageDTO img = new ReviewImageDTO();
                img.setImageId(rs.getInt("image_id"));
                img.setReviewId(rs.getInt("review_id"));
                img.setImagePath(rs.getString("image_path"));
                img.setImageOrder(rs.getInt("image_order"));
                list.add(img);
            }

        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] getImagesByReviewId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] getImagesByReviewId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }

        return list;
    }

    // Delete - 개별 이미지 삭제
    public boolean deleteImage(int imageId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review_image WHERE image_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, imageId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImage() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImage() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Delete - 리뷰 ID 기준 이미지 전부 삭제 (리뷰 삭제 시 같이 사용)
    public boolean deleteImagesByReviewId(int reviewId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review_image WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImagesByReviewId() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewImageDAO] deleteImagesByReviewId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }
    
    public List<String> getReviewImagesByStoreId(int storeId) {
        List<String> imagePaths = new ArrayList<>();
        String sql = "SELECT ri.image_path FROM review_image ri JOIN review r ON ri.review_id = r.review_id WHERE r.store_id = ? ORDER BY ri.image_id DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, storeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                imagePaths.add(rs.getString("image_path"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewImageDAO] getReviewImagesByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());
        }
        return imagePaths;
    }

    
}