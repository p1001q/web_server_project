package com.babpool.dao;

import com.babpool.dto.ReviewDTO;
import com.babpool.filter.LogFileFilter;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewDAO {

    private Connection conn;

    public ReviewDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ 리뷰 등록 (회원 리뷰 작성)
    public boolean insertReview(ReviewDTO review) {
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            String sql = "INSERT INTO review (user_id, store_id, rating, content) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getStoreId());
            pstmt.setDouble(3, review.getRating());
            pstmt.setString(4, review.getContent());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return false;

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setReviewId(generatedKeys.getInt(1));  // <-- 🔑 여기가 핵심
            }
            return true;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] insertReview() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());
            return false;
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] insertReview() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }
    }


    // ✅ 리뷰 단건 조회 (JOIN 포함) -- 수연 추가
    public ReviewDTO getReviewById(int reviewId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ReviewDTO review = null;

        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path
                FROM review r
                JOIN user u ON r.user_id = u.user_id
                WHERE r.review_id = ?
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                review = mapReviewDTO(rs);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getReviewById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getReviewById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return review;
    }

    // ✅ 특정 음식점의 전체 리뷰 조회 (정렬 옵션 포함) -- 수연 추가
//    public List<ReviewDTO> getReviewsByStoreSorted(int storeId, String sortType) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        List<ReviewDTO> list = new ArrayList<>();
//        try {
//            String sql = """
//                SELECT r.*, u.nickname, u.profile_image_path 
//                FROM review r 
//                JOIN user u ON r.user_id = u.user_id 
//                WHERE r.store_id = ?
//            """;
//            if ("recent".equals(sortType)) {
//                sql += " ORDER BY r.created_at DESC";
//            } else if ("high".equals(sortType)) {
//                sql += " ORDER BY r.rating DESC";
//            } else if ("low".equals(sortType)) {
//                sql += " ORDER BY r.rating ASC";
//            }
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, storeId);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                list.add(mapReviewDTO(rs));
//            }
//        } catch (SQLException ex) {
//            LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByStoreSorted() READ-Error");
//            ex.printStackTrace(LogFileFilter.getWriter());
//
//        } finally {
//            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
//            catch (SQLException ex) {
//                LogFileFilter.getWriter().println("[ReviewDAO] 자원 정리 오류");
//                ex.printStackTrace(LogFileFilter.getWriter());
//
//            }
//        }
//        return list;
//    }

 // ✅ 특정 음식점의 전체 리뷰 조회 (정렬 옵션 포함) -- 수연 수정 안정화 버전 NULLPOINTEREXC 해결
    public List<ReviewDTO> getReviewsByStoreSorted(int storeId, String sortType) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();
        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path 
                FROM review r 
                JOIN user u ON r.user_id = u.user_id 
                WHERE r.store_id = ?
            """;
            if ("recent".equals(sortType)) {
                sql += " ORDER BY r.created_at DESC";
            } else if ("high".equals(sortType)) {
                sql += " ORDER BY r.rating DESC";
            } else if ("low".equals(sortType)) {
                sql += " ORDER BY r.rating ASC";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapReviewDTO(rs));
            }
        } catch (SQLException ex) {
            // ✅ NPE 방어 코드
            PrintWriter writer = LogFileFilter.getWriter();
            if (writer != null) {
                writer.println("[ReviewDAO] getReviewsByStoreSorted() READ-Error");
                ex.printStackTrace(writer);
            } else {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                PrintWriter writer = LogFileFilter.getWriter();
                if (writer != null) {
                    writer.println("[ReviewDAO] 자원 정리 오류");
                    ex.printStackTrace(writer);
                } else {
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }

    
    
    // ✅ 특정 사용자의 리뷰 조회 (마이페이지 등에서 활용 가능) -- 수연 추가
    public List<ReviewDTO> getReviewsByUserId(int userId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();
        try {
            String sql = """
                SELECT r.*, u.nickname, u.profile_image_path 
                FROM review r 
                JOIN user u ON r.user_id = u.user_id 
                WHERE r.user_id = ?
                ORDER BY r.created_at DESC
            """;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(mapReviewDTO(rs));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getReviewsByUserId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }

    // ✅ 리뷰 수정
    public boolean updateReview(ReviewDTO review) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE review SET rating = ?, content = ? WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, review.getRating());
            pstmt.setString(2, review.getContent());
            pstmt.setInt(3, review.getReviewId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] updateReview() UPDATE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] updateReview() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // ✅ 리뷰 삭제
    public boolean deleteReview(int reviewId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM review WHERE review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] deleteReview() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());
            return false;
        } finally {
            try {
            	if (pstmt != null) pstmt.close(); 
            }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] deleteReview() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }
    }

    // 수연 ✅ 리뷰 평점 통계 (별점바 그래프용)
    public Map<Integer, Integer> getScoreSummaryByStore(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, Integer> result = new HashMap<>();
        try {
            for (int i = 1; i <= 5; i++) result.put(i, 0);
            String sql = "SELECT FLOOR(rating) AS rating, COUNT(*) AS count FROM review WHERE store_id = ? GROUP BY FLOOR(rating)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("rating"), rs.getInt("count"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getScoreSummaryByStore() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getScoreSummaryByStore() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }
        return result;
    }

    // 수연 ✅ ResultSet → ReviewDTO 공통 매핑 함수 (중복 제거)
    private ReviewDTO mapReviewDTO(ResultSet rs) throws SQLException {
        ReviewDTO review = new ReviewDTO();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setStoreId(rs.getInt("store_id"));
        review.setRating(rs.getDouble("rating"));
        review.setContent(rs.getString("content"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        review.setNickname(rs.getString("nickname"));
        review.setProfileImagePath(rs.getString("profile_image_path"));
        return review;
    }
    
    // 동국 - 관리자 리뷰 관리 페이지 추가 
    public List<ReviewDTO> getAllReviews() {
        List<ReviewDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT r.*, u.nickname, u.profile_image_path FROM review r JOIN user u ON r.user_id = u.user_id ORDER BY r.review_id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapReviewDTO(rs));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[ReviewDAO] getAllReviews() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());
        } finally {
            try { if (rs != null) rs.close(); if (pstmt != null) pstmt.close(); }
            catch (SQLException ex) {
                LogFileFilter.getWriter().println("[ReviewDAO] getAllReviews() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());
            }
        }
        return list;
    }
}