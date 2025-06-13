<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>오류 발생</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f5eb;
            text-align: center;
            padding-top: 100px;
        }
        .error-box {
            display: inline-block;
            padding: 40px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 { color: #b71c1c; }
        p { color: #333; }
    </style>
</head>
<body>

<div class="error-box">
    <h1>문제가 발생했습니다 😢</h1>
    <p>잠시 후 다시 시도해주세요.</p>
    <button onclick="history.back()">이전 페이지로</button>
</div>

</body>
</html>
