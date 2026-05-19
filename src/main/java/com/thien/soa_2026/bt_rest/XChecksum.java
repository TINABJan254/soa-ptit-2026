/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class XChecksum {
    
    static class ResponseDTO {
        private String requestId;
        private List<String> data;

        public String getRequestId() {
            return requestId;
        }

        public List<String> getData() {
            return data;
        }
        
    }
    
    static class SubDTO {
        private String studentCode;
        private String qCode;
        private String requestId;

        public SubDTO(String studentCode, String qCode, String requestId) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "gKgGUDUX";
        String url = "http://36.50.135.242:2230/api/rest/header";
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        String xChecksum = res1.headers().firstValue("X-Checksum").orElse("");
        System.out.println(">>>>>> " + xChecksum);
        
        String body = res1.body();
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(body, ResponseDTO.class);
        
        SubDTO sub = new SubDTO(sCode, qCode, res.getRequestId());
        String postJson = gson.toJson(sub);
        
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/submit",url)))
                .header("Content-Type","application/json")
                .header( "X-Checksum",xChecksum )
                .POST(HttpRequest.BodyPublishers.ofString(postJson))
                .build();
        
        client.send(postReq, HttpResponse.BodyHandlers.ofString());
    }
}

/*
[REST] HTTP Header — X-Checksum
Mô tả
Bài tập này yêu cầu bạn đọc một custom HTTP response header (X-Checksum) từ phản hồi Phase 1 và gửi lại giá trị đó trong request header ở Phase 2.

Giao thức
Bước 1 — Lấy dữ liệu (GET):

GET /api/rest/header?studentCode=<mã_sv>&qCode=<qAlias>
Phản hồi (body):

{
  "requestId": "def56789",
  "data": [3421, 7890, 1234, 5678, 9012, 3456]
}
Phản hồi (header):

X-Checksum: a3f2c1...  (SHA-256 của danh sách số)
Bước 2 — Gửi đáp án (POST):

POST /api/rest/header/submit
Body JSON:

{
  "studentCode": "<mã_sv>",
  "qCode": "<qAlias>",
  "requestId": "def56789"
}
Request header bắt buộc:

X-Checksum: a3f2c1...  (giá trị đọc từ Phase 1)
Phản hồi khi đúng:

{"status": "AC", "message": "..."}
Yêu cầu
Đọc giá trị header X-Checksum từ phản hồi Phase 1.
Gửi lại đúng giá trị đó trong request header X-Checksum ở Phase 2.
Không cần tính SHA-256 thủ công — chỉ cần truyền lại giá trị đã nhận.
*/