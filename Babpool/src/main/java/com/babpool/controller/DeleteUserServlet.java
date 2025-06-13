package com.babpool.controller;

import java.io.IOException;
import java.sql.Connection;

import com.babpool.dao.UserDAO;
import com.babpool.utils.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// mypage.jsp - 동국 -> 탈퇴하기 버튼 사용 서블릿 클래스
@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 로그인된 사용자 이메일 가져오기 (세션에서)
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        // DB 연결 및 DAO 객체 생성
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            // 사용자가 삭제할 때, UserDAO에서 해당 사용자 삭제 메서드 호출
            boolean result = userDAO.deleteUserByEmail(email);

            if (result) {
                // 삭제 성공 시, 로그아웃 처리 후 메인 페이지로 리다이렉트
                session.invalidate();  // 세션 무효화
                response.sendRedirect(request.getContextPath() + "/mainPage.jsp");
            } else {
                // 삭제 실패 시 에러 메시지 표시
                response.getWriter().println("회원 탈퇴에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("서버 오류가 발생했습니다.");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}