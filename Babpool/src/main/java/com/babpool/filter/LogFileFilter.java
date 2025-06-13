package com.babpool.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class LogFileFilter implements Filter {

	private static PrintWriter writer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			// 로그 파일 경로 설정 (webapp/logs/monitor.log)
			ServletContext context = filterConfig.getServletContext();
			String realPath = context.getRealPath("/logs/monitor.log");

			// 로그 출력 스트림 생성
			FileWriter fw = new FileWriter(realPath, true);  // true = append
			writer = new PrintWriter(fw, true);

			writer.println("==== 📂 로그 파일 시작: " + now() + " ====");
		} catch (IOException e) {
			throw new ServletException("로그 파일 생성 실패", e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 필터는 일반 요청에는 별도 처리 없이 통과
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		if (writer != null) {
			writer.println("==== 📁 로그 파일 종료: " + now() + " ====");
			writer.close();
		}
	}

	// 로그 출력용 PrintWriter 반환 (static 메서드)
	public static PrintWriter getWriter() {
		return writer;
	}

	// 현재 시각 포맷터
	private String now() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
