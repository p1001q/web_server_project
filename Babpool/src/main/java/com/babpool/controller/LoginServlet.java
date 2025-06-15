package com.babpool.controller;

import com.babpool.dao.UserDAO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;
import com.babpool.utils.SHA256Util;
import com.babpool.filter.LogFileFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

//@WebServlet("/LoginServlet")
//public class LoginServlet extends HttpServlet {
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        // 1. 한글 인코딩
//        request.setCharacterEncoding("UTF-8");
//
//        // 2. 입력값 받기
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//
//        // 3. SHA-256 해싱
//        String hashedPassword = SHA256Util.encrypt(password);
//
//        Connection conn = null;
//        try {
//            // 4. DB 연결
//            conn = DBUtil.getConnection();
//            UserDAO userDAO = new UserDAO(conn);
//            UserDTO user = userDAO.getUserByEmail(email);
//
//            if (user != null && user.getPassword().equals(hashedPassword)) {
//                // 5. 로그인 성공 → 세션에 저장
//                HttpSession session = request.getSession();
//                session.setAttribute("loginUser", user);
//                response.sendRedirect("mainPage.jsp"); // 로그인 성공 시 메인으로
//            } else {
//                // 6. 로그인 실패
//                request.setAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
//                request.getRequestDispatcher("login.jsp").forward(request, response);
//            }
//
//        } catch (Exception e) {
//            LogFileFilter.getWriter().println("[LoginServlet] 로그인 처리 오류");
//            e.printStackTrace(LogFileFilter.getWriter());
//            response.sendRedirect("error.jsp");
//        } finally {
//            try {
//                if (conn != null) conn.close();
//            } catch (Exception e) {
//                LogFileFilter.getWriter().println("[LoginServlet] 자원 정리 오류");
//                e.printStackTrace(LogFileFilter.getWriter());
//            }
//        }
//    }
//}

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 한글 인코딩
        request.setCharacterEncoding("UTF-8");

        // 2. 입력값 받기 - 동국 06/14 입력부 수정
        String email = request.getParameter("email");
        String password = request.getParameter("password")
        		.replaceAll("[^\\p{Print}]", "")
        	    .trim();

        // 3. SHA-256 해싱
        String hashedPassword = SHA256Util.encrypt(password);

        // 로그인 처리
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();  // DB 연결
            UserDAO userDAO = new UserDAO(conn);
            UserDTO user = userDAO.getUserByEmail(email);  // 이메일로 사용자 조회

            // 로그인 로그 출력
            System.out.println("입력된 비밀번호: " + password);  // 사용자가 입력한 비밀번호
            System.out.println("해싱된 비밀번호: " + hashedPassword);  // 입력된 비밀번호의 해싱된 값

            if (user != null) {
                System.out.println("DB에서 가져온 비밀번호: " + user.getPassword());  // DB에서 가져온 해시된 비밀번호

                // 5. 비밀번호 비교
                if (user.getPassword().equals(hashedPassword)) {
                    // 6. 로그인 성공 → 세션에 저장
                    HttpSession session = request.getSession();
                    session.setAttribute("loginUser", user);
                    response.sendRedirect("mainPage.jsp");  // 로그인 성공 시 메인 페이지로 리다이렉트
                } else {
                    // 7. 로그인 실패 → 에러 메시지 표시
                    request.setAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // 8. 사용자 없음
                request.setAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");  // 오류 처리
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}