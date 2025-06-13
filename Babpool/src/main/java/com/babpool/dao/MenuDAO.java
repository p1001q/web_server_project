package com.babpool.dao;

import com.babpool.dto.MenuDTO;
import com.babpool.filter.LogFileFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    private Connection conn;

    public MenuDAO(Connection conn) {
        this.conn = conn;
    }

    // Create
    public boolean insertMenu(MenuDTO menu) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO menu (store_id, name, price) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menu.getStoreId());
            pstmt.setString(2, menu.getName());
            pstmt.setInt(3, menu.getPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MenuDAO] insertMenu() INSERT-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MenuDAO] insertMenu() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Read - 메뉴 ID로 조회
    public MenuDTO getMenuById(int menuId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MenuDTO menu = null;

        try {
            String sql = "SELECT * FROM menu WHERE menu_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menuId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                menu = new MenuDTO();
                menu.setMenuId(rs.getInt("menu_id"));
                menu.setStoreId(rs.getInt("store_id"));
                menu.setName(rs.getString("name"));
                menu.setPrice(rs.getInt("price"));
            }
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MenuDAO] getMenuById() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MenuDAO] getMenuById() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return menu;
    }

    // Read - 가게 ID로 메뉴 전체 조회
    public List<MenuDTO> getMenusByStoreId(int storeId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<MenuDTO> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM menu WHERE store_id = ? ORDER BY menu_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, storeId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MenuDTO menu = new MenuDTO();
                menu.setMenuId(rs.getInt("menu_id"));
                menu.setStoreId(rs.getInt("store_id"));
                menu.setName(rs.getString("name"));
                menu.setPrice(rs.getInt("price"));
                list.add(menu);
            }

        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MenuDAO] getMenusByStoreId() READ-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MenuDAO] getMenusByStoreId() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }

        return list;
    }

    // Update
    public boolean updateMenu(MenuDTO menu) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE menu SET name = ?, price = ? WHERE menu_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, menu.getName());
            pstmt.setInt(2, menu.getPrice());
            pstmt.setInt(3, menu.getMenuId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MenuDAO] updateMenu() UPDATE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());

            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MenuDAO] updateMenu() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }

    // Delete
    public boolean deleteMenu(int menuId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM menu WHERE menu_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, menuId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            LogFileFilter.getWriter().println("[MenuDAO] deleteMenu() DELETE-Error");
            ex.printStackTrace(LogFileFilter.getWriter());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                LogFileFilter.getWriter().println("[MenuDAO] deleteMenu() 자원 정리 오류");
                ex.printStackTrace(LogFileFilter.getWriter());

            }
        }
    }
}