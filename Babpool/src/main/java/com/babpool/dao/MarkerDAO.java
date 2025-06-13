////package com.babpool.dao;
////
////import com.babpool.dto.MarkerDTO;
////
////import java.sql.*;
////import java.util.ArrayList;
////import java.util.List;
////
////public class MarkerDAO {
////
////    private Connection conn;
////
////    public MarkerDAO(Connection conn) {
////        this.conn = conn;
////    }
////
////    // 1. 마커 등록
////    public boolean insertMarker(MarkerDTO marker) {
////        PreparedStatement pstmt = null;
////        try {
////            String sql = "INSERT INTO marker (store_id, wgs_x, wgs_y, tm_x, tm_y, url, unicode_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
////            pstmt = conn.prepareStatement(sql);
////            pstmt.setInt(1, marker.getStoreId());
////            pstmt.setDouble(2, marker.getWgsX());
////            pstmt.setDouble(3, marker.getWgsY());
////            pstmt.setDouble(4, marker.getTmX());
////            pstmt.setDouble(5, marker.getTmY());
////            pstmt.setString(6, marker.getUrl());
////            pstmt.setString(7, marker.getUnicodeName());
////
////            return pstmt.executeUpdate() > 0;
////        } catch (SQLException ex) {
////            System.out.println("마커 삽입 실패: " + ex.getMessage());
////            return false;
////        } finally {
////            try {
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////    }
////
////    // 2. 마커 단일 조회
////    public MarkerDTO getMarkerById(int markerId) {
////        PreparedStatement pstmt = null;
////        ResultSet rs = null;
////        MarkerDTO marker = null;
////
////        try {
////            String sql = "SELECT * FROM marker WHERE marker_id = ?";
////            pstmt = conn.prepareStatement(sql);
////            pstmt.setInt(1, markerId);
////            rs = pstmt.executeQuery();
////
////            if (rs.next()) {
////                marker = new MarkerDTO();
////                marker.setMarkerId(rs.getInt("marker_id"));
////                marker.setStoreId(rs.getInt("store_id"));
////                marker.setWgsX(rs.getDouble("wgs_x"));
////                marker.setWgsY(rs.getDouble("wgs_y"));
////                marker.setTmX(rs.getDouble("tm_x"));
////                marker.setTmY(rs.getDouble("tm_y"));
////                marker.setUrl(rs.getString("url"));
////                marker.setUnicodeName(rs.getString("unicode"));
////            }
////        } catch (SQLException ex) {
////            System.out.println("마커 조회 실패: " + ex.getMessage());
////        } finally {
////            try {
////                if (rs != null) rs.close();
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////
////        return marker;
////    }
////
////    // 3. 전체 마커 목록 조회
////    public List<MarkerDTO> getAllMarkers() {
////        List<MarkerDTO> list = new ArrayList<>();
////        PreparedStatement pstmt = null;
////        ResultSet rs = null;
////
////        try {
////            String sql = "SELECT * FROM marker ORDER BY marker_id";
////            pstmt = conn.prepareStatement(sql);
////            rs = pstmt.executeQuery();
////
////            while (rs.next()) {
////                MarkerDTO marker = new MarkerDTO();
////                marker.setMarkerId(rs.getInt("marker_id"));
////                marker.setStoreId(rs.getInt("store_id"));
////                marker.setWgsX(rs.getDouble("wgs_x"));
////                marker.setWgsY(rs.getDouble("wgs_y"));
////                marker.setTmX(rs.getDouble("tm_x"));
////                marker.setTmY(rs.getDouble("tm_y"));
////                marker.setUrl(rs.getString("url"));
////                marker.setUnicodeName(rs.getString("unicode"));
////                list.add(marker);
////            }
////        } catch (SQLException ex) {
////            System.out.println("전체 마커 조회 실패: " + ex.getMessage());
////        } finally {
////            try {
////                if (rs != null) rs.close();
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////
////        return list;
////    }
////
////    // 4. 마커 삭제
////    public boolean deleteMarker(int markerId) {
////        PreparedStatement pstmt = null;
////        try {
////            String sql = "DELETE FROM marker WHERE marker_id = ?";
////            pstmt = conn.prepareStatement(sql);
////            pstmt.setInt(1, markerId);
////            return pstmt.executeUpdate() > 0;
////        } catch (SQLException ex) {
////            System.out.println("마커 삭제 실패: " + ex.getMessage());
////            return false;
////        } finally {
////            try {
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////    }
////    
//// // ✅ categoryId로 마커 조회
////    public List<MarkerDTO> getMarkersByCategoryId(int categoryId) {
////        List<MarkerDTO> list = new ArrayList<>();
////        PreparedStatement pstmt = null;
////        ResultSet rs = null;
////
////        try {
////            String sql = "SELECT m.* FROM marker m " +
////                         "JOIN marker_category_map mc ON m.marker_id = mc.marker_id " +
////                         "WHERE mc.category_id = ?";
////            pstmt = conn.prepareStatement(sql);
////            pstmt.setInt(1, categoryId);
////            rs = pstmt.executeQuery();
////
////            while (rs.next()) {
////                MarkerDTO marker = new MarkerDTO();
////                marker.setMarkerId(rs.getInt("marker_id"));
////                marker.setStoreId(rs.getInt("store_id"));
////                marker.setWgsX(rs.getDouble("wgs_x"));
////                marker.setWgsY(rs.getDouble("wgs_y"));
////                marker.setTmX(rs.getDouble("tm_x"));
////                marker.setTmY(rs.getDouble("tm_y"));
////                marker.setUrl(rs.getString("url"));
////                marker.setUnicodeName(rs.getString("unicode_name"));
////                list.add(marker);
////            }
////        } catch (SQLException ex) {
////            System.out.println("카테고리별 마커 조회 실패: " + ex.getMessage());
////        } finally {
////            try {
////                if (rs != null) rs.close();
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////
////        return list;
////    }
////    
//// // ✅ tagId로 마커 조회
////    public List<MarkerDTO> getMarkersByTagId(int tagId) {
////        List<MarkerDTO> list = new ArrayList<>();
////        PreparedStatement pstmt = null;
////        ResultSet rs = null;
////
////        try {
////            String sql = "SELECT m.* FROM marker m " +
////                         "JOIN marker_tag_map mt ON m.marker_id = mt.marker_id " +
////                         "WHERE mt.tag_id = ?";
////            pstmt = conn.prepareStatement(sql);
////            pstmt.setInt(1, tagId);
////            rs = pstmt.executeQuery();
////
////            while (rs.next()) {
////                MarkerDTO marker = new MarkerDTO();
////                marker.setMarkerId(rs.getInt("marker_id"));
////                marker.setStoreId(rs.getInt("store_id"));
////                marker.setWgsX(rs.getDouble("wgs_x"));
////                marker.setWgsY(rs.getDouble("wgs_y"));
////                marker.setTmX(rs.getDouble("tm_x"));
////                marker.setTmY(rs.getDouble("tm_y"));
////                marker.setUrl(rs.getString("url"));
////                marker.setUnicodeName(rs.getString("unicode"));
////                list.add(marker);
////            }
////        } catch (SQLException ex) {
////            System.out.println("태그별 마커 조회 실패: " + ex.getMessage());
////        } finally {
////            try {
////                if (rs != null) rs.close();
////                if (pstmt != null) pstmt.close();
////            } catch (SQLException ex) {
////                ex.printStackTrace();
////            }
////        }
////
////        return list;
////    }
////
////}
//
//package com.babpool.dao;
//
//import com.babpool.dto.MarkerDTO;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MarkerDAO {
//
//    private Connection conn;
//
//    public MarkerDAO(Connection conn) {
//        this.conn = conn;
//    }
//
//    // 1. 마커 등록
//    public boolean insertMarker(MarkerDTO marker) {
//        PreparedStatement pstmt = null;
//        try {
//            String sql = "INSERT INTO marker (store_id, wgs_x, wgs_y, tm_x, tm_y, url, unicode_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, marker.getStoreId());
//            pstmt.setDouble(2, marker.getWgsX());
//            pstmt.setDouble(3, marker.getWgsY());
//            pstmt.setDouble(4, marker.getTmX());
//            pstmt.setDouble(5, marker.getTmY());
//            pstmt.setString(6, marker.getUrl());
//            pstmt.setString(7, marker.getUnicodeName());
//
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException ex) {
//            System.out.println("마커 삽입 실패: " + ex.getMessage());
//            return false;
//        } finally {
//            try {
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    // 2. 마커 단일 조회
//    public MarkerDTO getMarkerById(int markerId) {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        MarkerDTO marker = null;
//
//        try {
//            String sql = "SELECT * FROM marker WHERE marker_id = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, markerId);
//            rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                marker = new MarkerDTO();
//                marker.setMarkerId(rs.getInt("marker_id"));
//                marker.setStoreId(rs.getInt("store_id"));
//                marker.setWgsX(rs.getDouble("wgs_x"));
//                marker.setWgsY(rs.getDouble("wgs_y"));
//                marker.setTmX(rs.getDouble("tm_x"));
//                marker.setTmY(rs.getDouble("tm_y"));
//                marker.setUrl(rs.getString("url"));
//                marker.setUnicodeName(rs.getString("unicode_name"));  // ✅ 수정됨
//            }
//        } catch (SQLException ex) {
//            System.out.println("마커 조회 실패: " + ex.getMessage());
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return marker;
//    }
//
//    // 3. 전체 마커 목록 조회
//    public List<MarkerDTO> getAllMarkers() {
//        List<MarkerDTO> list = new ArrayList<>();
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            String sql = "SELECT * FROM marker ORDER BY marker_id";
//            pstmt = conn.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                MarkerDTO marker = new MarkerDTO();
//                marker.setMarkerId(rs.getInt("marker_id"));
//                marker.setStoreId(rs.getInt("store_id"));
//                marker.setWgsX(rs.getDouble("wgs_x"));
//                marker.setWgsY(rs.getDouble("wgs_y"));
//                marker.setTmX(rs.getDouble("tm_x"));
//                marker.setTmY(rs.getDouble("tm_y"));
//                marker.setUrl(rs.getString("url"));
//                marker.setUnicodeName(rs.getString("unicode_name"));  // ✅ 수정됨
//                list.add(marker);
//            }
//        } catch (SQLException ex) {
//            System.out.println("전체 마커 조회 실패: " + ex.getMessage());
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return list;
//    }
//
//    // 4. 마커 삭제
//    public boolean deleteMarker(int markerId) {
//        PreparedStatement pstmt = null;
//        try {
//            String sql = "DELETE FROM marker WHERE marker_id = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, markerId);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException ex) {
//            System.out.println("마커 삭제 실패: " + ex.getMessage());
//            return false;
//        } finally {
//            try {
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    // ✅ categoryId로 마커 조회
//    public List<MarkerDTO> getMarkersByCategoryId(int categoryId) {
//        List<MarkerDTO> list = new ArrayList<>();
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            String sql = "SELECT m.* FROM marker m " +
//                         "JOIN marker_category_map mc ON m.marker_id = mc.marker_id " +
//                         "WHERE mc.category_id = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, categoryId);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                MarkerDTO marker = new MarkerDTO();
//                marker.setMarkerId(rs.getInt("marker_id"));
//                marker.setStoreId(rs.getInt("store_id"));
//                marker.setWgsX(rs.getDouble("wgs_x"));
//                marker.setWgsY(rs.getDouble("wgs_y"));
//                marker.setTmX(rs.getDouble("tm_x"));
//                marker.setTmY(rs.getDouble("tm_y"));
//                marker.setUrl(rs.getString("url"));
//                marker.setUnicodeName(rs.getString("unicode_name"));  // ✅ 이 부분은 원래 잘 되어 있음
//                list.add(marker);
//            }
//        } catch (SQLException ex) {
//            System.out.println("카테고리별 마커 조회 실패: " + ex.getMessage());
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return list;
//    }
//
//    // ✅ tagId로 마커 조회
//    public List<MarkerDTO> getMarkersByTagId(int tagId) {
//        List<MarkerDTO> list = new ArrayList<>();
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            String sql = "SELECT m.* FROM marker m " +
//                         "JOIN marker_tag_map mt ON m.marker_id = mt.marker_id " +
//                         "WHERE mt.tag_id = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, tagId);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                MarkerDTO marker = new MarkerDTO();
//                marker.setMarkerId(rs.getInt("marker_id"));
//                marker.setStoreId(rs.getInt("store_id"));
//                marker.setWgsX(rs.getDouble("wgs_x"));
//                marker.setWgsY(rs.getDouble("wgs_y"));
//                marker.setTmX(rs.getDouble("tm_x"));
//                marker.setTmY(rs.getDouble("tm_y"));
//                marker.setUrl(rs.getString("url"));
//                marker.setUnicodeName(rs.getString("unicode_name"));  // ✅ 수정됨
//                list.add(marker);
//            }
//        } catch (SQLException ex) {
//            System.out.println("태그별 마커 조회 실패: " + ex.getMessage());
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return list;
//    }
//}


package com.babpool.dao;

import com.babpool.dto.MarkerDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarkerDAO {

    private Connection conn;

    public MarkerDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. 마커 등록
    public boolean insertMarker(MarkerDTO marker) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO marker (store_id, store_name, wgs_x, wgs_y, tm_x, tm_y, url, unicode_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, marker.getStoreId());
            pstmt.setString(2, marker.getStoreName());
            pstmt.setDouble(3, marker.getWgsX());
            pstmt.setDouble(4, marker.getWgsY());
            pstmt.setDouble(5, marker.getTmX());
            pstmt.setDouble(6, marker.getTmY());
            pstmt.setString(7, marker.getUrl());
            pstmt.setString(8, marker.getUnicodeName());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] insertMarker() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] insertMarker() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // 2. 마커 단일 조회
    public MarkerDTO getMarkerById(int markerId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MarkerDTO marker = null;

        try {
            String sql = "SELECT * FROM marker WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                marker = new MarkerDTO();
                marker.setMarkerId(rs.getInt("marker_id"));
                marker.setStoreId(rs.getInt("store_id"));
                marker.setStoreName(rs.getString("store_name"));  // ← 이 줄 추가
                marker.setWgsX(rs.getDouble("wgs_x"));
                marker.setWgsY(rs.getDouble("wgs_y"));
                marker.setTmX(rs.getDouble("tm_x"));
                marker.setTmY(rs.getDouble("tm_y"));
                marker.setUrl(rs.getString("url"));
                marker.setUnicodeName(rs.getString("unicode_name"));
                marker.setCreatedAt(rs.getTimestamp("create_at"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] getMarkerById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] getMarkerById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return marker;
    }

    // 3. 전체 마커 목록 조회
    public List<MarkerDTO> getAllMarkers() {
        List<MarkerDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM marker ORDER BY marker_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MarkerDTO marker = new MarkerDTO();
                marker.setMarkerId(rs.getInt("marker_id"));
                marker.setStoreId(rs.getInt("store_id"));
                marker.setStoreName(rs.getString("store_name"));  // ← 이 줄 추가
                marker.setWgsX(rs.getDouble("wgs_x"));
                marker.setWgsY(rs.getDouble("wgs_y"));
                marker.setTmX(rs.getDouble("tm_x"));
                marker.setTmY(rs.getDouble("tm_y"));
                marker.setUrl(rs.getString("url"));
                marker.setUnicodeName(rs.getString("unicode_name"));
                marker.setCreatedAt(rs.getTimestamp("create_at"));
                list.add(marker);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] getAllMarkers() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] getAllMarkers() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }

    // 4. 마커 삭제
    public boolean deleteMarker(int markerId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM marker WHERE marker_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, markerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] deleteMarker() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] deleteMarker() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // ✅ categoryId로 마커 조회
    public List<MarkerDTO> getMarkersByCategoryId(int categoryId) {
        List<MarkerDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT m.* FROM marker m JOIN marker_category_map mc ON m.marker_id = mc.marker_id WHERE mc.category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MarkerDTO marker = new MarkerDTO();
                marker.setMarkerId(rs.getInt("marker_id"));
                marker.setStoreId(rs.getInt("store_id"));
                marker.setStoreName(rs.getString("store_name"));  // ← 이 줄 추가
                marker.setWgsX(rs.getDouble("wgs_x"));
                marker.setWgsY(rs.getDouble("wgs_y"));
                marker.setTmX(rs.getDouble("tm_x"));
                marker.setTmY(rs.getDouble("tm_y"));
                marker.setUrl(rs.getString("url"));
                marker.setUnicodeName(rs.getString("unicode_name"));
                marker.setCreatedAt(rs.getTimestamp("create_at"));
                list.add(marker);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] getMarkersByCategoryId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] getMarkersByCategoryId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }

    // ✅ tagId로 마커 조회
    public List<MarkerDTO> getMarkersByTagId(int tagId) {
        List<MarkerDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT m.* FROM marker m JOIN marker_tag_map mt ON m.marker_id = mt.marker_id WHERE mt.tag_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tagId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MarkerDTO marker = new MarkerDTO();
                marker.setMarkerId(rs.getInt("marker_id"));
                marker.setStoreId(rs.getInt("store_id"));
                marker.setStoreName(rs.getString("store_name"));  // ← 이 줄 추가
                marker.setWgsX(rs.getDouble("wgs_x"));
                marker.setWgsY(rs.getDouble("wgs_y"));
                marker.setTmX(rs.getDouble("tm_x"));
                marker.setTmY(rs.getDouble("tm_y"));
                marker.setUrl(rs.getString("url"));
                marker.setUnicodeName(rs.getString("unicode_name"));
                marker.setCreatedAt(rs.getTimestamp("create_at"));
                list.add(marker);
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MarkerDAO] getMarkersByTagId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MarkerDAO] getMarkersByTagId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
        return list;
    }
}