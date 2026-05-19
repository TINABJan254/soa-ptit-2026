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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class SignatureHMAC {
    
    static class Data {
        private String nonce;
        private String signingKey;
        private List<String> events;

        public String getNonce() {
            return nonce;
        }

        public String getSigningKey() {
            return signingKey;
        }

        public List<String> getEvents() {
            return events;
        }
        
    }
    
    static class ResponseDTO {
        private String requestId;
        private Data data;

        public String getRequestId() {
            return requestId;
        }

        public Data getData() {
            return data;
        }
    }
    
    static class SubmitDTO {
        private String studentCode;
        private String qCode;
        private String requestId;

        public SubmitDTO(String studentCode, String qCode, String requestId) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "tvMp50Q2";
        String url = "http://36.50.135.242:2230/api/rest/header";
        
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>> " + res1.body());
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(res1.body(), ResponseDTO.class);
        
        //payload
        String payload = String.format("%s:%s:%s", res.getData().getNonce(), String.join("|", res.getData().getEvents()), sCode.toUpperCase());
        
        String signature = hmacSHA256Hex(res.getData().getSigningKey(), payload);
        
        SubmitDTO sub = new SubmitDTO(sCode, qCode, res.getRequestId());
        String postJson = gson.toJson(sub);
        
        //post
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/submit"))
                .header("X-Signature", signature)
                .POST(HttpRequest.BodyPublishers.ofString(postJson))
                .build();
        
        client.send(postReq, HttpResponse.BodyHandlers.ofString());
    }
    
    public static String hmacSHA256Hex(String key, String payload) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        mac.init(secretKey);

        byte[] bytes = mac.doFinal(
                payload.getBytes(StandardCharsets.UTF_8)
        );

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
    
}

/*
[REST Header] Chữ ký HMAC request
Nội dung
Tính và gửi chữ ký HMAC-SHA256 qua header
Bạn cần tạo chữ ký từ dữ liệu phase 1 và gửi chữ ký đó qua custom header khi submit.

Phase 1 - Lấy dữ liệu ký
GET /api/rest/header?studentCode=<mã_sv>&qCode=<qAlias trong đề>
Server trả về nonce, signingKey và danh sách events.

Yêu cầu xử lý
Tạo payload theo đúng công thức:

<nonce>:<event1>|<event2>|...|<eventN>:<STUDENT_CODE_IN_HOA>
Tính chữ ký dạng hex lowercase bằng HmacSHA256(signingKey, payload).

Phase 2 - Nộp chữ ký
POST /api/rest/header/submit
Header bắt buộc:

X-Signature: <hmac_hex>
{"studentCode":"<mã_sv>","qCode":"<qAlias trong đề>","requestId":"<requestId phase 1>"}
Không dùng lại chữ ký của request khác vì nonce, signingKey và events thay đổi theo từng request.
*/