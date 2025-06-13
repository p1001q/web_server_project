package com.babpool.controller;

import com.babpool.dao.*;
import com.babpool.dto.*;
import com.babpool.utils.DBUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/placeDetail")
public class PlaceDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int storeId = Integer.parseInt(request.getParameter("storeId"));
        int userId = 1;  // 세션 로그인 연동 전 임시 유저

        Connection conn = DBUtil.getConnection();

        try {
            // ⭐ 1. 가게 기본정보
            StoreDAO storeDAO = new StoreDAO(conn);
            StoreDTO store = storeDAO.getStoreById(storeId);
            request.setAttribute("store", store);

            // ⭐ 2. 메뉴
            MenuDAO menuDAO = new MenuDAO(conn);
            List<MenuDTO> menus = menuDAO.getMenusByStoreId(storeId);
            request.setAttribute("menus", menus);

            // ⭐ 태그 (수정본: TagDAO로 변경)
            TagDAO tagDAO = new TagDAO(conn);
            List<TagDTO> tags = tagDAO.getTagsByStoreId(storeId);
            request.setAttribute("tags", tags);

            // ⭐ 4. 리뷰 집계 (바그래프용)
            ReviewDAO reviewDAO = new ReviewDAO(conn);
            Map<Integer, Integer> scoreMap = reviewDAO.getScoreSummaryByStore(storeId);
            request.setAttribute("scoreMap", scoreMap);

            // ⭐ 5. 리뷰 리스트 (정렬 지원)
            String sortType = request.getParameter("sortType");
            if (sortType == null || sortType.isEmpty()) sortType = "recent";
            List<ReviewDTO> reviews = reviewDAO.getReviewsByStoreSorted(storeId, sortType);
            request.setAttribute("reviews", reviews);
            request.setAttribute("sortType", sortType);

            // ⭐ 6. 총 리뷰 수 전달 (풀잎들의 후기: 총 n명 표시용)
            int totalCount = reviews.size();
            request.setAttribute("totalCount", totalCount);

            // ⭐ 7. 별점 평균 계산 (가게 이름 옆에 출력용)
            double ratingSum = 0;
            for (ReviewDTO r : reviews) ratingSum += r.getRating();
            double ratingAvg = (totalCount == 0) ? 0.0 : ratingSum / totalCount;
            request.setAttribute("ratingAvg", ratingAvg);

            // ⭐ 8. 즐겨찾기 상태
            BookmarkDAO bookmarkDAO = new BookmarkDAO(conn);
            boolean isBookmarked = bookmarkDAO.isBookmarked(userId, storeId);
            int likeCount = bookmarkDAO.getBookmarkCountByStoreId(storeId);
            request.setAttribute("isBookmarked", isBookmarked);
            request.setAttribute("likeCount", likeCount);
            
            // ⭐ 카테고리 추가
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            List<CategoryDTO> categories = categoryDAO.getCategoriesByStoreId(storeId);
            request.setAttribute("categories", categories);

            // ⭐ JSP 이동
            request.getRequestDispatcher("/placeDetail.jsp").forward(request, response);

            

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "서버 내부 오류 발생");
        } finally {
            try { conn.close(); } catch (Exception e) {}
        }
    }
}