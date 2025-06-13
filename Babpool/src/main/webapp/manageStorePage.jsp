<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="com.babpool.dto.*" %>
<%@ page import="com.babpool.dao.*" %>
<%@ page import="com.babpool.utils.DBUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>음식점 관리</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/manageStore.css" />
</head>
<body>
    <div class="manage-container">
        <h2>음식점 관리</h2>

        <div class="form-container">
            <!-- 좌측: 음식점 영역 -->
            <div class="left-section">
                <h3>음식점 정보</h3>

                <form action="<%=request.getContextPath()%>/StoreSelectAllServlet" method="get">
                    <input type="hidden" name="toggle" value="true" />
                    <button type="submit" class="btn">음식점 전체 조회</button>
                </form>

                <form action="<%=request.getContextPath()%>/StoreSelectByIdServlet" method="get">
                    <input type="number" name="storeId" placeholder="가게 ID 입력" required />
                    <button type="submit" class="btn">선택 조회</button>
                </form>

                <form action="<%=request.getContextPath()%>/StoreInsertServlet" method="post">
                    <input type="text" name="name" placeholder="가게 이름" required />
                    <input type="text" name="address" placeholder="주소" required />
                    <input type="text" name="phone" placeholder="전화번호" required />
                    <input type="text" name="openTime" placeholder="운영시간" required />
                    <button type="submit" class="btn">음식점 등록</button>
                </form>
                
                <c:if test="${not empty storeInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeInsertResult}">
				                ✅ 음식점 등록 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 음식점 등록 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <form action="<%=request.getContextPath()%>/StoreUpdateServlet" method="post">
                    <input type="number" name="storeId" placeholder="수정할 가게 ID" required />
                    <input type="text" name="name" placeholder="가게 이름 수정" />
                    <input type="text" name="address" placeholder="주소 수정" />
                    <input type="text" name="phone" placeholder="전화번호 수정" />
                    <input type="text" name="openTime" placeholder="운영시간 수정" />
                    <button type="submit" class="btn">수정</button>
                </form>
                
                <c:if test="${not empty storeUpdateResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeUpdateResult}">
				                ✅ 음식점 수정 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 음식점 수정 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
    
                <form action="<%=request.getContextPath()%>/StoreDeleteServlet" method="post">
                    <input type="number" name="storeId" placeholder="삭제할 가게 ID" required />
                    <button type="submit" class="btn">삭제</button>
                </form>
                
                <c:if test="${not empty storeDeleteResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${storeDeleteResult}">
				                ✅ 음식점 삭제 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 음식점 삭제 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <h4>가게-카테고리 연결</h4>
                <form action="<%=request.getContextPath()%>/StoreCategoryInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="number" name="categoryId" placeholder="카테고리 ID" required />
                    <button type="submit" class="btn">카테고리 연결</button>
                </form>

                <h4>가게-태그 연결</h4>
                <form action="<%=request.getContextPath()%>/StoreTagInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="number" name="tagId" placeholder="태그 ID" required />
                    <button type="submit" class="btn">태그 연결</button>
                </form>

                <!-- 메뉴 등록 -->
                <h4>메뉴 등록</h4>
                <form action="<%=request.getContextPath()%>/MenuInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="가게 ID" required />
                    <input type="text" name="name" placeholder="메뉴 이름" required />
                    <input type="number" name="price" placeholder="가격" required />
                    <button type="submit" class="btn">메뉴 추가</button>
                </form>
                
                <c:if test="${not empty menuInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuInsertResult}">
				                ✅ 메뉴 추가 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 추가 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                

                <!-- 메뉴 수정 -->
                <form action="<%=request.getContextPath()%>/MenuUpdateServlet" method="post">
                    <input type="number" name="menuId" placeholder="메뉴 ID" required />
                    <input type="text" name="name" placeholder="메뉴 이름 수정" />
                    <input type="number" name="price" placeholder="가격 수정" />
                    <button type="submit" class="btn">메뉴 수정</button>
                </form>
                
                <c:if test="${not empty menuUpdateResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuUpdateResult}">
				                ✅ 메뉴 수정 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 수정 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                
                

                <!-- 메뉴 삭제 -->
                <form action="<%=request.getContextPath()%>/MenuDeleteServlet" method="post">
                    <input type="number" name="menuId" placeholder="메뉴 ID" required />
                    <button type="submit" class="btn">메뉴 삭제</button>
                </form>
                
                <c:if test="${not empty menuDeleteResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${menuDeleteResult}">
				                ✅ 메뉴 삭제 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 메뉴 삭제 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <c:if test="${not empty storeList}">
                    <h4>전체 음식점 목록</h4>
                    <table class="simple-table">
                        <tr><th>ID</th><th>이름</th><th>주소</th><th>전화번호</th></tr>
                        <c:forEach var="s" items="${storeList}">
                            <tr>
                                <td>${s.storeId}</td>
                                <td>${s.name}</td>
                                <td>${s.address}</td>
                                <td>${s.phone}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                
                <c:if test="${not empty store}">
				    <h4>선택 음식점 조회 결과</h4>
				    <table class="simple-table">
				        <tr><th>ID</th><th>이름</th><th>주소</th><th>전화번호</th><th>운영시간</th></tr>
				        <tr>
				            <td>${store.storeId}</td>
				            <td>${store.name}</td>
				            <td>${store.address}</td>
				            <td>${store.phone}</td>
				            <td>${store.openTime}</td>
				        </tr>
				    </table>
				</c:if>
				<c:if test="${storeNotFound}">
				    <p style="color:red;">해당 ID의 음식점이 존재하지 않습니다.</p>
				</c:if>
                
            </div>

            <!-- 우측: 마커 영역 -->
            <div class="right-section">
                <h3>마커 정보</h3>

                <form action="<%=request.getContextPath()%>/MarkerSelectAllServlet" method="get">
                    <input type="hidden" name="toggle" value="true" />
                    <button type="submit" class="btn">마커 전체 조회</button>
                </form>

                <form action="<%=request.getContextPath()%>/MarkerSelectByStoreIdServlet" method="get">
                    <input type="number" name="storeId" placeholder="음식점 ID 입력" required />
                    <button type="submit" class="btn">마커 조회</button>
                </form>

                <h4>장소 좌표 등록</h4>
                <form action="<%=request.getContextPath()%>/MarkerInsertServlet" method="post">
                    <input type="number" name="storeId" placeholder="음식점 ID" required />
                    <input type="text" name="storeName" placeholder="음식점 이름" required />
                    <input type="text" name="wgsX" placeholder="WGS X" required />
                    <input type="text" name="wgsY" placeholder="WGS Y" required />
                    <input type="text" name="tmX" placeholder="TM X" required />
                    <input type="text" name="tmY" placeholder="TM Y" required />
                    <input type="text" name="url" placeholder="URL" />
                    <input type="text" name="unicode" placeholder="유니코드" />
                    <button type="submit" class="btn">마커 추가</button>
                </form>
                
                <c:if test="${not empty markerInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerInsertResult}">
				                ✅ 마커 추가 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커 추가 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <h4>마커-카테고리 연결</h4>
                <form action="<%=request.getContextPath()%>/MarkerCategoryInsertServlet" method="post">
                    <input type="number" name="markerId" placeholder="마커 ID" required />
                    <input type="number" name="categoryId" placeholder="카테고리 ID" required />
                    <button type="submit" class="btn">카테고리 연결</button>
                </form>
                
                <c:if test="${not empty markerCategoryInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerCategoryInsertResult}">
				                ✅ 마커-카테고리 연결 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커-카테고리 연결 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <h4>마커-태그 연결</h4>
                <form action="<%=request.getContextPath()%>/MarkerTagInsertServlet" method="post">
                    <input type="number" name="markerId" placeholder="마커 ID" required />
                    <input type="number" name="tagId" placeholder="태그 ID" required />
                    <button type="submit" class="btn">태그 연결</button>
                </form>
                
                <c:if test="${not empty markerTagInsertResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${markerTagInsertResult}">
				                ✅ 마커-태그 연결 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 마커-태그 연결 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <h4>길찾기 URL 생성</h4>
                <form action="<%=request.getContextPath()%>/RouteUrlGenerateServlet" method="post">
                    <input type="text" name="tmX" placeholder="TM X 좌표" required />
                    <input type="text" name="tmY" placeholder="TM Y 좌표" required />
                    <input type="text" name="unicode" placeholder="유니코드 이름" required />
                    <input type="text" name="placeId" placeholder="장소 ID" required />
                    <button type="submit" class="btn">URL 생성</button>
                </form>
                
                <c:if test="${not empty routeUrlGenerateResult}">
				    <div class="result-message">
				        <c:choose>
				            <c:when test="${routeUrlGenerateResult}">
				                ✅ 길찾기 URL 생성 성공
				            </c:when>
				            <c:otherwise>
				                ❌ 길찾기 URL 생성 실패
				            </c:otherwise>
				        </c:choose>
				    </div>
				</c:if>
                

                <c:if test="${not empty markerList}">
                    <h4>전체 마커 목록</h4>
                    <table class="simple-table">
                        <tr><th>마커ID</th><th>음식점ID</th><th>음식점명</th><th>WGS</th><th>TM</th></tr>
                        <c:forEach var="m" items="${markerList}">
                            <tr>
                                <td>${m.markerId}</td>
                                <td>${m.storeId}</td>
                                <td>${m.storeName}</td>
                                <td>(${m.wgsX}, ${m.wgsY})</td>
                                <td>(${m.tmX}, ${m.tmY})</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                
                <c:if test="${not empty marker}">
				    <h4>선택 마커 조회 결과</h4>
				    <table class="simple-table">
				        <tr><th>마커ID</th><th>스토어ID</th><th>스토어명</th><th>WGS 좌표</th><th>TM 좌표</th></tr>
				        <tr>
				            <td>${marker.markerId}</td>
				            <td>${marker.storeId}</td>
				            <td>${marker.storeName}</td>
				            <td>(${marker.wgsX}, ${marker.wgsY})</td>
				            <td>(${marker.tmX}, ${marker.tmY})</td>
				        </tr>
				    </table>
				</c:if>
				
				<c:if test="${markerNotFound}">
				    <p style="color:red;">해당 ID의 마커가 존재하지 않습니다.</p>
				</c:if>

                <!-- ✅ 항상 출력되는 카테고리/태그 테이블 -->
                <%
                    Connection conn = DBUtil.getConnection();
                    CategoryDAO cdao = new CategoryDAO(conn);
                    TagDAO tdao = new TagDAO(conn);
                    List<CategoryDTO> categoryList = cdao.getAllCategories();
                    List<TagDTO> tagList = tdao.getAllTags();
                    conn.close();
                %>

                <h4>카테고리 목록</h4>
                <table class="simple-table">
                    <tr><th>ID</th><th>카테고리명</th></tr>
                    <c:forEach var="c" items="<%= categoryList %>">
                        <tr><td>${c.categoryId}</td><td>${c.name}</td></tr>
                    </c:forEach>
                </table>

                <h4>태그 목록</h4>
                <table class="simple-table">
                    <tr><th>ID</th><th>태그명</th></tr>
                    <c:forEach var="t" items="<%= tagList %>">
                        <tr><td>${t.tagId}</td><td>${t.name}</td></tr>
                    </c:forEach>
                </table>

            </div>
        </div>
    </div>
</body>
</html>
 