/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thien.soa_2026.bt_grpc;

/**
 *
 * @author ADMIN
 */

import java.util.*;
import java.io.*;

import io.grpc.*;

import GRPC.*;

public class SortWordAlphabeticallyProb {
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "kS7EaAmF";
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
        JudgeRequest judgeReq = JudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        JudgeResponse judgeRes = stub.request(judgeReq);
        System.out.println(">>>>>> " + judgeRes);
        
        String[] a = judgeRes.getData().split(",");
        Arrays.sort(a);
        
        String ans = String.join(",", a);
        
        //sub
        SubmitRequest subReq = SubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setAnswer(ans)
                .setRequestId(judgeRes.getRequestId())
                .build();
        
        SubmitResponse subRes = stub.submit(subReq);
        System.out.println(">>>>>> " + subRes);
        
    }
}

/*
Một dịch vụ gRPC JudgeService được triển khai trên server tại <Exam_IP>:2240.

Yêu cầu: Viết chương trình Java (gRPC client) để giao tiếp với JudgeService và thực hiện các công việc sau:

Gọi phương thức Request với student_code là mã sinh viên và question_alias là <question_alias trong đề bài>.

Nhận về JudgeResponse chứa request_id là chuỗi định danh và data là các từ phân tách bằng dấu phẩy, ví dụ "banana,apple,cherry,date".

Parse chuỗi data thành danh sách từ, sắp xếp theo thứ tự từ điển (không phân biệt hoa thường - case-insensitive).

Gọi phương thức Submit với request_id là giá trị nhận được ở bước 1 và answer là danh sách từ đã sắp xếp, phân tách bằng dấu phẩy, ví dụ "apple,banana,cherry,date".

Trong lời gọi Submit, request_id phải là giá trị đã nhận được ở bước 1.

Ví dụ: data = "banana,apple,cherry" -> sort case-insensitive -> answer = "apple,banana,cherry"

Đóng kênh gRPC và kết thúc chương trình.
*/