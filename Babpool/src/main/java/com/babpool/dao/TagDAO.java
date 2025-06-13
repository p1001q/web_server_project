package com.babpool.dao;

import com.babpool.dto.TagDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    private Connection conn;

    public TagDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. 태그 추가
    public boolean insertTag(String name) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO tag (name) VALUES (?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("태그 삽입 실패: " + ex.getMessage());
            LogFileFilter.getWriter().println("[TagDAO] insertMapping() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] insertMapping() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 2. 전체 태그 조회
    public List<TagDTO> getAllTags() {
        List<TagDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM tag ORDER BY tag_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                TagDTO tag = new TagDTO();
                tag.setTagId(rs.getInt("tag_id"));
                tag.setName(rs.getString("name"));
                list.add(tag);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[TagDAO] getAllTags() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] getAllTags() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // 3. 단일 태그 조회 (tag_id로)
    public TagDTO getTagById(int tagId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        TagDTO tag = null;

        try {
            String sql = "SELECT * FROM tag WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                tag = new TagDTO();
                tag.setTagId(rs.getInt("tag_id"));
                tag.setName(rs.getString("name"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[TagDAO] getTagById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] getTagById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return tag;
    }

    // 4. 태그 수정
    public boolean updateTag(int tagId, String newName) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE tag SET name = ? WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setInt(2, tagId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[TagDAO] updateTag() UPDATE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] updateTag() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 5. 태그 삭제
    public boolean deleteTag(int tagId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM tag WHERE tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[TagDAO] deleteTag() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] deleteTag() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 수연 📌 특정 음식점에 연결된 태그 리스트 조회 (placeDetail 페이지에서 사용)
    public List<TagDTO> getTagsByStoreId(int storeId) {
        List<TagDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT t.tag_id, t.name FROM tag t JOIN store_tag_map m ON t.tag_id = m.tag_id WHERE m.store_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                TagDTO tag = new TagDTO();
                tag.setTagId(rs.getInt("tag_id"));
                tag.setName(rs.getString("name"));
                list.add(tag);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[TagDAO] getTagsByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[TagDAO] getTagsByStoreId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }
}