/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_rest;

/**
 *
 * @author ADMIN
 */

import java.io.*;
import java.util.*;
import java.net.http.*;
import java.net.*;

import com.google.gson.*;

public class TransactionProb {
    
    static class Transaction {
        private String transactionId;
        private double amount;
        private String currency;
        private String status;

        public String getTransactionId() {
            return transactionId;
        }

        public double getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }

        public String getStatus() {
            return status;
        }    
    }
    
    static class ResponseDTO {
        private String requestId;
        private List<Transaction> data;

        public String getRequestId() {
            return requestId;
        }

        public List<Transaction> getData() {
            return data;
        }
    }
    
    static class TransSumary {
        private double capturedTotal, refundedTotal, netTotal;
        private int failedCount;

        public TransSumary(double capturedTotal, double refundedTotal, double netTotal, int failedCount) {
            this.capturedTotal = capturedTotal;
            this.refundedTotal = refundedTotal;
            this.netTotal = netTotal;
            this.failedCount = failedCount;
        }
    }
    
    static class SubmitDTO {
        private String studentCode;
        private String qCode;
        private String requestId;
        private TransSumary answer;

        public SubmitDTO(String studentCode, String qCode, String requestId, TransSumary answer) {
            this.studentCode = studentCode;
            this.qCode = qCode;
            this.requestId = requestId;
            this.answer = answer;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "Ve90scoQ";
        String url = "http://36.50.135.242:2230/api/rest/data";
        
        //req
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?studentCode=%s&qCode=%s", url, sCode, qCode)))
                .GET()
                .build();
        
        HttpResponse<String> res1 = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>>> " + res1.body());
        
        Gson gson = new Gson();
        ResponseDTO res = gson.fromJson(res1.body(), ResponseDTO.class);
        
        //solve
        double cTotal = 0;
        double rTotal = 0;
        int failCnt = 0;
        
        for (Transaction trans : res.getData()) {
            if (trans.getStatus().equals("CAPTURED")) {
                cTotal += trans.getAmount();
            }
            if (trans.getStatus().equals("REFUNDED")) {
                rTotal += trans.getAmount();
            }
            if (trans.getStatus().equals("FAILED")) {
                ++failCnt;
            }
        }
        
        //submit
        TransSumary sumary = new TransSumary(cTotal, rTotal, cTotal - rTotal, failCnt);
        
        SubmitDTO sub = new SubmitDTO(sCode, qCode, res.getRequestId(), sumary);
        
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/submit"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(sub)))
                .build();
        
        HttpResponse<String> postRes = client.send(postReq, HttpResponse.BodyHandlers.ofString());
        System.out.println(">>>>>>>>>> " + postRes.body());
    }
}

/*
Nội dung
Đối soát giao dịch thanh toán
Bạn cần viết client REST thực hiện quy trình 2 phase để xử lý một danh sách giao dịch thanh toán được sinh ngẫu nhiên theo từng request.

Phase 1 - Lấy dữ liệu
GET /api/rest/data?studentCode=<mã_sv>&qCode=<qAlias trong đề>
Server trả về requestId và data là mảng JSON các giao dịch. Mỗi giao dịch có transactionId, amount, currency, status. Status có thể là CAPTURED, REFUNDED, FAILED, PENDING.

Yêu cầu xử lý
capturedTotal: tổng amount của giao dịch CAPTURED.
refundedTotal: tổng amount của giao dịch REFUNDED.
netTotal: capturedTotal - refundedTotal.
failedCount: số giao dịch FAILED.
Các giá trị tiền làm tròn 2 chữ số thập phân.

Phase 2 - Nộp kết quả
POST /api/rest/data/submit
{
  "studentCode": "<mã_sv>",
  "qCode": "<qAlias trong đề>",
  "requestId": "<requestId phase 1>",
  "answer": {"capturedTotal": 123.45, "refundedTotal": 20.00, "netTotal": 103.45, "failedCount": 2}
}
Không dùng mã chuẩn Q2012 làm qCode khi gọi server. Dữ liệu thay đổi theo mỗi request, vì vậy client phải parse response và tính động.
*/
