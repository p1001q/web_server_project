package com.babpool.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/babpooldb?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
            String user = "root";         // ← 너가 쓰는 MySQL 계정명
            String password = ApiKeyUtil.get("mysqlPassword");  // ← 너의 root 비번 입력

            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}