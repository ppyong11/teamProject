package com.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.XML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String KEY = "API 키";
    private static final String BASE_URL = "http://plus.kipris.or.kr/openapi/rest/patUtiModInfoSearchSevice/cpcSearchInfo";
    private static List<JsonObject> patentDataList = new ArrayList<>(); // 모든 특허 정보를 저장할 리스트

    // MariaDB JDBC 설정
    private static final String DB_URL = "jdbc:mariadb://localhost:3030/PatentDB";
    private static final String DB_USER = "project";
    private static final String DB_PASSWORD = "비밀번호";

    public static void main(String[] args) {
       //3번 호출해서 데이터 모아서 적재
        List<String> cpcCodes= Arrays.asList("A01M", "A01N", "A01P"); //검색할 cpc 리스트
        try {
            for (String cpcCode : cpcCodes) {
                logger.info("CPC 코드 처리: " + cpcCode);
                int totalResults = getTotalResults(cpcCode);

                if (totalResults > 0) {
                    processResults(cpcCode, totalResults);
                } else {
                    logger.info("검색 결과가 없습니다.");
                }
            }
        } catch (Exception e) {
            logger.error("예외 발생", e);
        }
    }

    // JDBC 연결 메서드
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static int getTotalResults(String cpcNumber) throws Exception {
        String apiUrl = buildApiUrl(cpcNumber, 1);
        String response = getApiResponse(apiUrl);
        if (response != null) {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            System.out.println(jsonResponse);
            return extractTotalSearchCount(jsonResponse);
        }
        return 0;
    }

    private static String buildApiUrl(String cpcNumber, int docsCount) {
        return String.format(
                "%s?cpcNumber=%s&docsCount=%d&docsStart=1&descSort=false&sortSpec=AD&" +
                        "utility=false&lastvalue=R&accessKey=%s",
                BASE_URL, cpcNumber, docsCount, KEY
        );
    }

    private static String getApiResponse(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        logger.debug("응답 코드: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JSONObject xmlJSONObj = XML.toJSONObject(response.toString());
                return xmlJSONObj.toString(4);
            }
        } else {
            logger.error("GET 요청 실패. 응답 코드: " + responseCode);
            return null;
        }
    }

    private static int extractTotalSearchCount(JsonObject jsonResponse) {
        try {
            return jsonResponse.getAsJsonObject("response")
                    .getAsJsonObject("body")
                    .getAsJsonObject("items")
                    .get("TotalSearchCount")
                    .getAsInt();
        } catch (Exception e) {
            logger.error("TotalSearchCount 추출 실패", e);
            return 0;
        }
    }


    private static void processResults(String cpcNumber, int totalResults) throws Exception {
        int docsCount = 500; // 한 페이지당 문서 수

        try {
            String apiUrl = buildApiUrl(cpcNumber, docsCount);
            String response = getApiResponse(apiUrl);

            if (response != null) {
                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                JsonObject items = jsonResponse.getAsJsonObject("response")
                        .getAsJsonObject("body")
                        .getAsJsonObject("items");

                if (items.has("PatentUtilityInfo")) {
                    JsonElement patentInfo = items.get("PatentUtilityInfo");
                    if (patentInfo.isJsonArray()) {
                        JsonArray patentArray = patentInfo.getAsJsonArray();
                        for (JsonElement element : patentArray) {
                            patentDataList.add(element.getAsJsonObject()); // 데이터를 리스트에 추가
                        }
                    } else if (patentInfo.isJsonObject()) {
                        patentDataList.add(patentInfo.getAsJsonObject()); // 데이터를 리스트에 추가
                    }
                } else {
                    logger.warn("PatentUtilityInfo 필드가 없습니다.");
                }
            }
        } catch (Exception e) {
            logger.error("페이지 처리 중 오류 발생", e);
        }

        logger.info("CPC 코드 " + cpcNumber + " 처리 완료.");
        logger.info("현재까지 누적된 결과 건수: " + patentDataList.size());

        saveDataToDatabase(); // 데이터 저장
    }

    private static void saveDataToDatabase() {
        String insertSQL = "INSERT IGNORE INTO patent (ApplicationNumber, InventionName, InternationalpatentclassificationNumber, Abstract, RegistrationStatus, Applicant, " +
                "RegistrationDate, RegistrationNumber, Applicationdate, OpeningNumber, OpeningDate, PublicNumber, PublicDate, DrawingPath, ThumbnailPath) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int batchSize = 1000; // 배치 크기 설정
        int totalInsertedCount = 0; // 총 삽입된 데이터 수

        try (Connection conn = getConnection();
             PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false); // 자동 커밋 비활성화
            int currentBatchSize = 0; // 현재 배치 크기

            for (JsonObject patentJson : patentDataList) {
                try {
                    pstmtInsert.setString(1, patentJson.get("ApplicationNumber").getAsString());
                    pstmtInsert.setString(2, patentJson.get("InventionName").getAsString());
                    pstmtInsert.setString(3, patentJson.get("InternationalpatentclassificationNumber").getAsString());
                    pstmtInsert.setString(4, patentJson.get("Abstract").getAsString());
                    pstmtInsert.setString(5, patentJson.get("RegistrationStatus").getAsString());
                    pstmtInsert.setString(6, patentJson.get("Applicant").getAsString());
                    pstmtInsert.setDate(7, getDateFromJson(patentJson, "RegistrationDate", formatter));
                    pstmtInsert.setString(8, patentJson.get("RegistrationNumber").getAsString());
                    pstmtInsert.setDate(9, getDateFromJson(patentJson, "ApplicationDate", formatter));
                    pstmtInsert.setString(10, patentJson.get("OpeningNumber").getAsString());
                    pstmtInsert.setDate(11, getDateFromJson(patentJson, "OpeningDate", formatter));
                    pstmtInsert.setString(12, patentJson.get("PublicNumber").getAsString());
                    pstmtInsert.setDate(13, getDateFromJson(patentJson, "PublicDate", formatter));
                    pstmtInsert.setString(14, patentJson.get("DrawingPath").getAsString());
                    pstmtInsert.setString(15, patentJson.get("ThumbnailPath").getAsString());

                    pstmtInsert.addBatch(); // 배치에 추가
                    currentBatchSize++;

                    // 배치 크기 도달 시 실행 및 커밋
                    if (currentBatchSize >= batchSize) {
                        int[] results = pstmtInsert.executeBatch(); // 배치 실행
                        conn.commit();
                        totalInsertedCount += countInsertedRecords(results); // 성공적으로 삽입된 레코드 수 추가
                        currentBatchSize = 0;
                    }
                } catch (SQLException e) {
                    logger.error("데이터 삽입 중 오류 발생", e);
                }
            }

            // 남아있는 데이터 배치 실행
            if (currentBatchSize > 0) {
                int[] results = pstmtInsert.executeBatch(); // 배치 실행
                conn.commit();
                totalInsertedCount += countInsertedRecords(results); // 성공적으로 삽입된 레코드 수 추가
            }

            logger.info("총 {}개의 데이터가 MariaDB에 성공적으로 저장되었습니다.", totalInsertedCount);
        } catch (SQLException e) {
            logger.error("MariaDB에 데이터 저장 중 오류 발생", e);
        }
    }

    // 성공적으로 삽입된 레코드 수 계산 메서드
    private static int countInsertedRecords(int[] results) {
        int count = 0;
        for (int result : results) {
            if (result == PreparedStatement.SUCCESS_NO_INFO || result > 0) {
                count++;
            }
        }
        return count;
    }

    //json 날짜 형식 포맷 변경 함수
    private static java.sql.Date getDateFromJson(JsonObject patentJson, String key, DateTimeFormatter formatter) {
        try {
            String dateStr = patentJson.has(key) ? patentJson.get(key).getAsString() : null;
            if (dateStr != null && !dateStr.isEmpty()) {
                // 날짜 포맷이 맞지 않으면 예외가 발생하므로 try-catch로 감싸서 안전하게 처리
                DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");  // 기존 포맷을 수정
                LocalDate date = LocalDate.parse(dateStr, newFormatter);  // 새로운 포맷으로 파싱
                return java.sql.Date.valueOf(date); // java.sql.Date로 변환
            } else {
                return null; // 날짜 값이 없거나 빈 문자열인 경우 null 반환
            }
        } catch (DateTimeParseException e) {
            logger.warn("날짜 파싱 실패: " + key + " = " + patentJson.get(key).getAsString());
            return null; // 파싱 실패 시 null 반환
        }
    }
}