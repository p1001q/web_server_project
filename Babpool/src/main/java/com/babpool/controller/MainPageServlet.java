package com.babpool.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.babpool.dao.BookmarkDAO;
import com.babpool.dao.CategoryDAO;
import com.babpool.dao.StoreDAO;
import com.babpool.dao.TagDAO;
import com.babpool.dto.CategoryDTO;
import com.babpool.dto.StoreDTO;
import com.babpool.dto.TagDTO;
import com.babpool.dto.UserDTO;
import com.babpool.utils.DBUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/mainPage")
public class MainPageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션에서 유저 정보 가져오기
        HttpSession session = request.getSession();
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boolean isBookmarked = false; // 기본값

        // storeId 파라미터 확인 및 처리 (중복 받지 않도록)
        String storeIdParam = request.getParameter("storeId"); // storeId는 한 번만 받으면 됨
        if (storeIdParam != null && loginUser != null) {
            int userId = loginUser.getUserId();
            int storeId = Integer.parseInt(storeIdParam);

            // BookmarkDAO 객체 생성
            BookmarkDAO bookmarkDAO = new BookmarkDAO(DBUtil.getConnection());
            // 유저가 해당 가게를 찜했는지 확인
            isBookmarked = bookmarkDAO.isBookmarked(userId, storeId); // 찜 여부 확인
        }

        // 'mascot' 파라미터 확인 (마스코트 클릭 상태)
        String mascotParam = request.getParameter("mascot");
        boolean mascotClicked = "true".equals(mascotParam);  // 마스코트 클릭 여부

        // 가게 정보 조회
        StoreDTO store = null;
        if (storeIdParam != null) {
            int storeId = Integer.parseInt(storeIdParam);
            StoreDAO storeDAO = new StoreDAO(DBUtil.getConnection());
            store = storeDAO.getStoreById(storeId);  // 가게 정보 조회

            // store가 null인 경우 처리
            if (store == null) {
                store = new StoreDTO();
                store.setName("가게 정보 없음");
                store.setAddress("주소 미제공");
                store.setPhone("연락처 미제공");
                store.setOpenTime("운영시간 미제공");
                store.setRatingAvg(0.0f);
                store.setLikeCount(0);
            }
        }

        // 찜한 가게 수 조회
        int likeCount = (store != null) ? store.getLikeCount() : 0; // getStoreById에서 이미 포함되어 있음

        // CategoryDAO와 TagDAO를 사용하여 카테고리와 태그 정보 조회
        Connection conn = DBUtil.getConnection();
        CategoryDAO categoryDAO = new CategoryDAO(conn);
        TagDAO tagDAO = new TagDAO(conn);

        List<CategoryDTO> categoryList = categoryDAO.getAllCategories();  // 카테고리 정보 조회
        List<TagDTO> tagList = tagDAO.getAllTags();  // 태그 정보 조회

        // Top 3 가게 정보 조회
        StoreDAO storeDAO2 = new StoreDAO(conn);
        List<Map<String, Object>> topStores = storeDAO2.getTop3StoresWithCategory();

        // 각 Top 3 가게에 대해 찜 여부 확인
        BookmarkDAO bookmarkDAO2 = new BookmarkDAO(DBUtil.getConnection());
        for (Map<String, Object> topStore : topStores) {
            int topStoreId = (Integer) topStore.get("storeId");
            boolean topStoreBookmarked = false;
            if (loginUser != null) {
                topStoreBookmarked = bookmarkDAO2.isBookmarked(loginUser.getUserId(), topStoreId); // 찜 여부 확인
            }
            topStore.put("isBookmarked", topStoreBookmarked); // 각 가게에 찜 여부 추가
        }

        // 데이터를 JSP로 전달
        request.setAttribute("isBookmarked", isBookmarked);  // 찜 상태 전달
        request.setAttribute("categoryList", categoryList);  // 카테고리 리스트 전달
        request.setAttribute("tagList", tagList);  // 태그 리스트 전달
        request.setAttribute("store", store); // 가게 정보 전달
        request.setAttribute("likeCount", likeCount); // 찜한 사람 수 전달
        request.setAttribute("mascotClicked", mascotClicked); // 마스코트 클릭 상태 전달
        request.setAttribute("topStores", topStores); // Top 3 가게 데이터 전달

        // JSP로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/mainPage.jsp");
        dispatcher.forward(request, response);
    }
}
