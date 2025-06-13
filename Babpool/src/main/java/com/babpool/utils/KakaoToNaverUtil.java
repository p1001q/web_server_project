package com.babpool.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class KakaoToNaverUtil {

    private static final String KAKAO_API_KEY = ApiKeyUtil.get("kakaoRestAPI"); // 반드시 수정

    // 장소명 → TM 좌표 + 장소명 리턴
    public static String[] getTMCoordinatesFromPlace(String placeName) throws Exception {
        // 1. 장소명 → WGS84 좌표 변환
        String encodedQuery = URLEncoder.encode(placeName, "UTF-8");
        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + encodedQuery;

        HttpURLConnection conn = (HttpURLConnection) new URL(apiURL).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_API_KEY);

        BufferedReader br = new BufferedReader(new InputStreamReader(
            conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8"
        ));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        JSONObject json = new JSONObject(sb.toString());
        JSONArray documents = json.getJSONArray("documents");
        if (documents.length() == 0) return null;

        JSONObject item = documents.getJSONObject(0);
        String x = item.get("x").toString(); // getString → get().toString() 변경
        String y = item.get("y").toString();
        String name = item.getString("place_name");

        // 2. WGS84 → TM 좌표 변환
        return convertWGS84toTM(x, y, name);
    }

    // WGS84 좌표 → TM 좌표
    private static String[] convertWGS84toTM(String x, String y, String name) throws Exception {
        String url = "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=" + x + "&y=" + y + "&input_coord=WGS84&output_coord=TM";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_API_KEY);

        BufferedReader br = new BufferedReader(new InputStreamReader(
            conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), "UTF-8"
        ));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        JSONObject json = new JSONObject(sb.toString());
        JSONArray documents = json.getJSONArray("documents");
        if (documents.length() == 0) return null;

        JSONObject tm = documents.getJSONObject(0);
        return new String[] {
            tm.get("x").toString(), // getString → get().toString() 변경
            tm.get("y").toString(),
            name
        };
    }
}