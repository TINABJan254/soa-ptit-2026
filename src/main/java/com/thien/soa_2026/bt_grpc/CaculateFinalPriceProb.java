/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import java.io.*;
import java.util.*;

import io.grpc.*;

import GRPC.*;
import com.google.gson.Gson;

public class CaculateFinalPriceProb {
    
    static class Product {
        private String name;
        private double price;
        private double taxRate;
        private double discount;
        private double finalPrice;

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public double getTaxRate() {
            return taxRate;
        }

        public double getDiscount() {
            return discount;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
            this.finalPrice = finalPrice;
        }
    }
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "WOKb7oMk";
        String host = "36.50.135.242";
        int port = 2240;
        
        //channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        
        //stub
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);
        
        //req
        JudgeRequest jReq = JudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        JudgeResponse jRes = stub.request(jReq);
        System.out.println(">>>>> " + jRes);
        
        Gson gson = new Gson();
        Product p = gson.fromJson(jRes.getData(), Product.class);
        
        p.setFinalPrice(p.getPrice() * (1 + p.getTaxRate() / 100.0) - p.getDiscount());
        
//        String ans = String.valueOf(Math.round(p.getFinalPrice() * 100.0) / 100.0);
        String ans = String.format(Locale.US, "%.2f", p.getFinalPrice());
        
        //sub
        SubmitRequest sReq = SubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(jRes.getRequestId())
                .setAnswer(ans)
                .build();
        
        SubmitResponse sRes = stub.submit(sReq);
        System.out.println(">>>>>>> " + sRes);
    }
}
/*
Một dịch vụ gRPC JudgeService được triển khai trên server tại <Exam_IP>:2240.

Yêu cầu: Viết chương trình Java (gRPC client) để giao tiếp với JudgeService và thực hiện các công việc sau:

Gọi phương thức Request với student_code là mã sinh viên và question_alias là <question_alias trong đề bài>.

Nhận về JudgeResponse chứa request_id là chuỗi định danh và data là chuỗi JSON mô tả sản phẩm, ví dụ:

{"name":"ProductABC","price":150.0,"taxRate":10.0,"discount":15.0}
Trong đó discount là giá trị tuyệt đối (số tiền chiết khấu, không phải %).

Parse chuỗi JSON và tính giá cuối cùng theo công thức:

finalPrice = price × (1 + taxRate / 100) - discount
Gọi phương thức Submit với request_id là giá trị nhận được ở bước 1 và answer là giá trị finalPrice làm tròn 2 chữ số thập phân, dạng chuỗi, ví dụ "150.00".

Trong lời gọi Submit, request_id phải là giá trị đã nhận được ở bước 1.

Sai số cho phép: <= 0.01.

Ví dụ: price=150.0, taxRate=10.0, discount=15.0 -> finalPrice = 150 × 1.1 - 15 = 150.0 -> answer = "150.00"

Đóng kênh gRPC và kết thúc chương trình.
*/