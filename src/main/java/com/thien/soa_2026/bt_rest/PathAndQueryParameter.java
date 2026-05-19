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

public class PathAndQueryParameter {
    
    static class Product {
        private int id;
        private String name;
        private long priceVND;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getPriceVND() {
            return priceVND;
        }
    }
    
    static class ResponseDTO {
        private String requestId;
        private List<Product> data;

        public String getRequestId() {
            return requestId;
        }

        public List<Product> getData() {
            return data;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "8evU4eQA";
        String url = "http://36.50.135.242:2230/api/rest/path";
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(">>>>> " + res1.body());
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(res1.body(), ResponseDTO.class);
        
        int productId = res.getData().get(0).getId();
        
        HttpRequest getReq2 = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/%d?studentCode=%s&qCode=%s&requestId=%s&currency=USD", url, productId, sCode, qCode, res.getRequestId())))
                .GET()
                .build();
        
        client.send(getReq2, HttpResponse.BodyHandlers.ofString());
    }
}

/*
[REST] Path Parameter + Query Parameter
Nội dung
Mô tả
Bài tập này yêu cầu bạn sử dụng path parameter và query parameter để truy vấn một tài nguyên cụ thể trong danh sách sản phẩm.

Giao thức
Bước 1 — Lấy danh sách sản phẩm (GET):

GET /api/rest/path?studentCode=<mã_sv>&qCode=<qAlias>
Phản hồi:

{
  "requestId": "ghi01234",
  "data": [
    {"id": 1, "name": "Laptop", "priceVND": 15000000},
    {"id": 2, "name": "Smartphone", "priceVND": 8500000},
    {"id": 3, "name": "Tablet", "priceVND": 6200000}
  ]
}
Bước 2 — Truy vấn sản phẩm theo ID (GET):

GET /api/rest/path/{productId}?studentCode=<mã_sv>&qCode=<qAlias>&requestId=ghi01234&currency=USD
Ví dụ: truy vấn sản phẩm có id=2:

GET /api/rest/path/2?studentCode=B22DCCN001&qCode=<qAlias>&requestId=ghi01234&currency=USD
Phản hồi khi đúng:

{"status": "AC", "message": "..."}
Yêu cầu
Chọn bất kỳ id hợp lệ từ danh sách Phase 1 rồi đưa vào path.
Truyền requestId từ Phase 1 qua query parameter.
Truyền currency=USD qua query parameter.
Endpoint Phase 2 chỉ chấp nhận phương thức GET.
*/