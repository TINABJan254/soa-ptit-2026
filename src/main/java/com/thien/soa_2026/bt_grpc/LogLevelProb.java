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

public class LogLevelProb {
    
    public static void main(String[] args) throws Exception{
        String sCode = "B22DCCN827";
        String qCode = "NFNvCeah";
        String host = "36.50.135.242";
        int port = 2240;
        
        //channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        
        //stub
        TypedJudgeServiceGrpc.TypedJudgeServiceBlockingStub stub = TypedJudgeServiceGrpc.newBlockingStub(channel);
        
        //req
        TypedJudgeRequest jReq = TypedJudgeRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .build();
        
        TypedJudgeResponse jRes = stub.requestTyped(jReq);
        System.out.println(">>>>> " + jRes);
        
        //sovle
        TextBatchData data = jRes.getTextBatch();
        
        Map<String, Integer> counts = new HashMap<>();
        List<String> values = new ArrayList<>(); 
        for (String entry : data.getEntriesList()) {
            String[] tokens = entry.split("\\s+");
            counts.put(tokens[0], counts.getOrDefault(tokens[0], 0) + 1); 
            
            if (values.size() == 0) {
                for (String x : tokens) {
                    if (x.startsWith("code=")) {
                        values.add(x.substring(5));
                    }
                }
            }
        }
        
        //sub
        TextBatchAnswer ans = TextBatchAnswer.newBuilder()
                .addAllValues(values)
                .putAllCounts(counts)
                .build();
        
        TypedSubmitRequest sReq = TypedSubmitRequest.newBuilder()
                .setStudentCode(sCode)
                .setQuestionAlias(qCode)
                .setRequestId(jRes.getRequestId())
                .setTextBatchAnswer(ans)
                .build();
        
        TypedSubmitResponse sRes = stub.submitTyped(sReq);
        System.out.println(">>>>> " + sRes);
    }
}

/*
Nội dung
Phân loại mức độ log qua gRPC typed proto
Phase 1 nhận TextBatchData với mode=severity_counts và danh sách log entries.

Yêu cầu xử lý
Mức độ log là token đầu tiên của mỗi entry: INFO, WARN, hoặc ERROR.
counts: map đếm số log theo từng mức độ.
values: danh sách chỉ gồm mã lỗi đầu tiên xuất hiện trong entry có code=....
Submit
Gọi SubmitTyped với TextBatchAnswer. Không hardcode E001 vì các dòng ERROR ngẫu nhiên có thể xuất hiện trước dòng đảm bảo.
*/