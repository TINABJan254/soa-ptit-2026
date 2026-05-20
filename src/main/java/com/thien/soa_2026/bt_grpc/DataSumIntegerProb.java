/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

//generated class
import GRPC.*;

import java.io.*;
import java.util.*;

public class DataSumIntegerProb {
    public static void main(String[] args) throws Exception{
        
        String host = "36.50.135.242";
        int port = 2240;
        
        String sCode = "B22DCCN827";
        String qCode = "9BlXf4IM";
        
        //create channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext() //tat SSL
                .build();
        
        JudgeServiceGrpc.JudgeServiceBlockingStub stub = JudgeServiceGrpc.newBlockingStub(channel);
        
        //request
        JudgeRequest request = JudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        //send request
        JudgeResponse response = stub.request(request);
        
        //get data from response
        String reqId = response.getRequestId();
        String data  = response.getData();
        
        System.out.println(">>>>>> " + reqId + " " + data);
        
        //solve
        long sum = 0;
        String[] a = data.split(",");
        for (String x : a) {
            sum += Long.parseLong(x);
        }
        System.out.println(">>>>> " + sum);
        
        //submit
        SubmitRequest subReq = SubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(reqId)
                .setAnswer(String.valueOf(sum))
                .build();
        
        SubmitResponse subRes = stub.submit(subReq);
        System.out.println(subRes);
    }
    
}

/*
Nội dung
Một dịch vụ gRPC JudgeService được triển khai trên server tại <Exam_IP>:2240.

Yêu cầu: Viết chương trình Java (gRPC client) để giao tiếp với JudgeService và thực hiện các công việc sau:

Tạo kết nối gRPC tới server (plaintext, không TLS) và gọi phương thức Request với student_code là mã sinh viên và question_alias là <question_alias trong đề bài>.

Nhận về JudgeResponse chứa request_id là chuỗi định danh duy nhất, ví dụ "a1b2c3d4", và data là chuỗi các số nguyên phân tách bằng dấu phẩy, ví dụ "12,45,88,3,210".

Parse chuỗi data thành danh sách số nguyên và tính tổng.

Gọi phương thức Submit với student_code là mã sinh viên, question_alias là <question_alias trong đề bài>, request_id là giá trị nhận được ở bước 1, và answer là kết quả tổng dạng chuỗi, ví dụ "141".

Trong lời gọi Submit, request_id phải là giá trị đã nhận được ở bước 1.

Nhận về SubmitResponse chứa status ("AC" / "WA" / "RTE") và message.

Ví dụ: data = "1,2,3,4,5" -> tổng = 15 -> answer = "15"

Đóng kênh gRPC và kết thúc chương trình.

Proto Contract
syntax = "proto3";
package GRPC;
option java_package = "GRPC";
option java_multiple_files = true;

service JudgeService {
  rpc Request (JudgeRequest) returns (JudgeResponse);
  rpc Submit  (SubmitRequest) returns (SubmitResponse);
}

message JudgeRequest {
  string student_code    = 1;
  string question_alias  = 2;
}

message JudgeResponse {
  string request_id = 1;
  string data       = 2;
}

message SubmitRequest {
  string student_code    = 1;
  string question_alias  = 2;
  string request_id      = 3;
  string answer          = 4;
}

message SubmitResponse {
  string status  = 1;
  string message = 2;
}
*/